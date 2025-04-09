package com.spring.alarm_todo_list.application.login.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class JwtToken {
    private final String token;
    private final LocalDateTime expiresAt;
}