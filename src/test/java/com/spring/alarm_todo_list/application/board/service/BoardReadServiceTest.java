package com.spring.alarm_todo_list.application.board.service;

import com.spring.alarm_todo_list.application.account.dto.request.AccountInfo;
import com.spring.alarm_todo_list.application.board.dto.request.BoardSearchRequest;
import com.spring.alarm_todo_list.application.board.dto.response.BoardSearchListResponse;
import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.account.enums.LoginType;
import com.spring.alarm_todo_list.domain.account.enums.Role;
import com.spring.alarm_todo_list.domain.account.repository.AccountRepository;
import com.spring.alarm_todo_list.domain.board.entity.Board;
import com.spring.alarm_todo_list.domain.board.enums.BoardType;
import com.spring.alarm_todo_list.domain.board.repository.BoardRepository;
import com.spring.alarm_todo_list.util.PaginationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static reactor.core.publisher.Mono.when;

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
        Account account = Account.of("test@test.com", "test", "010-1234-1234", "0000", LoginType.BASIC, Role.ROLE_USER);
        return accountRepository.save(account);
    }

    private Board createBoard(String title, String content, LocalDate boardDate, LocalTime boardTime, BoardType boardType, Account account) {
        Board board = Board.of(title, content, boardDate, boardTime, boardType, account);
        return boardRepository.save(board);
    }

    @Test
    @Transactional
    @DisplayName("회원id가 등록한 todolist를 선택 날짜에 맞게 전체 조회한다.")
    public void findAll() {
        //given
        Account savedAccount = createAccount();
        AccountInfo accountInfo = AccountInfo.from(savedAccount);

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

        BoardSearchRequest boardSearchRequest = new BoardSearchRequest(null, LocalDate.of(2025,03,11), null, 1, 2);

        //when
        boardReadService.findAll(accountInfo, boardSearchRequest);

        //then
        List<Board> findAllBoard = boardRepository.findAllByBoardDateAndAccountId(savedAccount.getId(), LocalDate.of(2025, 3, 11));

        assertThat(findAllBoard).hasSize(2);

        assertThat(findAllBoard).extracting(
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
    @DisplayName("회원이 등록한 todo를 제목, 날짜, 내용으로 검색하면 검색한 todo가 성공적으로 조회된다.")
    public void findSearch() {
        //given
        Account savedAccount = createAccount();
        AccountInfo accountInfo = AccountInfo.from(savedAccount);

        Board board = createBoard(
                "test제목",
                "test내용",
                LocalDate.of(2025, 05, 30),
                LocalTime.of(23, 11),
                BoardType.TODO,
                savedAccount);
        BoardSearchRequest boardSearchRequest = BoardSearchRequest.from(board, 1, 10);

        //when
        boardReadService.findAll(accountInfo, boardSearchRequest);

        //then
        List<Board> search = boardRepository.findAllSearchByBoardTitleAndBoardDateAndBoardContentAndAccountId(
                boardSearchRequest, accountInfo.getId());

        assertThat(search).hasSize(1);
        assertThat(search).extracting("title", "content", "boardDate")
                .containsExactly(tuple("test제목", "test내용", LocalDate.of(2025, 05, 30)));
    }

    @Test
    @DisplayName("제목에 '둥이'를 포함한 Todo 검색 결과를 조회한다.")
    public void searchTitleContainsSuccess() {
        //given
        Account savedAccount = createAccount();
        AccountInfo accountInfo = AccountInfo.from(savedAccount);

        createBoard("둥이는 말티즈", "ch01",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);
        createBoard("안녕 둥이야!", "ch01-ch02",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);
        createBoard("둥이는 강아지", "ch01-ch02",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);
        createBoard("강아지인 둥이의 종류는 무엇일까?", "ch01-ch02",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);

        BoardSearchRequest boardSearchRequest = new BoardSearchRequest("둥이", null, null, 1, 10);

        //when
        PaginationResponse<BoardSearchListResponse> search = boardReadService.findAll(accountInfo, boardSearchRequest);

        //then
        assertThat(search.getContents()).hasSize(4);
        assertThat(search.getContents())
                .extracting(BoardSearchListResponse::getTitle)
                .allMatch(title -> title.contains("둥이"));
    }

    @Test
    @DisplayName("제목에 '둥이'를 포함한 Todo 검색 결과가 없을 경우 빈 리스트를 반환한다.")
    public void searchTitleContainsEmpty() {
        //given
        Account savedAccount = createAccount();
        AccountInfo accountInfo = AccountInfo.from(savedAccount);

        createBoard("말티즈", "ch01",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);
        createBoard("안녕 강아지야!", "ch01-ch02",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);
        createBoard("강아지", "ch01-ch02",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);
        createBoard("강아지의 종류는 무엇이 있을까?", "ch01-ch02",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);

        BoardSearchRequest boardSearchRequest = new BoardSearchRequest("둥이", null, null, 1, 10);

        //when
        PaginationResponse<BoardSearchListResponse> search = boardReadService.findAll(accountInfo, boardSearchRequest);

        //then
        assertThat(search.getContents()).isEmpty();
    }

    @Test
    @DisplayName("내용에 '둥이'를 포함한 Todo 검색 결과를 조회한다.")
    public void searchContentContainsSuccess() {
        //given
        Account savedAccount = createAccount();
        AccountInfo accountInfo = AccountInfo.from(savedAccount);

        createBoard("말티즈", "ch01-둥이에 대하여",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);
        createBoard("안녕 둥이야!", "ch01-ch02 - 안녕 둥이야",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);
        createBoard("둥이는 강아지", "ch01-ch02",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);
        createBoard("강아지인 둥이의 종류는 무엇일까?", "둥이 관련 ch01 듣기",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);

        BoardSearchRequest boardSearchRequest = new BoardSearchRequest(null, null, "둥이", 1, 10);

        //when
        PaginationResponse<BoardSearchListResponse> search = boardReadService.findAll(accountInfo, boardSearchRequest);

        //then
        assertThat(search.getContents()).hasSize(3);
        assertThat(search.getContents())
                .extracting(BoardSearchListResponse::getContent)
                .allMatch(content -> content.contains("둥이"));
    }

    @Test
    @DisplayName("내용에 '둥이'를 포함한 Todo 검색 결과가 없을 경우 빈 리스트를 반환한다.")
    public void searchContentContainsEmpty() {
        //given
        Account savedAccount = createAccount();
        AccountInfo accountInfo = AccountInfo.from(savedAccount);

        createBoard("말티즈", "ch01",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);
        createBoard("안녕 강아지야!", "ch01-ch02",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);
        createBoard("강아지", "ch01-ch02",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);
        createBoard("강아지의 종류는 무엇이 있을까?", "ch01-ch02",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);

        BoardSearchRequest boardSearchRequest = new BoardSearchRequest(null, null, "둥이", 1, 10);

        //when
        PaginationResponse<BoardSearchListResponse> search = boardReadService.findAll(accountInfo, boardSearchRequest);

        //then
        assertThat(search.getContents()).isEmpty();
    }

    @Test
    @DisplayName("사용자가 검색한 날짜를 포함한 Todo 검색 결과를 조회한다.")
    public void searchBoardDateContainsSuccess() {
        //given
        Account savedAccount = createAccount();
        AccountInfo accountInfo = AccountInfo.from(savedAccount);

        createBoard("말티즈", "ch01-둥이에 대하여",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);
        createBoard("안녕 둥이야!", "ch01-ch02 - 안녕 둥이야",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);
        createBoard("둥이는 강아지", "ch01-ch02",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);
        createBoard("강아지인 둥이의 종류는 무엇일까?", "둥이 관련 ch01 듣기",
                LocalDate.of(2025, 06, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);

        BoardSearchRequest boardSearchRequest = new BoardSearchRequest(null, LocalDate.of(2025,06,13), null, 1, 10);

        //when
        PaginationResponse<BoardSearchListResponse> search = boardReadService.findAll(accountInfo, boardSearchRequest);

        //then
        assertThat(search.getContents()).hasSize(4);
        assertThat(search.getContents())
                .extracting(BoardSearchListResponse::getBoardDate)
                .allMatch(date -> date.toString().contains("2025-06-13"));
    }

    @Test
    @DisplayName("사용자가 검색한 날짜를 포함한 Todo 검색 결과가 없을 경우 빈 리스트를 반환한다.")
    public void searchBoardDateContainsEmpty() {
        //given
        Account savedAccount = createAccount();
        AccountInfo accountInfo = AccountInfo.from(savedAccount);

        createBoard("말티즈", "ch01",
                LocalDate.of(2025, 05, 13), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);
        createBoard("안녕 강아지야!", "ch01-ch02",
                LocalDate.of(2025, 06, 15), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);
        createBoard("강아지", "ch01-ch02",
                LocalDate.of(2025, 06, 14), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);
        createBoard("강아지의 종류는 무엇이 있을까?", "ch01-ch02",
                LocalDate.of(2025, 06, 10), LocalTime.of(10, 00),
                BoardType.TODO, savedAccount);

        BoardSearchRequest boardSearchRequest = new BoardSearchRequest(null, LocalDate.of(2025,06,13), null, 1, 10);

        //when
        PaginationResponse<BoardSearchListResponse> search = boardReadService.findAll(accountInfo, boardSearchRequest);

        //then
        assertThat(search.getContents()).isEmpty();
    }
}