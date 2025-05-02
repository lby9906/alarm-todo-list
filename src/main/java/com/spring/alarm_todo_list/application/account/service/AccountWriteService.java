package com.spring.alarm_todo_list.application.account.service;

import com.spring.alarm_todo_list.application.account.dto.request.AccountRequest;
import com.spring.alarm_todo_list.application.account.dto.response.AccountResponse;
import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.account.enums.LoginType;
import com.spring.alarm_todo_list.domain.account.enums.Role;
import com.spring.alarm_todo_list.domain.account.repository.AccountRepository;
import com.spring.alarm_todo_list.exception.AlarmTodoListException;
import com.spring.alarm_todo_list.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class AccountWriteService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountResponse join(AccountRequest accountRequest){
        Optional<Account> email = accountRepository.findByEmail(accountRequest.getEmail());

        if (email.isPresent()) {
            throw new AlarmTodoListException(ErrorCode.EXISTING_ALREADY_ACCOUNT);
        }

        Account account = Account.of(accountRequest.getEmail(), accountRequest.getNickName(), accountRequest.getPhoneNumber(),
                passwordEncoder.encode(accountRequest.getPassword()), LoginType.BASIC, Role.ROLE_USER);
        accountRepository.save(account);

        return new AccountResponse(account.getId(), account.getEmail(), account.getNickName());
    }
}
