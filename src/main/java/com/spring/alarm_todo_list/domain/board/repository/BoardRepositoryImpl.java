package com.spring.alarm_todo_list.domain.board.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.alarm_todo_list.domain.board.entity.Board;
import com.spring.alarm_todo_list.domain.board.entity.QBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.spring.alarm_todo_list.domain.board.entity.QBoard.board;
import static com.spring.alarm_todo_list.domain.account.entity.QAccount.account;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardTodoRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Board> findAllBoardAndAccountId(Long accountId, LocalDate localDate) {
        return jpaQueryFactory
                .select(board)
                .from(board)
                .innerJoin(board.account, account)
                .where(eqNotNullDate(localDate), eqAccountId(accountId))
                .fetch();
    }

    private BooleanExpression eqNotNullDate(LocalDate localDate){
        return localDate != null ? board.boardDate.eq(localDate) : null;
    }

    private BooleanExpression eqAccountId(Long accountId){
        return accountId != null ? board.account.id.eq(accountId) : null;
    }
}
