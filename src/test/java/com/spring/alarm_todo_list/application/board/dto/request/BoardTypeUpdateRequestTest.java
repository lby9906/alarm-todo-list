package com.spring.alarm_todo_list.application.board.dto.request;

import com.spring.alarm_todo_list.domain.board.enums.BoardType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BoardTypeUpdateRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("Todo 상태 값 변경 시 상태 값이 null이면 예외가 발생한다.")
    public void occurBoardTypeUpdateNUllSuccessException() {
        //given
        BoardType boardType = null;

        BoardTypeUpdateRequest boardTypeUpdateRequest = new BoardTypeUpdateRequest(boardType);

        //when
        Set<ConstraintViolation<BoardTypeUpdateRequest>> violations = validator.validate(boardTypeUpdateRequest);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("boardType은 필수 값 입니다.")
                .hasSize(1);
    }

    @Test
    @DisplayName("Todo 상태 값 변경 시 상태 값이 TODO 이면 예외가 발생하지 않는다.")
    public void notOccurBoardTypeTodoSuccessException() {
        //given
        BoardType boardType = BoardType.TODO;

        BoardTypeUpdateRequest boardTypeUpdateRequest = new BoardTypeUpdateRequest(boardType);

        //when
        Set<ConstraintViolation<BoardTypeUpdateRequest>> violations = validator.validate(boardTypeUpdateRequest);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains()
                .hasSize(0);
    }

    @Test
    @DisplayName("Todo 상태 값 변경 시 상태 값이 DONE 이면 예외가 발생하지 않는다.")
    public void notOccurBoardTypeDoneSuccessException() {
        //given
        BoardType boardType = BoardType.DONE;

        BoardTypeUpdateRequest boardTypeUpdateRequest = new BoardTypeUpdateRequest(boardType);

        //when
        Set<ConstraintViolation<BoardTypeUpdateRequest>> violations = validator.validate(boardTypeUpdateRequest);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains()
                .hasSize(0);
    }
}