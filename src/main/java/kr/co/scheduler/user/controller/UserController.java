package kr.co.scheduler.user.controller;

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
