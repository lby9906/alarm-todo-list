package com.spring.alarm_todo_list.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlarmTodoListException extends RuntimeException {
    private ErrorCode errorCode;
}

