package com.spring.alarm_todo_list.exception;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ErrorHandler {

    public ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    public ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }
}
