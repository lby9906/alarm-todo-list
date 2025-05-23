package com.spring.alarm_todo_list.application.board.service;

import com.spring.alarm_todo_list.application.account.dto.request.AccountInfo;
import com.spring.alarm_todo_list.application.board.dto.response.BoardListResponse;
import com.spring.alarm_todo_list.application.board.dto.response.BoardResponse;
import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.board.repository.BoardRepository;
import com.spring.alarm_todo_list.domain.board.entity.Board;
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

    private final BoardRepository boardRepository;

    public BoardResponse findAll(AccountInfo accountInfo, LocalDate boardDate) {

        if (boardDate == null) {
            boardDate = LocalDate.now();
        }
        List<Board> boardList = boardRepository.findAllBoardAndAccountId(accountInfo.getId(), boardDate);

        List<BoardListResponse> boardListResponses = boardList.stream()
                .map(list -> new BoardListResponse(list.getId(),
                        list.getTitle(), list.getBoardDate(),
                        list.getBoardTime(), list.getBoardType()))
                .collect(Collectors.toList());

        return new BoardResponse(accountInfo.getNickName(), boardListResponses);
    }
}
