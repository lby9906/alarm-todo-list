package com.spring.alarm_todo_list.domain.board;


import com.spring.alarm_todo_list.application.schedule.dto.request.BoardResult;
import com.spring.alarm_todo_list.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query(value = """
    SELECT b.id AS boardId,
           b.title AS title,
           b.content AS content,
           b.board_date AS boardDate,
           b.board_time AS boardTime,
           b.board_type AS boardType,
           a.phone_number AS phoneNumber
    FROM board b
    INNER JOIN account a ON b.account_id = a.id
    WHERE :nowDate = DATE_FORMAT(b.created_At, '%Y-%m-%d')
          AND :nowTime >= DATE_FORMAT(b.created_At, '%H-%i-%s')
    """, nativeQuery = true)
    List<BoardResult> findAllByCreatedAtEqualDateAndLessThanTime(@Param("nowDate")LocalDate nowDate, @Param("nowTime") LocalTime nowTime);
}
