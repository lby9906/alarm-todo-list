package com.spring.alarm_todo_list.application.login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountLoginRequestDto {
    private String email;
    private String password;
}
