package kr.co.scheduler.user.controller.api;

import kr.co.scheduler.user.dtos.UserReqDTO;
import kr.co.scheduler.user.repository.UserRepository;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * login : 로그인
     */
    @PostMapping("/login")
    public String login(UserReqDTO userReqDTO) {
        return userService.login(userReqDTO);
    }
}
