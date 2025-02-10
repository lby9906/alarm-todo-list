package com.spring.alarm_todo_list.application.schedule.dto.request;


import com.spring.alarm_todo_list.domain.sendMessageHistory.enums.SendMessageHistoryType;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ScheduleRequest {
    Long getSendMessageHistoryId();
    String getUserPhoneNumber();

    String getTitle();

    LocalDate getBoardDate();

    LocalTime getBoardTime();

    Long getBoardId();

    SendMessageHistoryType getSendHistoryType();
}
