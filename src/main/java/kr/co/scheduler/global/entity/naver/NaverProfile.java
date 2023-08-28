package kr.co.scheduler.global.entity.naver;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NaverProfile {
    private String resultcode;
    private String message;
    private Response response;

    @Getter
    @Setter
    public class Response {

        public String id;
        public String nickname;
        public String name;
        public String email;
        public String gender;
        public String age;
        public String birthday;
        public String profile_image;
        public String birthyear;
        public String mobile;
        public String mobile_e164;
    }
}
