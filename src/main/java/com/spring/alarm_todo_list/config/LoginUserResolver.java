package com.spring.alarm_todo_list.config;

import com.spring.alarm_todo_list.application.account.dto.request.AccountInfo;
import com.spring.alarm_todo_list.application.jwt.JwtTokenProvider;
import com.spring.alarm_todo_list.application.login.service.LoginService;
import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.account.repository.AccountRepository;
import com.spring.alarm_todo_list.exception.AlarmTodoListException;
import com.spring.alarm_todo_list.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.struts.chain.commands.UnauthorizedActionException;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginUserResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;
    private final AccountRepository accountRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public AccountInfo resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            throw new AlarmTodoListException(ErrorCode.EMPTY_JWT_TOKEN);
        }

        String[] tokenSplit = token.split(" ");
        if (tokenSplit.length != 2 || !tokenSplit[0].equals("Bearer")) {
            throw new AlarmTodoListException(ErrorCode.AUTH_INVALID_TOKEN);
        }
        token = tokenSplit[1];

        if (!jwtTokenProvider.validateTokenExceptExpiration(token)) {
            throw new UnauthorizedActionException(ErrorCode.AUTH_INVALID_TOKEN.getMessage());
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        String authenticationName = authentication.getName();

        Account account = accountRepository.findByEmail(authenticationName).orElseThrow(() -> new AlarmTodoListException(ErrorCode.NOT_FOUND_ACCOUNT));

        return AccountInfo.from(account);
    }
}