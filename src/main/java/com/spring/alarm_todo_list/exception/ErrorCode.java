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
    NOT_MATCH_EMAIL_PASSWORD(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 일치하지 않습니다."),
    EXPIRE_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "이미 만료된 토큰입니다."),
    NOT_FOUND_LOGIN_ACCOUNT(HttpStatus.NOT_FOUND, "로그아웃 된 사용자입니다."),
    NOT_MATCH_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "refresh 토큰이 일치하지 않습니다."),
    EMPTY_JWT_TOKEN(HttpStatus.BAD_REQUEST, "JWT 토큰이 존재하지 않습니다."),
    AUTH_INVALID_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 토큰입니다."),
    NOT_FOUNT_ACCOUNT_INFO(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    INVALID_PAGE_SIZE(HttpStatus.BAD_REQUEST, "페이지가 올바르지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
