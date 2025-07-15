package com.spring.alarm_todo_list.application.board.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardSearchRequest {
    private String boardTitle;
    private LocalDate boardDate;
    private String boardContent;
    private long page;
    private long size;

}
