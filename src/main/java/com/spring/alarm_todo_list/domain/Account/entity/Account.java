package com.spring.alarm_todo_list.domain.Account.entity;

import com.spring.alarm_todo_list.domain.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class Account extends BaseEntity {
    private String name;
}
