package com.spring.alarm_todo_list.application.login.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountLoginRequestDto {

    @NotBlank(message="이메일은 필수값입니다.")
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @NotBlank(message="비밀번호는 필수값입니다.")
    private String password;
}
