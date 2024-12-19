package com.spring.alarm_todo_list.domain.account.entity;

import com.spring.alarm_todo_list.domain.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Account extends BaseEntity {

    private String email;

    private String nickName;

    private String phoneNumber;

    private String password;
}
