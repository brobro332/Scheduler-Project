package kr.co.scheduler.global.controller;

import kr.co.scheduler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserService userService;

    /**
     * index: 인덱스 페이지를 리턴
     */
    @GetMapping("/")
    public String index(Principal principal) {

        if (principal != null) {

            userService.updateLastLoggedDay(principal.getName());
        }

        return "index";
    }

    /**
     * signUp: 회원가입 페이지를 리턴
     */
    @GetMapping("/signUp")
    public String signUp() {

        return "signUp";
    }

    /**
     * signIn: 로그인 페이지를 리턴
     * 로그인 과정에서 발생하는 오류나 예외 메세지를 사용자에게 표시
     */
    @GetMapping("/signIn")
    public String signIn(@RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "exception", required = false) String exception,
                             Model model) {

        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "signIn";
    }
}
