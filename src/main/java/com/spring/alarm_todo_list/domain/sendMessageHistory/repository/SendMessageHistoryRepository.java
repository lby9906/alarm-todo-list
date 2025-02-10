package com.spring.alarm_todo_list.domain.sendMessageHistory.repository;

import com.spring.alarm_todo_list.application.schedule.dto.request.ScheduleRequest;
import com.spring.alarm_todo_list.domain.sendMessageHistory.entity.SendMessageHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface SendMessageHistoryRepository extends JpaRepository<SendMessageHistory, Long> {

    @Query(value = """
        SELECT smh.id AS sendMessageHistoryId, b.id AS boardId, send_message_history_type AS sendHistoryType, fail_count AS failCount,
        b.title AS title, b.board_date AS boardDate, b.board_time AS boardTime, a.phone_number AS userPhoneNumber
        FROM send_message_history smh
        INNER JOIN board b ON smh.board_id = b.id INNER JOIN account a ON b.account_id = a.id
        WHERE DATE_FORMAT(smh.created_At, '%Y-%m-%d') = :nowDate
    """,nativeQuery = true)
    List<ScheduleRequest> findAllByCurrentDate(@Param("nowDate") LocalDate nowDate);

}
