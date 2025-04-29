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

    private final long REFRESH_TOKEN_EXPIRE_SECONDS = 60 * 60 * 24;

    @Override
    public void save(RefreshToken refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String key = String.valueOf(refreshToken.getAccountId());

        if(!Objects.isNull(valueOperations.get(key))){
            redisTemplate.delete(key);
        }

        valueOperations.set(key, refreshToken.getRefreshToken());
        redisTemplate.expire(key, REFRESH_TOKEN_EXPIRE_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public Optional<RefreshToken> findByAccountId(Long id) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String refreshToken = valueOperations.get(id);

        if (refreshToken == null) {
            return Optional.empty();
        } else {
            return Optional.of(new RefreshToken(id, refreshToken));
        }
    }

    @Override
    public void delete(Long id) {
        redisTemplate.delete(id);
    }
}
