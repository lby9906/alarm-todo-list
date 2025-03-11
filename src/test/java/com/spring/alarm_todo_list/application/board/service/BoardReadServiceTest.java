package com.spring.alarm_todo_list.application.board.service;

import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.account.repository.AccountRepository;
import com.spring.alarm_todo_list.domain.board.entity.Board;
import com.spring.alarm_todo_list.domain.board.enums.BoardType;
import com.spring.alarm_todo_list.domain.board.repository.BoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardReadServiceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardReadService boardReadService;

    @BeforeEach
    public void clear() {
        boardRepository.deleteAllInBatch();
        accountRepository.deleteAllInBatch();
    }

    private Account createAccount() {
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000");
        return accountRepository.save(account);
    }

    private Board createBoard(String title, String content, LocalDate boardDate, LocalTime boardTime, BoardType boardType, Account account) {
        Board board = Board.of(title, content, boardDate, boardTime, boardType, account);
        return boardRepository.save(board);
    }

    @Test
    @Transactional
    @DisplayName("회원id가 등록한 todolist를 현재 날짜에 맞게 전체 조회한다.")
    public void findAll() {
        //given
        Account savedAccount = createAccount();

        createBoard(
                "test제목",
                "test내용",
                LocalDate.of(2025, 03, 11),
                LocalTime.of(23, 11),
                BoardType.TODO,
                savedAccount);
        createBoard(
                "test제목!!",
                "test내용!!",
                LocalDate.of(2025, 03, 11),
                LocalTime.of(20, 00),
                BoardType.TODO,
                savedAccount);

        //when
        boardReadService.findAll(savedAccount.getId());

        //then
        List<Board> findAllBoard = boardRepository.findAllBoardAndAccountId(savedAccount.getId(), LocalDate.now());

        assertThat(findAllBoard).hasSize(2);

        assertThat(findAllBoard.get(0).getTitle()).isEqualTo("test제목");
        assertThat(findAllBoard.get(0).getContent()).isEqualTo("test내용");
        assertThat(findAllBoard.get(0).getBoardDate()).isEqualTo(LocalDate.of(2025,03,11));
        assertThat(findAllBoard.get(0).getBoardTime()).isEqualTo(LocalTime.of(23,11));
        assertThat(findAllBoard.get(0).getBoardType()).isEqualTo(BoardType.TODO);

        assertThat(findAllBoard.get(1).getTitle()).isEqualTo("test제목!!");
        assertThat(findAllBoard.get(1).getContent()).isEqualTo("test내용!!");
        assertThat(findAllBoard.get(1).getBoardDate()).isEqualTo(LocalDate.of(2025,03,11));
        assertThat(findAllBoard.get(1).getBoardTime()).isEqualTo(LocalTime.of(20,00));
        assertThat(findAllBoard.get(1).getBoardType()).isEqualTo(BoardType.TODO);
    }
}