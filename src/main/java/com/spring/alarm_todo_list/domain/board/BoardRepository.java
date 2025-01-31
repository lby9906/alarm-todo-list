package com.spring.alarm_todo_list.domain.board;


import com.spring.alarm_todo_list.application.schedule.dto.request.ScheduleRequest;
import com.spring.alarm_todo_list.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query(value = """
            SELECT b.id AS boardId,
            b.title AS title,
            b.board_date AS boardDate,
            b.board_time AS boardTime, 
            a.phone_number AS userPhoneNumber 
            FROM board b INNER JOIN account a ON b.account_id = a.id
            WHERE b.board_date = CURRENT_DATE() AND b.board_time <= CURRENT_TIME()
            """, nativeQuery = true)
    List<ScheduleRequest> schedule();
}
