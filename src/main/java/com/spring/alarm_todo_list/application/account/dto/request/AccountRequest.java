package com.spring.alarm_todo_list.application.account.dto.request;

import lombok.Getter;

@Getter
public class AccountRequest {
    private String email;
    private String nickName;
    private String phoneNumber;
    private String password;
}
