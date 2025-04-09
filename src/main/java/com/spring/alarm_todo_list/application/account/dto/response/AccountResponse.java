package com.spring.alarm_todo_list.application.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountResponse {
    private Long accountId;
    private String email;
    private String nickName;
}
