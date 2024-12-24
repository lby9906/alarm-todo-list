package com.spring.alarm_todo_list.domain.account.entity;

import com.spring.alarm_todo_list.domain.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Account extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 30)
    private String nickName;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;


    public static Account of(String email, String nickName, String phoneNumber, String password){
        return new Account(email, nickName, phoneNumber, password);
    }
}
