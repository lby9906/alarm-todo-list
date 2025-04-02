package com.spring.alarm_todo_list.domain.board.entity;

import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.account.enums.LoginType;
import com.spring.alarm_todo_list.domain.account.enums.Role;
import com.spring.alarm_todo_list.domain.account.repository.AccountRepository;
import com.spring.alarm_todo_list.domain.board.enums.BoardType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    @DisplayName("제목,내용,날짜,시간을 입력하여 일정을 생성한다.")
    public void createTodo() {
        //given
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000", LoginType.BASIC, Role.USER);

        String title = "00공부";
        String content = "1-2범위 공부하기";
        LocalDate boardDate = LocalDate.parse("2025-01-01");
        LocalTime boardTime = LocalTime.parse("16:00");
        BoardType boardType = BoardType.TODO;

        //when
        Board board = Board.of(title, content, boardDate, boardTime, boardType, account);

        //then
        assertThat(board.getTitle()).isEqualTo(title);
        assertThat(board.getContent()).isEqualTo(content);
        assertThat(board.getBoardDate()).isEqualTo(boardDate);
        assertThat(board.getBoardTime()).isEqualTo(boardTime);
        assertThat(board.getBoardType()).isEqualTo(boardType);
    }

}