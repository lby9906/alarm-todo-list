package com.spring.alarm_todo_list.application.jwt.redis;

import com.spring.alarm_todo_list.domain.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24)
public class RefreshToken extends BaseEntity {

    private String email;
    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
