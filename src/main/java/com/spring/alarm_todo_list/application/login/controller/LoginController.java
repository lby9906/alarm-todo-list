package com.spring.alarm_todo_list.application.login.controller;

import com.spring.alarm_todo_list.application.login.dto.request.AccountLoginRequestDto;
import com.spring.alarm_todo_list.application.login.dto.AccountLoginResponseDto;
import com.spring.alarm_todo_list.application.login.dto.request.TokenRequestDto;
import com.spring.alarm_todo_list.application.login.dto.response.TokenResponseDto;
import com.spring.alarm_todo_list.application.login.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public AccountLoginResponseDto login(@Valid @RequestBody AccountLoginRequestDto requestDto) {
        AccountLoginResponseDto login = loginService.login(requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", String.format("Bearer %s", login.getAccessToken()));

        return login;
    }

    @PostMapping("/reIssue")
    public TokenResponseDto reIssue(@Valid @RequestBody TokenRequestDto requestDto) {
        return loginService.reIssue(requestDto);
    }
}
