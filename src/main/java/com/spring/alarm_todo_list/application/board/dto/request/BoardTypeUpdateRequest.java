package com.spring.alarm_todo_list.application.board.dto.request;

import com.spring.alarm_todo_list.domain.board.enums.BoardType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardTypeUpdateRequest {

    @NotNull(message = "boardType은 필수 값 입니다.")
    private BoardType boardType;
}
