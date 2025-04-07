package com.spring.alarm_todo_list.application.login.controller;

import com.spring.alarm_todo_list.application.login.dto.AccountLoginRequestDto;
import com.spring.alarm_todo_list.application.login.dto.AccountLoginResponseDto;
import com.spring.alarm_todo_list.application.login.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RestControllerAdvice
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validation(MethodArgumentNotValidException e) {
        Map<String, String> error = new HashMap<>();
        e.getAllErrors().forEach(
                c -> error.put(((FieldError) c).getField(), c.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(error);
    }


    @PostMapping("/login")
    public AccountLoginResponseDto login(@Valid @RequestBody AccountLoginRequestDto requestDto) {
        AccountLoginResponseDto login = loginService.login(requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + login.getToken());

        return login;
    }
}
