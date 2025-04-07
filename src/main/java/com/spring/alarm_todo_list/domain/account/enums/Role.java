package com.spring.alarm_todo_list.domain.account.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    ROLE_USER("회원");
    private final String content;
}
