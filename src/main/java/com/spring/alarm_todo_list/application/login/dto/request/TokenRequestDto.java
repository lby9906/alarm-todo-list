package com.spring.alarm_todo_list.application.login.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenRequestDto {

    @NotBlank(message="accessToken은 필수값입니다.")
    private String accessToken;

    @NotBlank(message="refresh 필수값입니다.")
    private String refreshToken;
}
