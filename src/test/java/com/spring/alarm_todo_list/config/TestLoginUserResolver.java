package com.spring.alarm_todo_list.config;

import com.spring.alarm_todo_list.application.jwt.JwtTokenProvider;
import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.account.repository.AccountRepository;
import com.spring.alarm_todo_list.exception.AlarmTodoListException;
import com.spring.alarm_todo_list.exception.ErrorCode;
import org.apache.struts.chain.commands.UnauthorizedActionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class TestLoginUserResolver implements HandlerMethodArgumentResolver {

    private final Account account;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountRepository accountRepository;

    public TestLoginUserResolver(Account account, JwtTokenProvider jwtTokenProvider, AccountRepository accountRepository) {
        this.account = account;
        this.jwtTokenProvider = jwtTokenProvider;
        this.accountRepository = accountRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            throw new AlarmTodoListException(ErrorCode.EMPTY_JWT_TOKEN);
        }
        if (!token.startsWith("Bearer ")) {
            throw new AlarmTodoListException(ErrorCode.AUTH_INVALID_TOKEN);
        }
        token = token.substring(7);

        if (!jwtTokenProvider.validateTokenExceptExpiration(token)) {
            throw new UnauthorizedActionException(ErrorCode.AUTH_INVALID_TOKEN.getMessage());
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        String str = authentication.getName();

        return accountRepository.findByEmail(str).orElseThrow(() -> new AlarmTodoListException(ErrorCode.NOT_FOUND_ACCOUNT));
    }
}