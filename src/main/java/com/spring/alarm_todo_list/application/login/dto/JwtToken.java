package com.spring.alarm_todo_list.application.login.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class JwtToken {
    private final String accessToken;
    private final LocalDateTime accessExpiresAt;
    private final String refreshToken;
    private final LocalDateTime refreshExpiresAt;
}