package com.spring.alarm_todo_list.application.board.service;

import com.spring.alarm_todo_list.application.account.dto.request.AccountInfo;
import com.spring.alarm_todo_list.application.board.dto.request.BoardSearchRequest;
import com.spring.alarm_todo_list.application.board.dto.response.BoardSearchListResponse;
import com.spring.alarm_todo_list.domain.board.repository.BoardRepository;
import com.spring.alarm_todo_list.domain.board.entity.Board;
import com.spring.alarm_todo_list.exception.AlarmTodoListException;
import com.spring.alarm_todo_list.exception.ErrorCode;
import com.spring.alarm_todo_list.util.PaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardReadService {

    private final BoardRepository boardRepository;

    public PaginationResponse<BoardSearchListResponse> findAll(AccountInfo accountInfo, BoardSearchRequest boardSearchRequest) {

        if (boardSearchRequest.getSize() <= 0) {
            throw new AlarmTodoListException(ErrorCode.INVALID_PAGE_SIZE);
        }

        LocalDate boardDate = boardSearchRequest.getBoardDate();

        if (boardDate == null) {
            boardDate = LocalDate.now();
        }

        List<Board> boardSearchList = boardRepository.findAllByCondition(
                boardSearchRequest, accountInfo.getId());

        long totalElements = boardRepository.countByBoard(boardSearchRequest);

        return PaginationResponse.of(boardSearchList.stream().map(BoardSearchListResponse::of).toList(),
                boardSearchRequest.getPage(), boardSearchRequest.getSize(), totalElements);
    }
}
