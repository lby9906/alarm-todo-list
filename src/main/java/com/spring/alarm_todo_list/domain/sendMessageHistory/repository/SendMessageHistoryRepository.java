package com.spring.alarm_todo_list.domain.sendMessageHistory.repository;

import com.spring.alarm_todo_list.domain.sendMessageHistory.entity.SendMessageHistory;
import com.spring.alarm_todo_list.domain.sendMessageHistory.enums.SendMessageHistoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SendMessageHistoryRepository extends JpaRepository<SendMessageHistory, Long> {

    @Query(value = """
    select board_id from send_message_history where
     date_format(:now, '%Y-%m-%d') = date_format(created_At, '%Y-%m-%d') 
     AND date_format(:now, '%H-%i-%s') >= date_format(created_At, '%H-%i-%s') 
     AND send_message_history_type like '%SEND_SUCCESS%';
""",nativeQuery = true)
    List<Long> findAllByNowDateTimeCompareCreatedAt(@Param("now") LocalDateTime now);

    Long countByBoardIdAndSendMessageHistoryType(Long boardId, SendMessageHistoryType type);
}
