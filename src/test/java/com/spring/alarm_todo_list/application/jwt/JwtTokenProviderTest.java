package com.spring.alarm_todo_list.application.jwt;

import com.spring.alarm_todo_list.application.login.dto.JwtToken;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("토큰이 정상적으로 생성된다.")
    void successCreateToken() {
        //given
        String email = "test@test.com";

        //when
        JwtToken token = jwtTokenProvider.createToken(email);

        //then
        assertThat(token).isNotNull();
        assertThat(token.getToken()).isNotBlank();
    }
}