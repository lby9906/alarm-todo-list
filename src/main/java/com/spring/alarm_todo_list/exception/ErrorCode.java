package com.spring.alarm_todo_list.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    EXISTING_ALREADY_ACCOUNT(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
    NOT_FOUND_ACCOUNT(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    NOT_FOUND_ACCOUNT_REGISTER_BOARD(HttpStatus.NOT_FOUND, "회원이 등록한 일정을 찾을 수 없습니다."),
    UNABLE_TO_SEND_SMS(HttpStatus.BAD_REQUEST, "문자를 보낼 수 없습니다."),
    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
