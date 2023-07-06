package kr.co.scheduler.global.config.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
public class WebMvcConfigurer implements org.springframework.web.servlet.config.annotation.WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new ProfileImgInterceptor())
                .addPathPatterns("/user/info");
    }
}
