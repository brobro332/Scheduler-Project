package kr.co.scheduler.global.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:C:/tools/sap_pe_nb.txt")
public class OAuthProperties {

    @Autowired
    private Environment env;

    public void myMethod() {

        String oauthNaverKey = env.getProperty("OAUTH_NAVER_KEY");
        String oauthKey = env.getProperty("OAUTH_KEY");
        String mailPw = env.getProperty("MAIL_PW");
        String keyStorePw = env.getProperty("KEY_STORE_PW");
    }
}
