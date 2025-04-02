package com.spring.alarm_todo_list.application.jwt;

import com.spring.alarm_todo_list.application.login.service.AccountDetailService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${spring.jwt.secretKey}")
    private String secretKey;

    private Key key;
    private final long tokenValidTime = 1000L * 60 * 30;

    private final AccountDetailService accountDetailService;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = accountDetailService.loadUserByUsername(getAccountEmail(token));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    public String getAccountEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody()
                        .getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String authorization = req.getHeader("Authorization");

        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")){
            return authorization.substring(7);
        }
        return null;
    }


    public Boolean validateTokenExceptExpiration(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return true;
        } catch (SecurityException | UnsupportedJwtException | IllegalArgumentException | MalformedJwtException e) {
            e.printStackTrace();
        } catch (ExpiredJwtException e) {
            return false;
        }
        return false;
    }
}
