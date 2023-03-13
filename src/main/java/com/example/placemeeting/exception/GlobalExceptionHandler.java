package com.example.placemeeting.exception;

import com.example.placemeeting.global.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomCommonException.class)
    public ResponseEntity<ResponseDto<Object>> customCommonException(CustomCommonException e) {
        /*
         CustomCommonException이 발생했을 시, 에러 이벤트와 함께 들어오는 메시지를 출력한다
         ex) by zero
        */
        e.getMessage();

        /*
         CustomCommonException이 발생했을 시, 예외가 난 곳과 예외 메시지를 출력해준다 이를 이용해 예외에 대한 상세한 StackTrace를 보며 예외가 일어난 곳의 위치를 상세하게 알 수 있다
         ex) java.lang.ArithmeticException: /by zero
         at TryEx12.main(TryEx12.java:8)
        */
        e.printStackTrace();
        return new ResponseEntity<>(ResponseDto.fail(e.getStatus(), e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Object>> exception(Exception e) {
        e.printStackTrace();
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(ResponseDto.fail(
                errorCode.getStatus(), errorCode.getHttpStatus(), errorCode.getMessage()),
                errorCode.getHttpStatus());
    }
}
