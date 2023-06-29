package kr.co.scheduler.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {

        return "index";
    }

    @GetMapping("/signUpForm")
    public String signUpForm() {

        return "signUpForm";
    }

    @GetMapping("/signInForm")
    public String signInForm(@RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "exception", required = false) String exception,
                             Model model) {

        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "signInForm";
    }
}
