package kr.co.scheduler.user.controller;

import jakarta.validation.Valid;
import kr.co.scheduler.global.dtos.ResponseDto;
import kr.co.scheduler.user.dtos.UserReqDTO;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseDto<Object> signUp(@Valid @RequestBody UserReqDTO userReqDTO, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            Map<String, String> validateResult = userService.validateHandling(bindingResult);

            return ResponseDto.ofFailData(HttpStatus.BAD_REQUEST.value(), "회원정보 등록에 실패했습니다.", validateResult);
        }

        Boolean duplication = userService.validateDuplication(userReqDTO);

        if (duplication) {

            return ResponseDto.ofFailMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미 가입된 회원입니다.");
        }

        userService.signUp(userReqDTO);

        return ResponseDto.ofSuccessData("회원정보가 성공적으로 등록되었습니다.", null);
    }
}