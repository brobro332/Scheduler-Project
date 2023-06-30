package kr.co.scheduler.user.controller;

import jakarta.validation.Valid;
import kr.co.scheduler.global.dtos.ResponseDto;
import kr.co.scheduler.user.dtos.UserReqDTO;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    /**
     * signUp: 회원가입
     * 1. 입력 데이터 검증
     * 2. 요청한 이메일이 이미 가입된 계정인지 검증
     */
    @PostMapping("/signUp")
    public ResponseDto<Object> signUp(@Valid @RequestBody UserReqDTO.CREATE create, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {

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
     * 2. 요청한 이메일이 이미 가입된 계정인지 검증
     */
    @PutMapping("/api/user/update")
    public ResponseDto<Object> updateInfo(@Valid @RequestBody UserReqDTO.UPDATE update,
                                          BindingResult bindingResult,
                                          Principal principal) {

        if(bindingResult.hasErrors()) {

            Map<String, String> validateResult
                    = userService.validateHandling(bindingResult);

            return ResponseDto.ofFailData(
                    HttpStatus.BAD_REQUEST.value(),
                    "회원정보 수정에 실패했습니다.",
                    validateResult);
        }

        boolean incorrectCheckedPassword
                = userService.validateCheckedPassword(update.getPassword(), update.getCheckedPassword());

        if (incorrectCheckedPassword) {

            return ResponseDto.ofFailMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    "확인용 비밀번호가 일치하지 않습니다.");
        }

        userService.updateInfo(update, principal.getName());

        return ResponseDto.ofSuccessData(
                "회원정보를 성공적으로 수정하였습니다.",
                null);
    }

}