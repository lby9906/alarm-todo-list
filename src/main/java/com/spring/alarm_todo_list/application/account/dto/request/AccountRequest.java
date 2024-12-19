package com.spring.alarm_todo_list.application.account.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AccountRequest {

    @NotBlank(message="이메일은 필수값입니다.")
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @NotBlank(message="닉네임은 필수값입니다.")
    private String nickName;

    @NotBlank(message="핸드폰 번호는 필수값입니다.")
    private String phoneNumber;

    @NotBlank(message="비밀번호는 필수값입니다.")
    private String password;
}
