package com.spring.alarm_todo_list.application.board.dto.response;

import com.spring.alarm_todo_list.application.board.service.BoardReadService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardResponseTest {

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
        Account account = Account.of("test@test.com", "test", "010-3173-9762", "0000");
        return accountRepository.save(account);
    }

    private Board createBoard(String title, String content, LocalDate boardDate, LocalTime boardTime, BoardType boardType, Account account) {
        Board board = Board.of(title, content, boardDate, boardTime, boardType, account);
        return boardRepository.save(board);
    }

    @Test
    @Transactional
    @DisplayName("response의 닉네임과 bordList가 올바르게 반환된다.")
    public void validateBoardResponse() {
        //given
        Account savedAccount = createAccount();
        createBoard(
                "test제목",
                "test내용",
                LocalDate.of(2025, 03, 11),
                LocalTime.of(23, 11),
                BoardType.TODO,
                savedAccount);

        //when
        BoardResponse boardResponse = boardReadService.findAll(savedAccount.getId());

        //then
        assertThat(boardResponse.getNickName()).isEqualTo("test");
        assertThat(boardResponse.getBoardListResponseList()).hasSize(1);
        assertThat(boardResponse.getBoardListResponseList().get(0).getTitle()).isEqualTo("test제목");
        assertThat(boardResponse.getBoardListResponseList().get(0).getBoardId()).isEqualTo(1);
        assertThat(boardResponse.getBoardListResponseList().get(0).getBoardDate()).isEqualTo(LocalDate.of(2025,03,11));
        assertThat(boardResponse.getBoardListResponseList().get(0).getBoardTime()).isEqualTo(LocalTime.of(23,11));
        assertThat(boardResponse.getBoardListResponseList().get(0).getBoardType()).isEqualTo(BoardType.TODO);
    }
}