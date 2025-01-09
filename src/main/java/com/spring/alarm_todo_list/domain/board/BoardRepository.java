package com.spring.alarm_todo_list.domain.board;


import com.spring.alarm_todo_list.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
