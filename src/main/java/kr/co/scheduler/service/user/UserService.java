package kr.co.scheduler.service.user;

import kr.co.scheduler.dto.UserReqDTO;

import java.util.Map;

public interface UserService {

    /**
     * signUp : 회원가입
     */
    public Long signUp(UserReqDTO userReqDTO) throws Exception;

    /**
     * login : 로그인
     */
    String login(Map<String, String> users);
}
