package com.spring.alarm_todo_list.domain.board.repository;

import com.spring.alarm_todo_list.QuerydslConfig;
import com.spring.alarm_todo_list.application.board.dto.request.BoardSearchRequest;
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
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000", LoginType.BASIC, Role.ROLE_USER);
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
        List<Board> findAll = boardRepositoryImpl.findAllByBoardDateAndAccountId(account.getId(), LocalDate.of(2025,03,11));

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
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000", LoginType.BASIC, Role.ROLE_USER);
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
        List<Board> findAll = boardRepositoryImpl.findAllByBoardDateAndAccountId(account.getId(), LocalDate.of(2025,03,11));

        //then
        assertThat(findAll).isEmpty();
    }

    @Test
    @DisplayName("회원이 등록한 Todolist를 검색하면 검색한 회원의 Todo가 조회된다.")
    public void todoSearchSuccess() {
        //given
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000", LoginType.BASIC, Role.ROLE_USER);
        Board board = Board.of(
                "test제목",
                "test내용",
                LocalDate.of(2025, 03, 11),
                LocalTime.of(23, 11),
                BoardType.TODO,
                account);

        accountRepository.save(account);
        boardRepository.save(board);

        //when
        BoardSearchRequest boardSearchRequest = new BoardSearchRequest(board.getTitle(), board.getBoardDate(), board.getContent(), 1, 10);

        List<Board> search = boardRepositoryImpl.findAllSearchByBoardTitleAndBoardDateAndBoardContentAndAccountId(
                boardSearchRequest, account.getId());

        //then
        assertThat(search).hasSize(1);
        assertThat(search).extracting(Board::getAccount).extracting(Account::getId).containsExactly(board.getAccount().getId());
        assertThat(search).extracting(Board::getTitle).containsExactly(board.getTitle());
        assertThat(search).extracting(Board::getContent).containsExactly(board.getContent());
        assertThat(search).extracting(Board::getBoardDate).containsExactly(board.getBoardDate());
    }

    @Test
    @DisplayName("Todo 제목에 '둥이'를 포함한 Todo 검색하면 성공적으로 조회된다.")
    public void searchTodoTitleSuccess() {
        //given
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000", LoginType.BASIC, Role.ROLE_USER);
        accountRepository.save(account);

        Board board = Board.of(
                "둥이에 관하여",
                "둥이는 말티즈",
                LocalDate.of(2025, 03, 11),
                LocalTime.of(23, 11),
                BoardType.TODO,
                account);

        boardRepository.save(board);

        //when
        BoardSearchRequest boardSearchRequest = new BoardSearchRequest(board.getTitle(), board.getBoardDate(), board.getContent(), 1, 10);

        List<Board> search = boardRepositoryImpl.findAllSearchByBoardTitleAndBoardDateAndBoardContentAndAccountId(
                boardSearchRequest, account.getId());

        //then
        assertThat(search).hasSize(1);
        assertThat(search)
                .extracting(Board::getTitle)
                .allMatch(title -> title.contains("둥이"));
    }

    @Test
    @DisplayName("Todo 내용에 '말티즈'를 포함한 Todo 검색하면 성공적으로 조회된다.")
    public void searchTodoContentSuccess() {
        //given
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000", LoginType.BASIC, Role.ROLE_USER);
        accountRepository.save(account);

        Board board = Board.of(
                "둥이에 관하여",
                "둥이는 말티즈",
                LocalDate.of(2025, 03, 11),
                LocalTime.of(23, 11),
                BoardType.TODO,
                account);

        boardRepository.save(board);

        //when
        BoardSearchRequest boardSearchRequest = new BoardSearchRequest(board.getTitle(), board.getBoardDate(), board.getContent(), 1, 10);

        List<Board> search = boardRepositoryImpl.findAllSearchByBoardTitleAndBoardDateAndBoardContentAndAccountId(
                boardSearchRequest, account.getId());

        //then
        assertThat(search).hasSize(1);
        assertThat(search)
                .extracting(Board::getContent)
                .allMatch(content -> content.contains("말티즈"));
    }

    @Test
    @DisplayName("Todo 날짜에 '2025-03-11'을 포함한 Todo 검색하면 성공적으로 조회된다.")
    public void searchTodoBoardDateSuccess() {
        //given
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000", LoginType.BASIC, Role.ROLE_USER);
        accountRepository.save(account);

        Board board = Board.of(
                "둥이에 관하여",
                "둥이는 말티즈",
                LocalDate.of(2025, 03, 11),
                LocalTime.of(23, 11),
                BoardType.TODO,
                account);

        boardRepository.save(board);

        //when
        BoardSearchRequest boardSearchRequest = new BoardSearchRequest(board.getTitle(), board.getBoardDate(), board.getContent(), 1, 10);

        List<Board> search = boardRepositoryImpl.findAllSearchByBoardTitleAndBoardDateAndBoardContentAndAccountId(
                boardSearchRequest, account.getId());

        //then
        assertThat(search).hasSize(1);
        assertThat(search)
                .extracting(Board::getBoardDate)
                .allMatch(date -> date.equals(board.getBoardDate()));
    }

    @Test
    @DisplayName("todo 검색 시 제목에 '둥이'를 포함한 ToDolist가 없을 경우 빈 리스트를 반환한다.")
    public void isEmptySearchTitle() {
        //given
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000", LoginType.BASIC, Role.ROLE_USER);
        accountRepository.save(account);

        Board board = Board.of(
                "강아지에 관하여",
                "강아지는 말티즈",
                LocalDate.of(2025, 03, 11),
                LocalTime.of(23, 11),
                BoardType.TODO,
                account);

        boardRepository.save(board);

        //when
        BoardSearchRequest boardSearchRequest = new BoardSearchRequest("둥이", board.getBoardDate(), board.getContent(), 1, 10);

        List<Board> search = boardRepositoryImpl.findAllSearchByBoardTitleAndBoardDateAndBoardContentAndAccountId(
                boardSearchRequest, account.getId());

        //then
        assertThat(search).isEmpty();
    }

    @Test
    @DisplayName("todo 검색 시 내용에 '둥이'를 포함한 ToDolist가 없을 경우 빈 리스트를 반환한다.")
    public void isEmptySearchContent() {
        //given
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000", LoginType.BASIC, Role.ROLE_USER);
        accountRepository.save(account);

        Board board = Board.of(
                "강아지에 관하여",
                "강아지는 말티즈",
                LocalDate.of(2025, 03, 11),
                LocalTime.of(23, 11),
                BoardType.TODO,
                account);

        boardRepository.save(board);

        //when
        BoardSearchRequest boardSearchRequest = new BoardSearchRequest(board.getTitle(), board.getBoardDate(), "둥이", 1, 10);

        List<Board> search = boardRepositoryImpl.findAllSearchByBoardTitleAndBoardDateAndBoardContentAndAccountId(
                boardSearchRequest, account.getId());

        //then
        assertThat(search).isEmpty();
    }

    @Test
    @DisplayName("todo 검색 시 날짜에 '2025-05-30'를 포함한 ToDolist가 없을 경우 빈 리스트를 반환한다.")
    public void isEmptySearchBoardDate() {
        //given
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000", LoginType.BASIC, Role.ROLE_USER);
        accountRepository.save(account);

        Board board = Board.of(
                "강아지에 관하여",
                "강아지는 말티즈",
                LocalDate.of(2025, 03, 11),
                LocalTime.of(23, 11),
                BoardType.TODO,
                account);

        boardRepository.save(board);

        //when
        BoardSearchRequest boardSearchRequest = new BoardSearchRequest(board.getTitle(), LocalDate.of(2025,05,30), board.getContent(), 1, 10);

        List<Board> search = boardRepositoryImpl.findAllSearchByBoardTitleAndBoardDateAndBoardContentAndAccountId(
                boardSearchRequest, account.getId());

        //then
        assertThat(search).isEmpty();
    }
}