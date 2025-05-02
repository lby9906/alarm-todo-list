package com.spring.alarm_todo_list.domain.account.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginType {
    BASIC("일반");
    private final String content;
}
