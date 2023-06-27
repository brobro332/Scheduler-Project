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
<<<<<<< HEAD
    public String login(UserReqDTO userReqDTO) {
=======
    public String login(@RequestBody UserReqDTO userReqDTO) {

>>>>>>> 3098ee8adbdaece15e98ec95d1296e495deeaa8e
        return userService.login(userReqDTO);
    }
}
