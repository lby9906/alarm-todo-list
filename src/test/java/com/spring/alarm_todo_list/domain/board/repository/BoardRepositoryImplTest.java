package com.spring.alarm_todo_list.domain.board.repository;

import com.spring.alarm_todo_list.QuerydslConfig;
import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.account.enums.LoginType;
import com.spring.alarm_todo_list.domain.account.enums.Role;
import com.spring.alarm_todo_list.domain.account.repository.AccountRepository;
import com.spring.alarm_todo_list.domain.board.entity.Board;
import com.spring.alarm_todo_list.domain.board.enums.BoardType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
@Import({QuerydslConfig.class})
class BoardRepositoryImplTest {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardRepositoryImpl boardRepositoryImpl;

    @BeforeEach
    public void clear() {
        boardRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("회원 id에 맞는 Todolist를 정상적으로 조회한다.")
    public void findTodoSuccess() {
        //given
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000", LoginType.BASIC, Role.USER);
        Board board1 = Board.of(
                "test제목",
                "test내용",
                LocalDate.of(2025, 03, 11),
                LocalTime.of(23, 11),
                BoardType.TODO,
                account);
        Board board2 = Board.of(
                "test제목!!",
                "test내용!!",
                LocalDate.of(2025, 03, 11),
                LocalTime.of(20, 00),
                BoardType.TODO,
                account);

        accountRepository.save(account);
        boardRepository.save(board1);
        boardRepository.save(board2);

        //when
        List<Board> findAll = boardRepositoryImpl.findAllBoardAndAccountId(account.getId(), LocalDate.of(2025,03,11));

        //then
        assertThat(findAll).hasSize(2);
        assertThat(findAll).extracting(
                "title", "content", "boardDate", "boardTime", "boardType")
                .contains(tuple("test제목", "test내용",
                        LocalDate.of(2025,03,11),
                        LocalTime.of(23,11),
                        BoardType.TODO),

                        tuple("test제목!!", "test내용!!",
                        LocalDate.of(2025,03,11),
                        LocalTime.of(20,00),
                        BoardType.TODO));
    }

    @Test
    @DisplayName("오늘 날짜로 된 Todolist가 없는 경우 빈 리스트를 반환한다.")
    public void giveTodolistIsEmpty() {
        //given
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000", LoginType.BASIC, Role.USER);
        Board board1 = Board.of(
                "test제목",
                "test내용",
                LocalDate.of(2025, 03, 04),
                LocalTime.of(23, 11),
                BoardType.TODO,
                account);
        Board board2 = Board.of(
                "test제목!!",
                "test내용!!",
                LocalDate.of(2025, 03, 05),
                LocalTime.of(20, 00),
                BoardType.TODO,
                account);

        accountRepository.save(account);
        boardRepository.save(board1);
        boardRepository.save(board2);

        //when
        List<Board> findAll = boardRepositoryImpl.findAllBoardAndAccountId(account.getId(), LocalDate.of(2025,03,11));

        //then
        assertThat(findAll).isEmpty();
    }
}