package com.spring.alarm_todo_list.application.login.service;

import com.spring.alarm_todo_list.application.jwt.JwtTokenProvider;
import com.spring.alarm_todo_list.application.login.dto.AccountLoginRequestDto;
import com.spring.alarm_todo_list.application.login.dto.AccountLoginResponseDto;
import com.spring.alarm_todo_list.application.login.dto.JwtToken;
import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.account.repository.AccountRepository;
import com.spring.alarm_todo_list.exception.AlarmTodoListException;
import com.spring.alarm_todo_list.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public AccountLoginResponseDto login(AccountLoginRequestDto requestDto) {
        Account account = accountRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new AlarmTodoListException(ErrorCode.NOT_FOUND_ACCOUNT));

        if (!passwordEncoder.matches(requestDto.getPassword(), account.getPassword()))
            throw new AlarmTodoListException(ErrorCode.NOT_MATCH_EMAIL_PASSWORD);

        JwtToken jwtToken = jwtTokenProvider.createToken(account.getEmail());

        return new AccountLoginResponseDto(account.getId(), account.getNickName(), jwtToken.getToken(), jwtToken.getExpiresAt());
    }
}
