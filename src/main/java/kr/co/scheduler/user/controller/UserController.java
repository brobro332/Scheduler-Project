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


    /**
     * selectUserInfo: 사용자 정보 조회 페이지를 리턴
     */
    @GetMapping("/user/info")
    public String selectUserInfo(Principal principal, Model model) {

        model.addAttribute("info", userService.searchInfo(principal.getName()));
        model.addAttribute("img", userRepository.findByEmail(principal.getName()));

        return "user/info";
    }

    /**
     * selectUpdateList: 수정 목록 선택하는 페이지 리턴
     * 1. 사용자 정보 수정
     * 2. 패스워드 수정
     */
    @GetMapping("/user/info/updateList")
    public String selectUpdateList() {

        return "user/updateList";
    }

    /**
     * updateInfo: 사용자 정보 수정 페이지 리턴
     */
    @GetMapping("/user/info/updateInfo")
    public String updateInfo() {

        return "user/updateInfo";
    }


    /**
     * updatePasswordForm: 패스워드 수정 페이지 리턴
     */
    @GetMapping("/user/info/updatePW")
    public String updatePW() {

        return "user/updatePW";
    }

    /**
     * profileImg: 프로필 이미지 변경 페이지 리턴
     */
    @GetMapping("/user/info/profileImg")
    public String profileImg() {

        return "user/profileImg";
    }

    // ================================== 구분 ================================== //

    /**
     * kakaoCallback: 카카오 로그인 요청 및 콜백 응답받아 리다이렉트
     */
    @GetMapping("/kakao/callback")
    public String kakaoCallback(HttpServletRequest request, String code) {

        userService.kakaoLogin(request, code);

        return "redirect:/";
    }

    /**
     * NaverCallback: 네이버 로그인 요청 및 콜백 응답받아 리다이렉트
     */
    @GetMapping("/naver/callback")
    public String naverCallback(HttpServletRequest request, String code, String state) {

        userService.naverLogin(request, code, state);

        return "redirect:/";
    }
}
