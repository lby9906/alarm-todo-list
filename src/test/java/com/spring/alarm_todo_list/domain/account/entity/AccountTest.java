package com.spring.alarm_todo_list.domain.account.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
class AccountTest {
    @Test
    @DisplayName("이메일, 닉네임, 전화번호, 비밀번호로 회원을 생성한다.")
    public void createAccount() {
        //given
        String email = "test@email.com";
        String nickname = "nickname";
        String phoneNumber = "010-1234-1234";
        String password = "test";

        //when
        Account createAccount = Account.of(email, nickname, phoneNumber, password);

        //then
        assertThat(createAccount.getPhoneNumber()).isEqualTo(phoneNumber);
    }

}