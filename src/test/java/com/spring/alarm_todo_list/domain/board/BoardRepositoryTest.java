package com.spring.alarm_todo_list.domain.board;

import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.board.entity.Board;
import com.spring.alarm_todo_list.domain.board.enums.BoardType;
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
    @DisplayName("Todo 생성 후 id로 일정을 정상적으로 찾는다.")
    public void findByIdSuccess() {
        //given
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000");

        String title = "00공부";
        String content = "1-2범위 공부하기";
        LocalDate boardDate = LocalDate.parse("2025-01-01");
        LocalTime boardTime = LocalTime.parse("16:00");
        BoardType boardType = BoardType.TODO;

        Board board = Board.of(title, content, boardDate, boardTime, boardType, account);

        //when
        boardRepository.save(board);

        //then
        Optional<Board> find = boardRepository.findById(board.getId());
        assertThat(find).isPresent();
        assertThat(find.get().getTitle()).isEqualTo(title);
        assertThat(find.get().getContent()).isEqualTo(content);
        assertThat(find.get().getBoardDate()).isEqualTo(boardDate);
        assertThat(find.get().getBoardTime()).isEqualTo(boardTime);
        assertThat(find.get().getBoardType()).isEqualTo(boardType);
        assertThat(find.get().getAccount()).isEqualTo(account);
    }
}