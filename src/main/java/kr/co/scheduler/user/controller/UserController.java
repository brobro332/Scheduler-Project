package kr.co.scheduler.user.controller;

import jakarta.validation.Valid;
import kr.co.scheduler.user.dtos.UserReqDTO;
import kr.co.scheduler.user.entity.User;
import kr.co.scheduler.user.repository.UserRepository;
import kr.co.scheduler.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     *  Index
     *  1. index(): 웹에 접속했을 때, 인덱스 페이지
     *  2. http://localhost:8080/
     */
    @GetMapping({"","/"})
    public String index() {

        return "index";
    }

    
    /**
     * Login
     * 1. loginForm(): 로그인폼을 리턴한다.
     * 2. login(): 로그인 수행
     */
    @GetMapping("/loginForm")
    public String login() {

        return "loginForm";
    }


    /**
     * Join
     * 1. joinForm(): 회원가입폼을 리턴한다.
     * 2. join(): 회원가입 성공 시 "/login"으로 리다이렉트
     */
    @GetMapping("/joinForm")
    public String joinForm() {

        return "joinForm";
    }

    @PostMapping("/join")
    public String join(@Valid UserReqDTO userReqDTO) throws Exception{

        userService.signUp(userReqDTO);

        return "redirect:/login";
    }
}
