package com.spring.alarm_todo_list.application.login.service;

import com.spring.alarm_todo_list.application.jwt.JwtTokenProvider;
import com.spring.alarm_todo_list.application.jwt.redis.RefreshToken;
import com.spring.alarm_todo_list.application.jwt.redis.repository.RefreshTokenRepository;
import com.spring.alarm_todo_list.application.login.dto.request.AccountLoginRequestDto;
import com.spring.alarm_todo_list.application.login.dto.AccountLoginResponseDto;
import com.spring.alarm_todo_list.application.login.dto.JwtToken;
import com.spring.alarm_todo_list.application.login.dto.request.TokenRequestDto;
import com.spring.alarm_todo_list.application.login.dto.response.TokenResponseDto;
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
    private final RefreshTokenRepository refreshTokenRepository;

    public AccountLoginResponseDto login(AccountLoginRequestDto requestDto) {
        Account account = accountRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new AlarmTodoListException(ErrorCode.NOT_FOUND_ACCOUNT));

        if (!passwordEncoder.matches(requestDto.getPassword(), account.getPassword()))
            throw new AlarmTodoListException(ErrorCode.NOT_MATCH_EMAIL_PASSWORD);

        JwtToken jwtToken = jwtTokenProvider.createAllToken(account.getEmail());

        refreshTokenRepository.save(new RefreshToken(account.getId(), jwtToken.getRefreshToken()));

        return new AccountLoginResponseDto(account.getId(), account.getNickName(),
                jwtToken.getAccessToken(), jwtToken.getAccessExpiresAt(),
                jwtToken.getRefreshToken(), jwtToken.getRefreshExpiresAt());
    }

    public TokenResponseDto reIssue(TokenRequestDto requestDto) {
        if (!jwtTokenProvider.validateTokenExceptExpiration(requestDto.getRefreshToken()))
            throw new AlarmTodoListException(ErrorCode.EXPIRE_REFRESH_TOKEN);

        String email = jwtTokenProvider.getAccountEmail(requestDto.getRefreshToken());

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AlarmTodoListException(ErrorCode.NOT_FOUND_ACCOUNT));

        RefreshToken findRefreshToken = refreshTokenRepository.findByAccountId(account.getId())
                .orElseThrow(() -> new AlarmTodoListException(ErrorCode.NOT_FOUND_LOGIN_ACCOUNT));

        if (!findRefreshToken.getRefreshToken().equals(requestDto.getRefreshToken())) {
            throw new AlarmTodoListException(ErrorCode.NOT_MATCH_REFRESH_TOKEN);
        }

        JwtToken newToken = jwtTokenProvider.createAllToken(email);
        findRefreshToken.updateRefreshToken(newToken.getRefreshToken());
        refreshTokenRepository.save(findRefreshToken);

        return new TokenResponseDto(newToken.getAccessToken(), newToken.getAccessExpiresAt(),
                newToken.getRefreshToken(), newToken.getRefreshExpiresAt());
    }
}
