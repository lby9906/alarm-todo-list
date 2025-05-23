package com.spring.alarm_todo_list.application.board.service;

import com.spring.alarm_todo_list.application.account.dto.request.AccountInfo;
import com.spring.alarm_todo_list.application.board.dto.request.BoardRequest;
import com.spring.alarm_todo_list.application.board.dto.request.BoardUpdateRequest;
import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.account.enums.LoginType;
import com.spring.alarm_todo_list.domain.account.enums.Role;
import com.spring.alarm_todo_list.domain.account.repository.AccountRepository;
import com.spring.alarm_todo_list.domain.board.enums.BoardType;
import com.spring.alarm_todo_list.domain.board.repository.BoardRepository;
import com.spring.alarm_todo_list.domain.board.entity.Board;
import com.spring.alarm_todo_list.exception.AlarmTodoListException;
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
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000", LoginType.BASIC, Role.ROLE_USER);
        return accountRepository.save(account);
    }

    private Board createBoard(Account account) {
        Board board = Board.of("00공부하기", "1-2범위 공부하기", LocalDate.of(2025, 01, 01), LocalTime.of(16,00),
                BoardType.TODO, account);
        return boardRepository.save(board);
    }

    @Test
    @Transactional
    @DisplayName("존재하는 회원이 제목,내용,날짜,시간을 입력하여 일정을 생성한다.")
    public void existAccountCreateTodo() {
        //given
        Account savedAccount = createAccount();
        AccountInfo accountInfo = AccountInfo.from(savedAccount);

        String title = "00공부";
        String content = "1-2범위 공부하기";
        LocalDate boardDate = LocalDate.parse("2025-01-01");
        LocalTime boardTime = LocalTime.parse("16:00");

        BoardRequest boardRequest = new BoardRequest(title, content, boardDate, boardTime);

        //when
        boardWriteService.create(accountInfo, boardRequest);


        //then
        Board board = boardRepository.findAll().stream().findFirst().orElseThrow();
        Account account = board.getAccount();
        assertThat(accountInfo.getId()).isEqualTo(account.getId());
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
                () -> boardWriteService.create(null, boardRequest));

        //then
        assertThat(exception).isInstanceOf(AlarmTodoListException.class);
        assertThat(exception.getErrorCode().getMessage()).isEqualTo("회원 정보를 찾을 수 없습니다.");
    }

    @Test

    @DisplayName("존재하는 회원이 등록한 일정을 수정 시 정상적으로 수정된다.")
    @Transactional
    public void existAccountUpdateTodo() {
        //given
        Account savedAccount = createAccount();
        AccountInfo accountInfo = AccountInfo.from(savedAccount);
        Board savedBoard = createBoard(savedAccount);

        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest("운동하기", "상체운동하기",
                LocalDate.of(2025,01,01), LocalTime.of(16,00));

        //when
        boardWriteService.update(accountInfo, savedBoard.getId(), boardUpdateRequest);

        //then
        Board board = boardRepository.findById(savedBoard.getId()).stream().findFirst().orElseThrow();
        Account account = board.getAccount();
        assertThat(accountInfo.getId()).isEqualTo(account.getId());
        assertThat(board.getTitle()).isEqualTo(boardUpdateRequest.getTitle());
        assertThat(board.getContent()).isEqualTo(boardUpdateRequest.getContent());
        assertThat(board.getBoardDate()).isEqualTo(boardUpdateRequest.getBoardDate());
        assertThat(board.getBoardTime()).isEqualTo(boardUpdateRequest.getBoardTime());
    }

    @Test
    @DisplayName("존재하는 회원이 권한 없는 일정을 수정 시 예외가 발생한다.")
    public void notExistBoardUpdateTodoException() {
        //given
        Account savedAccount = createAccount();
        AccountInfo accountInfo = AccountInfo.from(savedAccount);
        Board savedBoard = createBoard(savedAccount);

        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest("운동하기", "상체운동하기",
                LocalDate.of(2025,01,01), LocalTime.of(16,00));

        //when
        AlarmTodoListException exception = assertThrows(AlarmTodoListException.class,
                () -> boardWriteService.update(accountInfo, -1L, boardUpdateRequest));

        //then
        assertThat(exception).isInstanceOf(AlarmTodoListException.class);
        assertThat(exception.getErrorCode().getMessage()).isEqualTo("회원이 등록한 일정을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("존재하는 회원이 자신이 등록한 Todo 제목을 수정 시 정상적으로 수정된다.")
    @Transactional
    public void existAccountUpdateTodoTitle() {
        //given
        Account savedAccount = createAccount();
        AccountInfo accountInfo = AccountInfo.from(savedAccount);
        Board savedBoard = createBoard(savedAccount);

        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest("운동하기", savedBoard.getContent(),
                savedBoard.getBoardDate(), savedBoard.getBoardTime());

        //when
        boardWriteService.update(accountInfo, savedBoard.getId(), boardUpdateRequest);

        //then
        Board board = boardRepository.findById(savedBoard.getId()).stream().findFirst().orElseThrow();
        Account account = board.getAccount();
        assertThat(accountInfo.getId()).isEqualTo(account.getId());
        assertThat(board.getTitle()).isEqualTo(boardUpdateRequest.getTitle());
    }

    @Test
    @DisplayName("존재하는 회원이 자신이 등록한 Todo 내용을 수정 시 정상적으로 수정된다.")
    @Transactional
    public void existAccountUpdateTodoContent() {
        //given
        Account savedAccount = createAccount();
        AccountInfo accountInfo = AccountInfo.from(savedAccount);
        Board savedBoard = createBoard(savedAccount);

        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(savedBoard.getTitle(), "상체운동하기",
                savedBoard.getBoardDate(), savedBoard.getBoardTime());

        //when
        boardWriteService.update(accountInfo, savedBoard.getId(), boardUpdateRequest);

        //then
        Board board = boardRepository.findById(savedBoard.getId()).stream().findFirst().orElseThrow();
        Account account = board.getAccount();
        assertThat(accountInfo.getId()).isEqualTo(account.getId());
        assertThat(board.getContent()).isEqualTo(boardUpdateRequest.getContent());
    }

    @Test
    @DisplayName("존재하는 회원이 자신이 등록한 Todo 날짜를 수정 시 정상적으로 수정된다.")
    @Transactional
    public void existAccountUpdateTodoBoardDate() {
        //given
        Account savedAccount = createAccount();
        AccountInfo accountInfo = AccountInfo.from(savedAccount);
        Board savedBoard = createBoard(savedAccount);

        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(savedBoard.getTitle(), savedBoard.getContent(),
                LocalDate.of(2025,01,02), savedBoard.getBoardTime());

        //when
        boardWriteService.update(accountInfo, savedBoard.getId(), boardUpdateRequest);

        //then
        Board board = boardRepository.findById(savedBoard.getId()).stream().findFirst().orElseThrow();
        Account account = board.getAccount();
        assertThat(accountInfo.getId()).isEqualTo(account.getId());
        assertThat(board.getBoardDate()).isEqualTo(boardUpdateRequest.getBoardDate());
    }

    @Test
    @DisplayName("존재하는 회원이 자신이 등록한 Todo 시간을 수정 시 정상적으로 수정된다.")
    @Transactional
    public void existAccountUpdateTodoBoardTime() {
        //given
        Account savedAccount = createAccount();
        AccountInfo accountInfo = AccountInfo.from(savedAccount);
        Board savedBoard = createBoard(savedAccount);

        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(savedBoard.getTitle(), savedBoard.getContent(),
                savedBoard.getBoardDate(), LocalTime.of(10,00));

        //when
        boardWriteService.update(accountInfo, savedBoard.getId(), boardUpdateRequest);

        //then
        Board board = boardRepository.findById(savedBoard.getId()).stream().findFirst().orElseThrow();
        Account account = board.getAccount();
        assertThat(accountInfo.getId()).isEqualTo(account.getId());
        assertThat(board.getBoardTime()).isEqualTo(boardUpdateRequest.getBoardTime());
    }
}