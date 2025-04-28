package com.spring.alarm_todo_list.application.login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountLoginResponseDto {
    private Long id;
    private String nickName;
    private String accessToken;
    private LocalDateTime accessExpiresAt;
    private String refreshToken;
    private LocalDateTime refreshExpiresAt;
}
