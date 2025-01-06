package com.spring.alarm_todo_list.application.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class BoardRequest {

    @NotBlank(message="제목은 필수값입니다.")
    @Column(length = 500)
    @Size(min = 3, max = 500, message = "제목은 최소 3자에서 최대 500자까지 입력할 수 있습니다.")
    private String title;

    @Column(length = 500)
    @Size(min = 3, max = 500, message = "내용은 최소 3자에서 최대 500자까지 입력할 수 있습니다.")
    private String content;

    @NotNull(message="날짜 입력은 필수값입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate boardDate;

    @NotNull(message="시간 입력은 필수값입니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime boardTime;
}
