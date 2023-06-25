package kr.co.scheduler.user.service;

import kr.co.scheduler.user.dtos.UserReqDTO;

import java.util.Map;

public interface UserService {

    /**
     * signUp : 회원가입
     */
    public Long signUp(UserReqDTO userReqDTO) throws Exception;

    /**
     * login : 로그인
     */
    String login(UserReqDTO userReqDTO);
}
