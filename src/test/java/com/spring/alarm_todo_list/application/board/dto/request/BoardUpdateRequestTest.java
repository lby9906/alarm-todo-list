package com.spring.alarm_todo_list.application.board.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BoardUpdateRequestTest {

    private Validator validator;


    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("일정 수정 시 제목의 길이가 3미만 시 예외가 발생한다.")
    public void titleLengthLessThan() {
        //given
        String title = "a";
        String content = "1-2범위 공부하기";
        LocalDate boardDate = LocalDate.parse("2025-01-01");
        LocalTime boardTime = LocalTime.parse("16:00");

        BoardUpdateRequest request = new BoardUpdateRequest(title, content, boardDate, boardTime);

        //when
        Set<ConstraintViolation<BoardUpdateRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("제목은 최소 3자에서 최대 500자까지 입력할 수 있습니다.")
                .hasSize(1);
    }

    @Test
    @DisplayName("일정 수정 시 제목의 길이가 500초과 시 예외가 발생한다.")
    public void titleLengthExceeded() {
        //given
        String title = "a".repeat(501);
        String content = "1-2범위 공부하기";
        LocalDate boardDate = LocalDate.parse("2025-01-01");
        LocalTime boardTime = LocalTime.parse("16:00");

        BoardUpdateRequest request = new BoardUpdateRequest(title, content, boardDate, boardTime);

        //when
        Set<ConstraintViolation<BoardUpdateRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("제목은 최소 3자에서 최대 500자까지 입력할 수 있습니다.");
    }

    @Test
    @DisplayName("일정 수정 시 제목의 길이가 3이상이고 500미만일 시 예외가 발생하지 않는다.")
    public void notOccurTitleLengthSuccessException() {
        //given
        String title = "00공부하기";
        String content = "1-2범위 공부하기";
        LocalDate boardDate = LocalDate.parse("2025-01-01");
        LocalTime boardTime = LocalTime.parse("16:00");

        BoardUpdateRequest request = new BoardUpdateRequest(title, content, boardDate, boardTime);

        //when
        Set<ConstraintViolation<BoardUpdateRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains()
                .hasSize(0);
    }

    @Test
    @DisplayName("일정 수정 시 내용의 길이가 3미만 시 예외가 발생한다.")
    public void contentLengthLessThan() {
        //given
        String title = "00공부하기";
        String content = "a";
        LocalDate boardDate = LocalDate.parse("2025-01-01");
        LocalTime boardTime = LocalTime.parse("16:00");

        BoardUpdateRequest request = new BoardUpdateRequest(title, content, boardDate, boardTime);

        //when
        Set<ConstraintViolation<BoardUpdateRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("내용은 최소 3자에서 최대 500자까지 입력할 수 있습니다.")
                .hasSize(1);
    }

    @Test
    @DisplayName("일정 수정 시 내용의 길이가 500초과 시 예외가 발생한다.")
    public void contentLengthExceeded() {
        //given
        String title = "00공부하기";
        String content = "a".repeat(501);
        LocalDate boardDate = LocalDate.parse("2025-01-01");
        LocalTime boardTime = LocalTime.parse("16:00");

        BoardUpdateRequest request = new BoardUpdateRequest(title, content, boardDate, boardTime);

        //when
        Set<ConstraintViolation<BoardUpdateRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("내용은 최소 3자에서 최대 500자까지 입력할 수 있습니다.")
                .hasSize(1);
    }

    @Test
    @DisplayName("일정 수정 시 내용의 길이가 3이상이고 500미만일 시 예외가 발생하지 않는다.")
    public void notOccurContentLengthSuccessException() {
        //given
        String title = "00공부하기";
        String content = "1-2범위 공부하기";
        LocalDate boardDate = LocalDate.parse("2025-01-01");
        LocalTime boardTime = LocalTime.parse("16:00");

        BoardUpdateRequest request = new BoardUpdateRequest(title, content, boardDate, boardTime);

        //when
        Set<ConstraintViolation<BoardUpdateRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains()
                .hasSize(0);
    }
}