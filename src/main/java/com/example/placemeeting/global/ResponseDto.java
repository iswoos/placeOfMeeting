package com.example.placemeeting.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private boolean success;
    private T data;
    private Error error;

    //클래스 안의 메소드여서 양옆으로 제네릭 삽입!
    // 양옆으로 제네릭 쓰는 거 공부!
    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(true, data, null);
    }

    public static <T> ResponseDto<T> fails(HttpStatus httpStatus, String message) {
        return new ResponseDto<>(false, null, new Error(httpStatus,message));
    }
}
