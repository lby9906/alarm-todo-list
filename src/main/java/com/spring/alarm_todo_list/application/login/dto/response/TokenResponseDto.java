package com.spring.alarm_todo_list.application.login.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class TokenResponseDto {

    private final String accessToken;
    private final LocalDateTime accessExpiresAt;
    private final String refreshToken;
    private final LocalDateTime refreshExpiresAt;
}
