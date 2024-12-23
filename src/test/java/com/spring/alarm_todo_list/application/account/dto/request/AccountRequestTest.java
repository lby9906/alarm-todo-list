package com.spring.alarm_todo_list.application.account.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
class AccountRequestTest {

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

        AccountRequest request = new AccountRequest(email, nickName, phoneNumber, password);

        //when
        Set<ConstraintViolation<AccountRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("이메일은 필수값입니다.")
                .hasSize(1);

    }

    @Test
    @DisplayName("이메일을 입력 할 경우 예외가 발생하지 않는다.")
    public void emailValidSuccess() {
        //given
        String email = "test@test.com";
        String nickName = "test";
        String phoneNumber = "010-1234-1234";
        String password = "1234";

        AccountRequest request = new AccountRequest(email, nickName, phoneNumber, password);

        //when
        Set<ConstraintViolation<AccountRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains()
                .hasSize(0);
    }

    @Test
    @DisplayName("이메일 형식이 올바르지 않을 경우 예외가 발생한다.")
    public void emailTypeValidTest() {
        //given
        String email = "test@@test.com";
        String nickName = "test";
        String phoneNumber = "010-1234-1234";
        String password = "1234";

        AccountRequest request = new AccountRequest(email, nickName, phoneNumber, password);

        //when
        Set<ConstraintViolation<AccountRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("잘못된 이메일 형식입니다.")
                .hasSize(1);
    }

    @Test
    @DisplayName("이메일 형식 일 경우 예외가 발생하지 않는다.")
    public void emailTypeValidSuccess() {
        //given
        String email = "test@test.com";
        String nickName = "test";
        String phoneNumber = "010-1234-1234";
        String password = "1234";

        AccountRequest request = new AccountRequest(email, nickName, phoneNumber, password);

        //when
        Set<ConstraintViolation<AccountRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains()
                .hasSize(0);
    }

    @Test
    @DisplayName("닉네임을 작성하지 않은 경우 예외가 발생한다.")
    public void nickNameValidTest() {
        //given
        String email = "test@test.com";
        String nickName = null;
        String phoneNumber = "010-1234-1234";
        String password = "1234";

        AccountRequest request = new AccountRequest(email, nickName, phoneNumber, password);

        //when
        Set<ConstraintViolation<AccountRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("닉네임은 필수값입니다.")
                .hasSize(1);
    }

    @Test
    @DisplayName("닉네임을 입력 할 경우 예외가 발생하지 않는다.")
    public void nickNameValidSuccess() {
        //given
        String email = "test@test.com";
        String nickName = "test";
        String phoneNumber = "010-1234-1234";
        String password = "1234";

        AccountRequest request = new AccountRequest(email, nickName, phoneNumber, password);

        //when
        Set<ConstraintViolation<AccountRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains()
                .hasSize(0);
    }

    @Test
    @DisplayName("핸드폰 번호를 작성하지 않을 경우 예외가 발생한다.")
    public void phoneNumberValidTest() {
        //given
        String email = "test@test.com";
        String nickName = "test";
        String phoneNumber = null;
        String password = "1234";

        AccountRequest request = new AccountRequest(email, nickName, phoneNumber, password);

        //when
        Set<ConstraintViolation<AccountRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("핸드폰 번호는 필수값입니다.")
                .hasSize(1);
    }

    @Test
    @DisplayName("핸드폰 번호를 입력 할 경우 예외가 발생하지 않는다.")
    public void phoneNumberValidSuccess() {
        //given
        String email = "test@test.com";
        String nickName = "test";
        String phoneNumber = "010-1234-1234";
        String password = "1234";

        AccountRequest request = new AccountRequest(email, nickName, phoneNumber, password);

        //when
        Set<ConstraintViolation<AccountRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains()
                .hasSize(0);
    }

    @Test
    @DisplayName("비밀번호를 작성하지 않을 경우 예외가 발생한다.")
    public void passwordValidTest() {
        //given
        String email = "test@test.com";
        String nickName = "test";
        String phoneNumber = "010-1234-1234";
        String password = null;

        AccountRequest request = new AccountRequest(email, nickName, phoneNumber, password);

        //when
        Set<ConstraintViolation<AccountRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("비밀번호는 필수값입니다.")
                .hasSize(1);
    }

    @Test
    @DisplayName("비밀번호를 입력 할 경우 예외가 발생하지 않는다.")
    public void passwordValidSuccess() {
        //given
        String email = "test@test.com";
        String nickName = "test";
        String phoneNumber = "010-1234-1234";
        String password = "1234";

        AccountRequest request = new AccountRequest(email, nickName, phoneNumber, password);

        //when
        Set<ConstraintViolation<AccountRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains()
                .hasSize(0);
    }
}