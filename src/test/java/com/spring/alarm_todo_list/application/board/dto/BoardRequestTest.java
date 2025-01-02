package com.spring.alarm_todo_list.application.board.dto;

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

class BoardRequestTest {

    private Validator validator;


    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("제목을 입력하지 않는 경우 예외가 발생한다.")
    public void occurTitleBlankException() {
        //given
        String title = null;
        String content = "1-2범위 공부하기";
        LocalDate boardDate = LocalDate.parse("2025-01-01");
        LocalTime boardTime = LocalTime.parse("16:00");

        BoardRequest request = new BoardRequest(title, content, boardDate, boardTime);

        //when
        Set<ConstraintViolation<BoardRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("제목은 필수값입니다.")
                .hasSize(1);
    }

    @Test
    @DisplayName("제목이 비어있을 경우 예외가 발생한다.")
    public void occurTitleEmptyException() {
        //given
        String title = "";
        String content = "1-2범위 공부하기";
        LocalDate boardDate = LocalDate.parse("2025-01-01");
        LocalTime boardTime = LocalTime.parse("16:00");

        BoardRequest request = new BoardRequest(title, content, boardDate, boardTime);

        //when
        Set<ConstraintViolation<BoardRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("제목은 필수값입니다.")
                .hasSize(1);
    }

    @Test
    @DisplayName("제목을 입력하는 경우 예외가 발생하지 않는다.")
    public void notOccurTitleSuccessException() {
        //given
        String title = "00공부하기";
        String content = "1-2범위 공부하기";
        LocalDate boardDate = LocalDate.parse("2025-01-01");
        LocalTime boardTime = LocalTime.parse("16:00");

        BoardRequest request = new BoardRequest(title, content, boardDate, boardTime);

        //when
        Set<ConstraintViolation<BoardRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains()
                .hasSize(0);
    }

    @Test
    @DisplayName("날짜를 입력하지 않는 경우 예외가 발생한다.")
    public void occurDateNullException() {
        //given
        String title = "00공부하기";
        String content = "1-2범위 공부하기";
        LocalDate boardDate = null;
        LocalTime boardTime = LocalTime.parse("16:00");

        BoardRequest request = new BoardRequest(title, content, boardDate, boardTime);

        //when
        Set<ConstraintViolation<BoardRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("날짜 입력은 필수값입니다.")
                .hasSize(1);
    }

    @Test
    @DisplayName("날짜를 입력하는 경우 예외가 발생하지 않는다.")
    public void notOccurDateSuccessException() {
        //given
        String title = "00공부하기";
        String content = "1-2범위 공부하기";
        LocalDate boardDate = LocalDate.parse("2025-01-01");
        LocalTime boardTime = LocalTime.parse("16:00");

        BoardRequest request = new BoardRequest(title, content, boardDate, boardTime);

        //when
        Set<ConstraintViolation<BoardRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains()
                .hasSize(0);
    }

    @Test
    @DisplayName("시간을 입력하지 않는 경우 예외가 발생한다.")
    public void occurTimeNullException() {
        //given
        String title = "00공부하기";
        String content = "1-2범위 공부하기";
        LocalDate boardDate = LocalDate.parse("2025-01-01");
        LocalTime boardTime = null;

        BoardRequest request = new BoardRequest(title, content, boardDate, boardTime);

        //when
        Set<ConstraintViolation<BoardRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("시간 입력은 필수값입니다.")
                .hasSize(1);
    }

    @Test
    @DisplayName("시간을 입력하는 경우 예외가 발생하지 않는다.")
    public void notOccurTimeSuccessException() {
        //given
        String title = "00공부하기";
        String content = "1-2범위 공부하기";
        LocalDate boardDate = LocalDate.parse("2025-01-01");
        LocalTime boardTime = LocalTime.parse("16:00");

        BoardRequest request = new BoardRequest(title, content, boardDate, boardTime);

        //when
        Set<ConstraintViolation<BoardRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains()
                .hasSize(0);
    }

}