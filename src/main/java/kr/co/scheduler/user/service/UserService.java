package kr.co.scheduler.user.service;

import kr.co.scheduler.user.dtos.UserReqDTO;


public interface UserService {

    public Long signUp(UserReqDTO userReqDTO) throws Exception;

    String login(UserReqDTO userReqDTO);
}
