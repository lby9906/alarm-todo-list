package com.spring.alarm_todo_list.domain.sendMessageHistory.entity;

import com.spring.alarm_todo_list.domain.board.entity.Board;
import com.spring.alarm_todo_list.domain.common.entity.BaseEntity;
import com.spring.alarm_todo_list.domain.sendMessageHistory.enums.SendMessageHistoryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageHistory extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Enumerated(EnumType.STRING)
    private SendMessageHistoryType sendMessageHistoryType;

    private Long failCount;

    public static SendMessageHistory of(Board board, SendMessageHistoryType sendMessageHistoryType, Long failCount){
        return new SendMessageHistory(board, sendMessageHistoryType, failCount);
    }
}
