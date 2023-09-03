package kr.co.scheduler.user.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.co.scheduler.global.config.mail.RegisterMail;
import kr.co.scheduler.global.dtos.ResponseDto;
import kr.co.scheduler.user.dtos.UserReqDTO;
import kr.co.scheduler.user.entity.User;
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
    private final RegisterMail registerMail;

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
     * updatePassword: 회원 비밀번호 수정
     * 1. 입력 데이터 검증
     * 2. 현재 비밀번호 검증
     * 3. 비밀번호와 확인용 비밀번호 일치 검증
     * 4. 입력된 정보 등록
     */
    @PutMapping("/api/user/update/password")
    public ResponseDto<Object> updatePassword(@Valid @RequestBody UserReqDTO.UPDATE_PASSWORD update,
                                          BindingResult bindingResult,
                                          Principal principal) {

        // 입력 데이터 검증
        if (bindingResult.hasErrors()) {

            Map<String, String> validateResult
                    = userService.validateHandling(bindingResult);

            return ResponseDto.ofFailData(
                    HttpStatus.BAD_REQUEST.value(),
                    "회원정보 수정에 실패했습니다.",
                    validateResult);
        }

        // 현재 비밀번호 일치 검증
        boolean incorrectPrevPassword
                = userService.validatePrevPassword(principal.getName(), update.getPrevPassword());

        if (incorrectPrevPassword) {

            return ResponseDto.ofFailMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    "현재 비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호와 확인용 비밀번호 일치 검증
        boolean incorrectCheckedPassword
                = userService.validateCheckedPassword(update.getPassword(), update.getCheckedPassword());

        if (incorrectCheckedPassword) {

            return ResponseDto.ofFailMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    "확인용 비밀번호가 일치하지 않습니다.");
        }

        // 회원정보 수정
        userService.updatePassword(update, principal.getName());
        
        return ResponseDto.ofSuccessData(
                "패스워드를 성공적으로 수정하였습니다.",
                null);
    }

    /**
     * updateInfo: 회원정보 수정
     * 1. 입력 데이터 검증
     * 2. 입력된 정보 등록
     */
    @PutMapping("/api/user/update/info")
    public ResponseDto<Object> updateInfo(@Valid @RequestBody UserReqDTO.UPDATE_INFO update,
                                              BindingResult bindingResult,
                                              Principal principal) {

        // 입력 데이터 검증
        if (bindingResult.hasErrors()) {

            Map<String, String> validateResult
                    = userService.validateHandling(bindingResult);

            return ResponseDto.ofFailData(
                    HttpStatus.BAD_REQUEST.value(),
                    "회원정보 수정에 실패했습니다.",
                    validateResult);
        }

        // 회원정보 수정
        userService.updateInfo(update, principal.getName());

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
    @GetMapping("/api/user/info/profileImg")
    public ResponseEntity<?> getProfileImg(Principal principal) throws IOException {

        User user = userService.findUser(principal.getName());

        if(user == null) {

            return new ResponseEntity<>("회원 프로필 이미지 조회에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }

        InputStream inputStream = new FileInputStream(user.getProfileImgPath());
        byte[] imageByteArray = IOUtils.toByteArray(inputStream);
        inputStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }

    /**
     * uploadProfileImg: 프로필이미지를 등록 및 수정
     * 1. 파일이 업로드 되었는지 확인
     * 2. 파일이 업로드 되어있지 않다면 실패 메세지 출력
     * 3. 이미 등록된 프로필이미지가 있다면 삭제
     * 4. 업로드 된 프로필 이미지 등록
     */
    @PutMapping("/api/user/info/profileImg/update")
    public ResponseDto<?> uploadProfileImg(Principal principal,
                                           @RequestPart(value = "uploadImg", required = false) MultipartFile uploadImg) throws IOException {

        // 프로필 이미지가 업로드 되었는지 검사
        if (uploadImg != null) {

            User user = userService.findUser(principal.getName());

            if(user == null) {

                return ResponseDto.ofFailMessage(
                        HttpStatus.BAD_REQUEST.value(),
                        "회원 프로필이미지 조회에 실패하였습니다.");
            }

            String filePath = user.getProfileImgPath();

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
        } else {

            return ResponseDto.ofFailMessage(HttpStatus.NOT_IMPLEMENTED.value(), "기존 프로필이미지 삭제에 실패하였습니다.");
        }
        // 업로드된 이미지 등록
        userService.uploadProfileImg(principal.getName(), uploadImg);
        
        return ResponseDto.ofSuccessData(
                "프로필이미지를 성공적으로 등록하였습니다.",
                null);
    }

    /**
     * deleteProfileImg: 프로필이미지를 삭제
     * 1. 업로드된 프로필이미지 파일을 삭제
     * 2. DB 에서 프로필이미지에 대한 데이터에 null 값을 넣음
     */
    @PostMapping("/api/user/info/profileImg/delete")
    public ResponseDto<?> deleteProfileImg(Principal principal, HttpSession session) {

        User user = userService.findUser(principal.getName());

        if(user == null) {

            return ResponseDto.ofFailMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    "회원 프로필이미지 조회에 실패하였습니다.");
        }

        File file = null;

        try {

            file = new File(URLDecoder.decode(user.getProfileImgPath(), StandardCharsets.UTF_8));

            file.delete();

            userService.deleteImgData(principal.getName(), session);
        } catch(Exception e) {

            log.error("fail to delete profileImg", e);

            return ResponseDto.ofFailMessage(HttpStatus.NOT_IMPLEMENTED.value(), "프로필이미지 삭제에 실패하였습니다.");
        }

        return ResponseDto.ofSuccessData(
                "프로필이미지를 성공적으로 삭제하였습니다.",
                null);
    }

    @PostMapping("/api/user/mailCertify")
    @ResponseBody
    String mailConfirm(@RequestParam("email") String email) throws Exception {

        String code = registerMail.sendSimpleMessage(email);
        System.out.println("인증코드 : " + code);

        return code;
    }
}