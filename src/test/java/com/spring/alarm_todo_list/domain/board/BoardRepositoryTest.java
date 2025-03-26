package com.spring.alarm_todo_list.domain.board;

import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.board.entity.Board;
import com.spring.alarm_todo_list.domain.board.enums.BoardType;
import com.spring.alarm_todo_list.domain.board.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    public void clear() {
        boardRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("일정을 정상적으로 저장한다.")
    public void saveTodoSuccess() {
        //given
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000");

        String title = "00공부";
        String content = "1-2범위 공부하기";
        LocalDate boardDate = LocalDate.parse("2025-01-01");
        LocalTime boardTime = LocalTime.parse("16:00");
        BoardType boardType = BoardType.TODO;

        Board board = Board.of(title, content, boardDate, boardTime, boardType, account);

        //when
        Board savedBoard = boardRepository.save(board);

        //then
        assertThat(savedBoard).isNotNull();
        assertThat(savedBoard.getId()).isNotNull();
        assertThat(savedBoard.getTitle()).isEqualTo(title);
        assertThat(savedBoard.getContent()).isEqualTo(content);
        assertThat(savedBoard.getBoardDate()).isEqualTo(boardDate);
        assertThat(savedBoard.getBoardTime()).isEqualTo(boardTime);
        assertThat(savedBoard.getBoardType()).isEqualTo(boardType);
        assertThat(savedBoard.getAccount()).isEqualTo(account);
    }
}