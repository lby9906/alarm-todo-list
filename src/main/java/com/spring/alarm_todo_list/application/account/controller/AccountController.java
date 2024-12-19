package com.spring.alarm_todo_list.application.account.controller;

import com.spring.alarm_todo_list.application.account.dto.request.AccountRequest;
import com.spring.alarm_todo_list.application.account.service.AccountWriteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private final AccountWriteService accountWriteService;

    @PostMapping
    public String join(@RequestBody @Valid AccountRequest accountRequest) {
        return accountWriteService.join(accountRequest);
    }
}
