package com.spring.alarm_todo_list.domain.sendMessageHistory.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SendMessageHistoryType {
    SEND_SUCCESS("발송 성공"), SEND_FAIL("발송 실패");

    private final String content;
}
