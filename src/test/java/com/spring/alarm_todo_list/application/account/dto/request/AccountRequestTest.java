package com.spring.alarm_todo_list.application.account.dto.request;

import com.spring.alarm_todo_list.application.account.service.AccountWriteService;
import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.account.repository.AccountRepository;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.xml.bind.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccountRequestTest {

    @Autowired
    private AccountWriteService accountWriteService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    private Validator validator;


    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("이메일을 작성하지 않는 경우 예외가 발생한다.")
    public void emailValidTest() {
        //given
        String email = null;
        String nickName = "test";
        String phoneNumber = "010-1234-1234";
        String password = "1234";

        AccountRequest request = new AccountRequest(email, nickName, phoneNumber, )

        //when

        //then
    }

    @Test
    void getNickName() {
    }

    @Test
    void getPhoneNumber() {
    }

    @Test
    void getPassword() {
    }
}