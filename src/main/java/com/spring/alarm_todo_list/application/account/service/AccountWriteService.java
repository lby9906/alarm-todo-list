package com.spring.alarm_todo_list.application.account.service;

import com.spring.alarm_todo_list.application.account.dto.request.AccountRequest;
import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.account.repository.AccountRepository;
import com.spring.alarm_todo_list.exception.AlarmTodoListException;
import com.spring.alarm_todo_list.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class AccountWriteService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public String join(AccountRequest accountRequest){
        Optional<Account> email = accountRepository.findByEmail(accountRequest.getEmail());

        if (email.isPresent()) {
            throw new AlarmTodoListException(ErrorCode.EXISTING_ALREADY_ACCOUNT);
        }

        Account account = new Account(accountRequest.getEmail(), accountRequest.getNickName(), accountRequest.getPhoneNumber(), passwordEncoder.encode(accountRequest.getPassword()));
        accountRepository.save(account);
        return "회원 가입 성공";
    }
}
