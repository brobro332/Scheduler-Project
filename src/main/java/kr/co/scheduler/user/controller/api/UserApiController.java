package kr.co.scheduler.user.controller.api;

import jakarta.validation.Valid;
import kr.co.scheduler.global.dtos.response.ResponseDto;
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
    @PostMapping("/signUp")
    public ResponseDto<?> signUp(@Valid @RequestBody UserReqDTO userReqDTO, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            Map<String, String> validateResult = userService.validateHandling(bindingResult);

            return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), validateResult);
        }

        userService.signUp(userReqDTO);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
}