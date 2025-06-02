package com.spring.alarm_todo_list.domain.board.repository;

import com.spring.alarm_todo_list.domain.board.entity.Board;

import java.time.LocalDate;
import java.util.List;

public interface BoardTodoQueryDslRepository {
    List<Board> findAllByBoardDateAndAccountId(Long accountId, LocalDate localDate);

    List<Board> findSearchByBoardTitleAndBoardDateAndBoardContentAndAccountId(String boardTitle, LocalDate localDate, String boardContent, Long accountId);
}
