package com.spring.alarm_todo_list.application.login.dto;

import com.spring.alarm_todo_list.domain.account.enums.LoginType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfoDto {
    private Long accountId;
    private String email;
    private String nickName;
    private String password;
    private LoginType loginType;
}
