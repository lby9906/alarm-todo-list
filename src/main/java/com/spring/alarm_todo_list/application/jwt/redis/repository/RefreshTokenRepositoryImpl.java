package com.spring.alarm_todo_list.application.jwt.redis.repository;

import com.spring.alarm_todo_list.application.jwt.redis.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final RedisTemplate redisTemplate;

    @Override
    public void save(RefreshToken refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        if(!Objects.isNull(valueOperations.get(refreshToken.getEmail()))){
            redisTemplate.delete(refreshToken.getEmail());
        }
        valueOperations.set(refreshToken.getEmail(), refreshToken.getRefreshToken());
        redisTemplate.expire(refreshToken.getEmail(), 60 * 60 * 24, TimeUnit.SECONDS);
    }

    @Override
    public Optional<RefreshToken> findByEmail(String email) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String refreshToken = valueOperations.get(email);

        if (refreshToken == null) {
            return Optional.empty();
        } else {
            return Optional.of(new RefreshToken(email, refreshToken));
        }
    }

    @Override
    public void delete(String email) {
        redisTemplate.delete(email);
    }
}
