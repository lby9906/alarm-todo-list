package com.spring.alarm_todo_list.domain.board.entity;

import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.board.enums.BoardType;
import com.spring.alarm_todo_list.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Board extends BaseEntity {

    @Column(nullable = false, length = 500)
    private String title;

    @Column(length = 500)
    private String content;

    @Column(nullable = false)
    private LocalDate boardDate;

    @Column(nullable = false)
    private LocalTime boardTime;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    public static Board of(String title, String content, LocalDate boardDate, LocalTime boardTime, BoardType boardType, Account account){
        return new Board(title, content, boardDate, boardTime, boardType, account);
    }

    public void update(String title, String content, LocalDate boardDate, LocalTime boardTime) {
        this.title = title;
        this.content = content;
        this.boardDate = boardDate;
        this.boardTime = boardTime;
    }

    public void typeUpdate(BoardType boardType) {
        this.boardType = boardType;
    }
}
