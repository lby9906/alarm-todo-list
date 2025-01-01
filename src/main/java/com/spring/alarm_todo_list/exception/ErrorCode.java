package com.spring.alarm_todo_list.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    EXISTING_ALREADY_ACCOUNT(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
    NOT_FOUNT_ACCOUNT(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
