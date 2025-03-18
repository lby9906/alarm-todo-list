package com.spring.alarm_todo_list.application.board.service;

import com.spring.alarm_todo_list.application.board.dto.response.BoardListResponse;
import com.spring.alarm_todo_list.application.board.dto.response.BoardResponse;
import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.account.repository.AccountRepository;
import com.spring.alarm_todo_list.domain.board.enums.BoardType;
import com.spring.alarm_todo_list.domain.board.repository.BoardRepository;
import com.spring.alarm_todo_list.domain.board.entity.Board;
import com.spring.alarm_todo_list.exception.AlarmTodoListException;
import com.spring.alarm_todo_list.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardReadService {

    private final AccountRepository accountRepository;
    private final BoardRepository boardRepository;

    public BoardResponse findAll(Long accountId, LocalDate boardDate) {

        if (boardDate == null) {
            boardDate = LocalDate.now();
        }

        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AlarmTodoListException(ErrorCode.NOT_FOUND_ACCOUNT));
        List<Board> boardList = boardRepository.findAllBoardAndAccountId(account.getId(), boardDate);

        List<BoardListResponse> boardListResponses = boardList.stream()
                .map(list -> new BoardListResponse(list.getId(),
                        list.getTitle(), list.getBoardDate(),
                        list.getBoardTime(), list.getBoardType()))
                .collect(Collectors.toList());

        return new BoardResponse(account.getNickName(), boardListResponses);
    }
}
