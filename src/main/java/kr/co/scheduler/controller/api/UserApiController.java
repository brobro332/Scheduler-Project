package kr.co.scheduler.controller.api;

import jakarta.validation.Valid;
import kr.co.scheduler.dto.UserReqDTO;
import kr.co.scheduler.repository.UserRepository;
import kr.co.scheduler.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserApiController {

    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * join : 회원가입
     */
    @PostMapping("/join")
    @ResponseStatus(HttpStatus.OK)
    public Long join(@Valid @RequestBody UserReqDTO userReqDTO) throws Exception{

        return userService.signUp(userReqDTO);
    }

    /**
     * login : 로그인
     */
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> users) {
        return userService.login(users);
    }
}
