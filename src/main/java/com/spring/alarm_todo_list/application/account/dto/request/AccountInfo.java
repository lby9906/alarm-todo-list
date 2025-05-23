package com.spring.alarm_todo_list.application.account.dto.request;

import com.spring.alarm_todo_list.domain.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountInfo {

    private Long id;
    private String email;
    private String nickName;

    public static AccountInfo from(Account account) {
        return new AccountInfo(account.getId(), account.getEmail(), account.getNickName());
    }
}