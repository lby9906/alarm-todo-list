package com.spring.alarm_todo_list.application.login.service;

import com.spring.alarm_todo_list.application.login.dto.request.AccountLoginRequestDto;
import com.spring.alarm_todo_list.application.login.dto.AccountLoginResponseDto;
import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.account.enums.LoginType;
import com.spring.alarm_todo_list.domain.account.enums.Role;
import com.spring.alarm_todo_list.domain.account.repository.AccountRepository;
import com.spring.alarm_todo_list.exception.AlarmTodoListException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void clear() {
        accountRepository.deleteAllInBatch();
    }

    private Account createAccount(String rawPassword) {
        String password = passwordEncoder.encode(rawPassword);
        Account account = Account.of("test@test.com", "test", "010-1234-1234", password, LoginType.BASIC, Role.ROLE_USER);
        return accountRepository.save(account);
    }

    @Test
    @DisplayName("존재하는 회원이 로그인 성공 시 토큰 및 사용자 정보가 반환된다.")
    void successLogin() {
        String password = "0000";

        //given
        Account savedAccount = createAccount(password);

        AccountLoginRequestDto requestDto = new AccountLoginRequestDto(savedAccount.getEmail(), password);

        //when
        AccountLoginResponseDto responseDto = loginService.login(requestDto);

        //then
        Account account = accountRepository.findByEmail(savedAccount.getEmail()).get();
        assertThat(account.getId()).isEqualTo(savedAccount.getId());
        assertThat(account.getNickName()).isEqualTo(savedAccount.getNickName());

        assertNotNull(responseDto.getAccessToken());
        assertNotNull(responseDto.getAccessExpiresAt());
        assertNotNull(responseDto.getRefreshToken());
        assertNotNull(responseDto.getRefreshExpiresAt());
        assertThat(responseDto.getAccessExpiresAt()).isAfter(LocalDateTime.now());
        assertThat(responseDto.getRefreshExpiresAt()).isAfter(LocalDateTime.now());
    }

    @Test
    @DisplayName("존재하는 회원이 로그인 시 이메일 또는 비밀번호가 일치하지 않으면 예외가 발생한다.")
    public void notMatchEmailPasswordException() {
        String password = "0000";

        //given
        Account savedAccount = createAccount(password);

        AccountLoginRequestDto requestDto = new AccountLoginRequestDto(savedAccount.getEmail(), "1111");

        //when
        AlarmTodoListException exception = assertThrows(AlarmTodoListException.class,
                () -> loginService.login(requestDto));

        //then
        assertThat(exception).isInstanceOf(AlarmTodoListException.class);
        assertThat(exception.getErrorCode().getMessage()).isEqualTo("이메일 또는 비밀번호가 일치하지 않습니다.");
    }
}