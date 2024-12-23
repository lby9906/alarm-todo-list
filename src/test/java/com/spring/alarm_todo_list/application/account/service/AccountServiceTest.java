package com.spring.alarm_todo_list.application.account.service;

import com.spring.alarm_todo_list.application.account.dto.request.AccountRequest;
import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.account.repository.AccountRepository;
import com.spring.alarm_todo_list.exception.AlarmTodoListException;
import com.spring.alarm_todo_list.exception.ErrorCode;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
class AccountServiceTest {

    @Autowired
    private AccountWriteService accountWriteService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void clear() {
        accountRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("이메일, 닉네임, 전화번호, 패스워드로 회원가입한다.")
    public void join() {
        //given
        String email = "test@test.com";
        String nickName = "test";
        String phoneNumber = "010-1111-2222";
        String password = "0000";

        AccountRequest request = new AccountRequest(email, nickName, phoneNumber, password);

        //when
        accountWriteService.join(request);

        //then
        Account account = accountRepository.findByEmail(email).get();
        assertThat(account.getEmail()).isEqualTo(email);
        assertThat(account.getNickName()).isEqualTo(nickName);
        assertThat(account.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(passwordEncoder.matches(password, account.getPassword())).isTrue();
    }

    @Test
    @DisplayName("이미 존재하는 이메일이 있을 경우 예외가 발생한다.")
    public void overlapAccount() {
        //given
        String email = "test@test.com";
        String nickName = "test";
        String phoneNumber = "010-1111-2222";
        String password = "0000";

        accountRepository.save(Account.of(email, nickName, phoneNumber, password));
        AccountRequest request = new AccountRequest(email, nickName, phoneNumber, password);

        //when && then
        AlarmTodoListException ex = Assertions.assertThrows(AlarmTodoListException.class, () -> accountWriteService.join(request));
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.EXISTING_ALREADY_ACCOUNT);
    }
}