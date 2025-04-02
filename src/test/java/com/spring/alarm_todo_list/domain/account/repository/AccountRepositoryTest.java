package com.spring.alarm_todo_list.domain.account.repository;

import com.spring.alarm_todo_list.QuerydslConfig;
import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.account.enums.LoginType;
import com.spring.alarm_todo_list.domain.account.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QuerydslConfig.class})
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;


    @BeforeEach
    public void clear() {
        accountRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("회원 생성 후 이메일로 계정을 정상적으로 찾는다.")
    public void findByEmailSuccess() {
        //given
        String email = "test@test.com";
        String nickName = "test";
        String phoneNumber = "010-1111-2222";
        String password = "0000";

        Account account = new Account(email, nickName, phoneNumber, password, LoginType.BASIC, Role.USER);

        //when
        accountRepository.save(account);

        //then
        Optional<Account> find = accountRepository.findByEmail(email);
        assertThat(find).isPresent();
        assertThat(find.get().getEmail()).isEqualTo(email);
        assertThat(find.get().getNickName()).isEqualTo(nickName);
        assertThat(find.get().getPhoneNumber()).isEqualTo(phoneNumber);
    }
}