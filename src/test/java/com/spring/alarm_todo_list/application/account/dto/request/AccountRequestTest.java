package com.spring.alarm_todo_list.application.account.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class AccountRequestTest {

    private Validator validator;


    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("이메일을 작성하지 않는 경우 예외가 발생한다.")
    public void occurEmailBlankException() {
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
    public void notOccurEmailSuccessException() {
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
    public void occurEmailFormatWrongException() {
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
    public void notOccurEmailFormatWrongException() {
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
    public void occurNickNameBlankException() {
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
    public void notOccurNickNameSuccessException() {
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
    public void occurPhoneNumberBlankException() {
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
    public void notOccurPhoneNumberSuccessException() {
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
    public void occurPasswordBlankException() {
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
    public void notOccurPasswordSuccessException() {
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
    @DisplayName("이메일이 비어있을 경우 예외가 발생한다.")
    public void occurEmailEmptyException() {
        //given
        String email = "";
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
    @DisplayName("닉네임이 비어있을 경우 예외가 발생한다.")
    public void occurNickNameEmptyException() {
        //given
        String email = "test@test.com";
        String nickName = "";
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
    @DisplayName("핸드폰 번호가 비어있을 경우 예외가 발생한다.")
    public void occurPhoneNumberEmptyException() {
        //given
        String email = "test@test.com";
        String nickName = "test";
        String phoneNumber = "";
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
    @DisplayName("비밀번호가 비어있을 경우 예외가 발생한다.")
    public void occurPasswordEmptyException() {
        //given
        String email = "test@test.com";
        String nickName = "test";
        String phoneNumber = "010-1234-1234";
        String password = "";

        AccountRequest request = new AccountRequest(email, nickName, phoneNumber, password);

        //when
        Set<ConstraintViolation<AccountRequest>> violations = validator.validate(request);

        //then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("비밀번호는 필수값입니다.")
                .hasSize(1);
    }
}