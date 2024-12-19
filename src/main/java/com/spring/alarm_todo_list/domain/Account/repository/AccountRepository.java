package com.spring.alarm_todo_list.domain.Account.repository;

import com.spring.alarm_todo_list.domain.Account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Long, Account> {

}
