package com.spring.alarm_todo_list.application.board.service;

import com.spring.alarm_todo_list.application.board.dto.request.BoardRequest;
import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.account.enums.LoginType;
import com.spring.alarm_todo_list.domain.account.enums.Role;
import com.spring.alarm_todo_list.domain.account.repository.AccountRepository;
import com.spring.alarm_todo_list.domain.board.repository.BoardRepository;
import com.spring.alarm_todo_list.domain.board.entity.Board;
import com.spring.alarm_todo_list.exception.AlarmTodoListException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardWriteServiceTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardWriteService boardWriteService;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void clear() {
        boardRepository.deleteAllInBatch();
        accountRepository.deleteAllInBatch();
    }

    private Account createAccount() {
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000", LoginType.BASIC, Role.USER);
        return accountRepository.save(account);
    }

    @Test
    @Transactional
    @DisplayName("존재하는 회원이 제목,내용,날짜,시간을 입력하여 일정을 생성한다.")
    public void existAccountCreateTodo() {
        //given
        Account savedAccount = createAccount();

        String title = "00공부";
        String content = "1-2범위 공부하기";
        LocalDate boardDate = LocalDate.parse("2025-01-01");
        LocalTime boardTime = LocalTime.parse("16:00");

        BoardRequest boardRequest = new BoardRequest(title, content, boardDate, boardTime);

        //when
        boardWriteService.create(savedAccount.getId(), boardRequest);


        //then
        Board board = boardRepository.findAll().stream().findFirst().orElseThrow();
        Account account = board.getAccount();
        assertThat(savedAccount.getId()).isEqualTo(account.getId());
        assertThat(savedAccount.getPassword()).isEqualTo(account.getPassword());
        assertThat(board.getTitle()).isEqualTo(title);
        assertThat(board.getContent()).isEqualTo(content);
        assertThat(board.getBoardDate()).isEqualTo(boardDate);
        assertThat(board.getBoardTime()).isEqualTo(boardTime);
    }

    @Test
    @DisplayName("존재하지 않은 회원이 제목,내용,날짜,시간을 입력하여 일정 생성 시 예외가 발생한다.")
    public void notExistAccountCreateTodoException() {
        //given
        String title = "00공부";
        String content = "1-2범위 공부하기";
        LocalDate boardDate = LocalDate.parse("2025-01-01");
        LocalTime boardTime = LocalTime.parse("16:00");

        BoardRequest boardRequest = new BoardRequest(title, content, boardDate, boardTime);

        //when
        AlarmTodoListException exception = assertThrows(AlarmTodoListException.class,
                () -> boardWriteService.create(-1L, boardRequest));

        //then
        assertThat(exception).isInstanceOf(AlarmTodoListException.class);
        assertThat(exception.getErrorCode().getMessage()).isEqualTo("회원을 찾을 수 없습니다.");
    }
}