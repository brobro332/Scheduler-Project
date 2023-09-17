package kr.co.scheduler.global.config.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
public class WebMvcConfigurer implements org.springframework.web.servlet.config.annotation.WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new ProfileImgInterceptor())
                // navbar 에 프로필 이미지 표시하기 위함 
                .addPathPatterns("/user/info");
    }
}
