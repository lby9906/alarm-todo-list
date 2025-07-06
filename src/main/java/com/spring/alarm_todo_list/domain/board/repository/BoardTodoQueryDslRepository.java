package com.spring.alarm_todo_list.domain.board.repository;

import com.spring.alarm_todo_list.application.board.dto.request.BoardSearchRequest;
import com.spring.alarm_todo_list.domain.board.entity.Board;

import java.time.LocalDate;
import java.util.List;

public interface BoardTodoQueryDslRepository {
    List<Board> findAllByBoardDateAndAccountId(Long accountId, LocalDate localDate);

    List<Board> findAllByCondition(BoardSearchRequest boardSearchRequest, Long accountId);

    Long countByBoard(BoardSearchRequest boardSearchRequest);
}
