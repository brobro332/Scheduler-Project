package kr.co.scheduler.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.scheduler.user.repository.UserRepository;
import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/user/info")
    public String searchInfo(Principal principal, Model model) {

        model.addAttribute("info", userService.searchInfo(principal.getName()));
        model.addAttribute("img", userRepository.findByEmail(principal.getName()));

        return "user/info";
    }

    /**
     * kakaoCallback: 카카오 로그인 요청 및 콜백 응답받음
     */
    @GetMapping("/kakao/callback")
    public String kakaoCallback(HttpServletRequest request, String code) {

        userService.kakaoLogin(request, code);

        return "redirect:/";
    }

    /**
     * NaverCallback: 네이버 로그인 요청 및 콜백 응답받음
     */
    @GetMapping("/naver/callback")
    public String naverCallback(HttpServletRequest request, String code, String state) {

        userService.naverLogin(request, code, state);

        return "redirect:/";
    }

    @GetMapping("/user/info/updateList")
    public String updateSelect() {

        return "user/updateList";
    }

    @GetMapping("/user/info/updatePasswordForm")
    public String updatePasswordForm() {

        return "user/updatePasswordForm";
    }

    @GetMapping("/user/info/updateInfoForm")
    public String updateInfoForm() {

        return "user/updateInfoForm";
    }

    @GetMapping("/user/info/profileImgForm")
    public String profileImgForm() {

        return "user/profileImgForm";
    }
}
