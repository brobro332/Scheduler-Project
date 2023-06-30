package kr.co.scheduler.user.controller;

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

    @GetMapping("/user/info")
    public String searchInfo(Principal principal, Model model) {

        model.addAttribute("info", userService.searchInfo(principal.getName()));

        return "user/info";
    }

    @GetMapping("/user/info/updateForm")
    public String updateForm() {

        return "user/updateForm";
    }
}
