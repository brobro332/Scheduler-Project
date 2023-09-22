package kr.co.scheduler.global.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class ReferrerInterceptor implements HandlerInterceptor {

    /**
     * Referer 헤더: 현재 요청을 보낸 이전 페이지의 URL
     * Host 헤더: 현재 요청이 전송된 도메인
     * Referer 가 null 이거나 Referer 에 Host 값이 포함되어 있지 않으면 요청 거부
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String referer = request.getHeader("Referer");
        String host = request.getHeader("host");
        if (referer == null || !referer.contains(host)) {
            response.sendRedirect("/");

            return false;
        }

        return true;
    }
}
