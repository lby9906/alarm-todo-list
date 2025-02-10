package com.spring.alarm_todo_list.application.schedule.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public interface BoardResult {

    String getTitle();

    LocalDate getBoardDate();

    LocalTime getBoardTime();

    Long getBoardId();

    String getPhoneNumber();
}
