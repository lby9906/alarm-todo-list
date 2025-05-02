package com.spring.alarm_todo_list.application.jwt.redis.repository;

import com.spring.alarm_todo_list.application.jwt.redis.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    void save(RefreshToken refreshToken);

    Optional<RefreshToken> findByAccountId(Long id);

    void delete(Long id);
}
