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
    select b.id as boardId,
           b.title AS title,
           b.content as content,
           b.board_date AS boardDate,
           b.board_time as boardTime,
           b.board_type as boardType,
           a.phone_number as phoneNumber
    From board b
    INNER JOIN account a ON b.account_id = a.id
    where :nowDate = date_format(b.created_At, '%Y-%m-%d')
          AND :nowTime >= date_format(b.created_At, '%H-%i-%s')
    """, nativeQuery = true)
    List<BoardResult> findAllByCreatedAtEqualDateAndLessThanTime(@Param("nowDate")LocalDate nowDate, @Param("nowTime") LocalTime nowTime);
}
