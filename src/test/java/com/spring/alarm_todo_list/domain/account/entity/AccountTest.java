package com.spring.alarm_todo_list.domain.account.entity;

import com.spring.alarm_todo_list.domain.account.enums.LoginType;
import com.spring.alarm_todo_list.domain.account.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {
    @Test
    @DisplayName("이메일, 닉네임, 전화번호, 비밀번호로 회원을 생성한다.")
    public void createAccount() {
        //given
        String email = "test@email.com";
        String nickname = "nickname";
        String phoneNumber = "010-1234-1234";
        String password = "test";
        LoginType loginType = LoginType.BASIC;
        Role role = Role.ROLE_USER;

        //when
        Account createAccount = Account.of(email, nickname, phoneNumber, password, loginType, role);

        //then
        assertThat(createAccount.getEmail()).isEqualTo(email);
        assertThat(createAccount.getNickName()).isEqualTo(nickname);
        assertThat(createAccount.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(createAccount.getPassword()).isEqualTo(password);
    }

}