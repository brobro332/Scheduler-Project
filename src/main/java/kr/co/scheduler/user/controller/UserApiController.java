package kr.co.scheduler.user.controller;

import jakarta.validation.Valid;
import kr.co.scheduler.global.dtos.ResponseDto;
import kr.co.scheduler.user.dtos.UserReqDTO;
import kr.co.scheduler.user.entity.User;
import kr.co.scheduler.user.repository.UserRepository;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * signUp: 회원가입
     * 1. 입력 데이터 검증
     * 2. 비밀번호와 확인용 비밀번호 일치 검증
     * 3. 이미 가입된 회원인지 검증
     * 4. 데이터 저장
     */
    @PostMapping("/signUp")
    public ResponseDto<Object> signUp(@Valid @RequestBody UserReqDTO.CREATE create, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            Map<String, String> validateResult = userService.validateHandling(bindingResult);

            return ResponseDto.ofFailData(
                    HttpStatus.BAD_REQUEST.value(),
                    "회원정보 등록에 실패했습니다.",
                    validateResult);
        }

        boolean incorrectCheckedPassword =
                userService.validateCheckedPassword(create.getPassword(), create.getCheckedPassword());

        if (incorrectCheckedPassword) {

            return ResponseDto.ofFailMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    "확인용 비밀번호가 일치하지 않습니다.");
        }

        boolean duplication = userService.validateDuplication(create);

        if (duplication) {

            return ResponseDto.ofFailMessage(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "이미 가입된 회원입니다.");
        }

        userService.signUp(create);

        return ResponseDto.ofSuccessData(
                "회원정보가 성공적으로 등록되었습니다.",
                null);
    }

    /**
     * searchInfo: 회원정보 조회
     * 1. Principal 객체에서 현재 세션의 이메일 값을 Service 로 전달
     * 2. 요청한 이메일 값을 통해 회원정보 조회
     */
    @GetMapping("/api/user/info")
    public ResponseDto<Object> searchInfo(Principal principal) {

        return ResponseDto.ofSuccessData(
                "회원정보를 성공적으로 조회하였습니다.",
                userService.searchInfo(principal.getName()));
    }

    /**
     * updateInfo: 회원정보 수정
     * 1. 입력 데이터 검증
     * 2. 비밀번호와 확인용 비밀번호 일치 검증
     * 3. 프로필이미지를 등록하는지 검사
     * 3-1. 이미 등록된 이미지가 있다면 기존 이미지를 삭제, 없다면 그대로 등록
     * 3-2. 업로드된 이미지가 없다면 나머지 정보만 수정
     */
    @PutMapping("/api/user/update")
    public ResponseDto<Object> updateInfo(@RequestPart("update") @Valid UserReqDTO.UPDATE update,
                                          BindingResult bindingResult,
                                          Principal principal,
                                          @RequestPart(value = "uploadImg", required = false) MultipartFile uploadImg) throws IOException {

        // 입력 데이터 검증
        if (bindingResult.hasErrors()) {

            Map<String, String> validateResult
                    = userService.validateHandling(bindingResult);

            return ResponseDto.ofFailData(
                    HttpStatus.BAD_REQUEST.value(),
                    "회원정보 수정에 실패했습니다.",
                    validateResult);
        }

        // 비밀번호와 확인용 비밀번호 일치 검증
        boolean incorrectCheckedPassword
                = userService.validateCheckedPassword(update.getPassword(), update.getCheckedPassword());

        if (incorrectCheckedPassword) {

            return ResponseDto.ofFailMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    "확인용 비밀번호가 일치하지 않습니다.");
        }

        // 프로필 이미지를 등록하는지 검사
        if (uploadImg != null) {

            String filePath = userRepository.findByEmail(principal.getName()).getProfileImgPath();

            // 이미 등록된 이미지가 있다면 기존 이미지를 삭제
            if (filePath != null) {

                File file = null;

                try {

                    file = new File(URLDecoder.decode(filePath, StandardCharsets.UTF_8));

                    file.delete();

                } catch (Exception e) {

                    log.error("fail to delete profileImg", e);

                    return ResponseDto.ofFailMessage(HttpStatus.NOT_IMPLEMENTED.value(), "기존 프로필이미지 삭제에 실패하였습니다.");
                }
            }

            // 이미 등록된 이미지가 없다면 그대로 등록
            userService.updateInfoWithImg(update, principal.getName(), uploadImg);
        } else {

            // 업로드된 이미지가 없다면 나머지 정보만 수정
            userService.updateInfo(update, principal.getName());
        }

        return ResponseDto.ofSuccessData(
                "회원정보를 성공적으로 수정하였습니다.",
                null);
    }

    /**
     * getProfileImg: 프로필이미지를 뷰로 전송
     * 뷰에서 바이트 단위의 데이터를 받기 위해서는
     * HttpStatus, HttpHeaders, HttpBody 를 모두 포함해야하기 때문에
     * ResponseDTO 가 아닌 ResponseEntity 를 응답한다.
     */
    @GetMapping("/user/info/profileImg")
    public ResponseEntity<?> getProfileImg(Principal principal) throws IOException {

        User user = userRepository.findOptionalByEmail(principal.getName())
                .orElseThrow(() -> {
                    return new IllegalArgumentException("가입되지 않은 회원입니다.");
                });

        InputStream inputStream = new FileInputStream(user.getProfileImgPath());
        byte[] imageByteArray = IOUtils.toByteArray(inputStream);
        inputStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }

    @PostMapping("/api/user/info/profileImg/delete")
    public ResponseDto<?> deleteProfileImg(Principal principal) {

        User user = userRepository.findByEmail(principal.getName());

        File file = null;

        try {

            file = new File(URLDecoder.decode(user.getProfileImgPath(), StandardCharsets.UTF_8));

            file.delete();

            userService.deleteImgInDataBase(principal.getName());

        } catch(Exception e) {

            log.error("fail to delete profileImg", e);

            return ResponseDto.ofFailMessage(HttpStatus.NOT_IMPLEMENTED.value(), "프로필이미지 삭제에 실패하였습니다.");
        }

        return ResponseDto.ofSuccessData(
                "프로필이미지를 성공적으로 삭제하였습니다.",
                null);
    }
}