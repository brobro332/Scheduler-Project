package kr.co.scheduler.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.co.scheduler.global.config.mail.RegisterMail;
import kr.co.scheduler.global.dtos.ResponseDto;
import kr.co.scheduler.global.dtos.TargetTokenReqDTO;
import kr.co.scheduler.global.service.ImgService;
import kr.co.scheduler.user.dtos.UserReqDTO;
import kr.co.scheduler.user.entity.User;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final ImgService imgService;
    private final RegisterMail registerMail;

    /**
     * signUp: 회원가입
     * 1. 입력 데이터에 문제 있을 경우 ValidateResult 리턴
     * 2. 비밀번호와 확인용 비밀번호 일치 여부 확인
     * 3. 이미 가입된 회원인지 확인
     * 4. 회원 데이터 저장
     */
    @PostMapping("/signUp")
    public ResponseDto<Object> signUp(@Valid @RequestBody UserReqDTO.CREATE create, BindingResult bindingResult) {

        // 입력 데이터에 문제 있을 경우 ValidateResult 리턴
        if (bindingResult.hasErrors()) {

            Map<String, String> validateResult = userService.validateHandling(bindingResult);

            return ResponseDto.ofFailData(
                    HttpStatus.BAD_REQUEST.value(),
                    "회원정보 등록에 실패했습니다.",
                    validateResult);
        }

        // 비밀번호와 확인용 비밀번호 일치 여부 확인
        boolean incorrectCheckedPassword =
                userService.validateCheckedPassword(create.getPassword(), create.getCheckedPassword());

        if (incorrectCheckedPassword) {

            return ResponseDto.ofFailMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    "확인용 비밀번호가 일치하지 않습니다.");
        }

        // 이미 가입된 회원인지 확인
        boolean duplication = userService.validateDuplication(create);

        if (duplication) {

            return ResponseDto.ofFailMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    "이미 가입된 회원입니다.");
        }

        // 회원 데이터 저장
        userService.signUp(create);

        return ResponseDto.ofSuccessData(
                "회원정보가 성공적으로 등록되었습니다.",
                null);
    }

    /**
     * selectInfo: 회원정보 조회
     * 1. Principal 객체에서 현재 세션의 이메일 값을 Service 로 전달
     * 2. 요청한 이메일 값을 통해 회원정보 조회
     */
    @GetMapping("/api/user/info")
    public ResponseDto<Object> selectInfo(Principal principal) {

        return ResponseDto.ofSuccessData(
                "회원정보를 성공적으로 조회하였습니다.",
                userService.searchInfo(principal.getName()));
    }

    /**
     * updateInfo: 회원정보 수정
     * 1. 입력 데이터에 문제 있을 경우 ValidateResult 리턴
     * 2. 입력된 정보로 회원 정보 수정
     */
    @PutMapping("/api/user/info")
    public ResponseDto<Object> updateInfo(@Valid @RequestBody UserReqDTO.UPDATE_INFO update,
                                          BindingResult bindingResult,
                                          Principal principal) {

        // 입력 데이터에 문제 있을 경우 ValidateResult 리턴
        if (bindingResult.hasErrors()) {

            Map<String, String> validateResult
                    = userService.validateHandling(bindingResult);

            return ResponseDto.ofFailData(
                    HttpStatus.BAD_REQUEST.value(),
                    "회원정보 수정에 실패했습니다.",
                    validateResult);
        }

        // 입력된 정보로 회원 정보 수정
        userService.updateInfo(update, principal.getName());

        return ResponseDto.ofSuccessData(
                "회원정보를 성공적으로 수정하였습니다.",
                null);
    }

    /**
     * updatePW: 회원 비밀번호 수정
     * 1. 입력 데이터에 문제 있을 경우 ValidateResult 리턴
     * 2. 현재 비밀번호 확인
     * 3. 비밀번호와 확인용 비밀번호 일치 여부 확인
     * 4. 입력된 정보로 회원 비밀번호 수정
     */
    @PutMapping("/api/user/password")
    public ResponseDto<Object> updatePW(@Valid @RequestBody UserReqDTO.UPDATE_PASSWORD update,
                                          BindingResult bindingResult,
                                          Principal principal) {

        // 입력 데이터에 문제 있을 경우 ValidateResult 리턴
        if (bindingResult.hasErrors()) {

            Map<String, String> validateResult
                    = userService.validateHandling(bindingResult);

            return ResponseDto.ofFailData(
                    HttpStatus.BAD_REQUEST.value(),
                    "회원정보 수정에 실패했습니다.",
                    validateResult);
        }

        // 현재 비밀번호 확인
        boolean incorrectPrevPassword
                = userService.validatePrevPassword(principal.getName(), update.getPrevPassword());

        if (incorrectPrevPassword) {

            return ResponseDto.ofFailMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    "현재 비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호와 확인용 비밀번호 일치 여부 확인
        boolean incorrectCheckedPassword
                = userService.validateCheckedPassword(update.getPassword(), update.getCheckedPassword());

        if (incorrectCheckedPassword) {

            return ResponseDto.ofFailMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    "확인용 비밀번호가 일치하지 않습니다.");
        }

        // 입력된 정보로 회원 데이터 수정
        userService.updatePassword(update, principal.getName());
        
        return ResponseDto.ofSuccessData(
                "패스워드를 성공적으로 수정하였습니다.",
                null);
    }

    // ================================== 구분 ================================== //

    /**
     * uploadProfileImg: 프로필이미지를 등록 및 수정
     * 1. 파일이 업로드 되었는지 확인
     * 2. 이미 등록된 이미지가 있다면 기존 이미지를 삭제
     * 3. 업로드 된 프로필 이미지 등록
     */
    @PutMapping("/api/user/info/profileImg")
    public ResponseDto<?> uploadProfileImg(Principal principal,
                                           @RequestPart(value = "uploadImg", required = false) MultipartFile uploadImg) throws IOException {

        // 파일이 업로드 되었는지 확인
        if (uploadImg != null) {

            User user = userService.selectUser(principal.getName());

            if(user == null) {

                return ResponseDto.ofFailMessage(
                        HttpStatus.BAD_REQUEST.value(),
                        "회원 프로필이미지 조회에 실패하였습니다.");
            }

            String imgPath = user.getProfileImgPath();

            // 이미 등록된 이미지가 있다면 기존 이미지를 삭제
            if (imgPath != null) {

                imgService.deleteImg(imgPath);
            }
        } else {

            return ResponseDto.ofFailMessage(HttpStatus.BAD_REQUEST.value(), "업로드된 프로필이미지가 없습니다.");
        }

        //업로드 된 프로필 이미지 등록
        userService.uploadProfileImg(principal.getName(), uploadImg);
        
        return ResponseDto.ofSuccessData(
                "프로필이미지를 성공적으로 등록하였습니다.",
                null);
    }

    /**
     * deleteProfileImg: 프로필이미지를 삭제
     */
    @DeleteMapping("/api/user/info/profileImg")
    public ResponseDto<?> deleteProfileImg(Principal principal) {

        userService.deleteImg(principal.getName());

        return ResponseDto.ofSuccessData(
                "프로필이미지를 성공적으로 삭제하였습니다.",
                null);
    }

    // ================================== 구분 ================================== //

    /**
     * emailCode: 회원가입시 이메일 인증 registerMail 로 이메일 코드 전송
     */
    @PostMapping("/api/user/emailCode")
    @ResponseBody
    String emailCode(@RequestParam("email") String email) throws Exception {

        String code = registerMail.sendSimpleMessage(email);
        System.out.println("인증코드 : " + code);

        return code;
    }

    // ================================== 구분 ================================== //

    @GetMapping("/api/user/loginStatus")
    public ResponseDto<?> selectLoginStatus() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            // 사용자가 로그인한 상태
            return ResponseDto.ofSuccessMessage("로그인한 사용자 입니다.");
        } else {
            // 사용자가 로그인하지 않은 상태
            return ResponseDto.ofFailMessage(HttpStatus.UNAUTHORIZED.value(), "로그인하지 않은 사용자입니다.");
        }
    }

    @PutMapping("/api/user/targetToken")
    public ResponseDto<?> updateTargetToken(@RequestBody TargetTokenReqDTO targetTokenReqDTO, Principal principal) {

        User user = userService.selectUser(principal.getName());

        if (user != null) {

            userService.updateTargetToken(targetTokenReqDTO.getTargetToken(), user);

            return ResponseDto.ofSuccessMessage("TargetToken이 정상적으로 등록되었습니다.");
        }

        return ResponseDto.ofFailMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "TargetToken 등록에 실패하였습니다.");
    }
}