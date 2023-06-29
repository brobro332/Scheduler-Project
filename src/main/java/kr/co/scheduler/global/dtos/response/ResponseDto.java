package kr.co.scheduler.global.dtos.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class ResponseDto<T> {

    private final Integer statusCode;
    private final String message;
    private final T data;

    /**
     * ofSuccess: 요청에 대하여 API 가 정상적으로 작동할 경우 응답
     * 1. Default
     * 2. Message : statusCode + message
     * 3. Data : statusCode + message + data
     */
    public static <T> ResponseDto<T> ofSuccess() {
        return new ResponseDto<>(HttpStatus.OK.value(), null, null);
    }

    public static <T> ResponseDto<T> ofSuccessMessage(String message) {
        return new ResponseDto<>(HttpStatus.OK.value(), message, null);
    }

    public static <T> ResponseDto<T> ofSuccessData(String message, T data) {
        return new ResponseDto<>(HttpStatus.OK.value(), message, data);
    }

    /**
     * ofFail: 요청에 대하여 API 가 정상적으로 작동하지 않을 경우 응답 
     * 1. Default : statusCode
     * 2. Message : statusCode + message
     * 3. Data : statusCode + message + data
     */
    public static <T> ResponseDto<T> ofFail(Integer statusCode) {
        return new ResponseDto<>(statusCode, null, null);
    }

    public static <T> ResponseDto<T> ofFailMessage(Integer statusCode, String message) {
        return new ResponseDto<>(statusCode, message, null);
    }

    public static <T> ResponseDto<T> ofFailData(Integer statusCode, String message, T data) {
        return new ResponseDto<>(statusCode, message, data);
    }
}