package kr.co.scheduler.global.entity.naver;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverOAuthToken {

    private String access_token;
    private String token_type;
    private String refresh_token;
    private Integer expires_in;
    private String error;
    private Integer error_description;
}
