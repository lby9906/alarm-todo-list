package com.spring.alarm_todo_list.domain.board.repository;

import com.spring.alarm_todo_list.QuerydslConfig;
import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.account.enums.LoginType;
import com.spring.alarm_todo_list.domain.account.enums.Role;
import com.spring.alarm_todo_list.domain.account.repository.AccountRepository;
import com.spring.alarm_todo_list.domain.board.entity.Board;
import com.spring.alarm_todo_list.domain.board.enums.BoardType;
import com.spring.alarm_todo_list.domain.board.repository.BoardRepository;
import com.spring.alarm_todo_list.exception.AlarmTodoListException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QuerydslConfig.class})
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void clear() {
        boardRepository.deleteAllInBatch();
    }

    private Account createAccount() {
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000", LoginType.BASIC, Role.ROLE_USER);
        return accountRepository.save(account);
    }

    private Board createBoard(Account account) {
        Board board = Board.of("00공부하기", "1-2범위 공부하기", LocalDate.of(2025, 01, 01), LocalTime.of(16,00),
                BoardType.TODO, account);
        return boardRepository.save(board);
    }

    @Test
    @DisplayName("일정을 정상적으로 저장한다.")
    public void saveTodoSuccess() {
        //given
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000", LoginType.BASIC, Role.ROLE_USER);

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

    @Test
    @DisplayName("존재하는 회원이 등록한 일정을 정상적으로 찾는다.")
    public void saveTodoFindSuccess() {
        //given
        Account savedAccount = createAccount();
        Board savedBoard = createBoard(savedAccount);

        //when
        Optional<Board> findBoard = boardRepository.findByIdAndAccountId(savedBoard.getId(), savedAccount.getId());

        //then
        assertThat(findBoard).isPresent();
        Board board = findBoard.get();
        assertThat(board.getId()).isEqualTo(savedBoard.getId());
        assertThat(board.getAccount().getId()).isEqualTo(savedAccount.getId());
    }
}