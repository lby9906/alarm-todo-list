package com.spring.alarm_todo_list.domain.board.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.alarm_todo_list.domain.board.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

import static com.spring.alarm_todo_list.domain.board.entity.QBoard.board;
import static com.spring.alarm_todo_list.domain.account.entity.QAccount.account;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardTodoQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Board> findAllByBoardDateAndAccountId(Long accountId, LocalDate boardDate) {
        return jpaQueryFactory
                .select(board)
                .from(board)
                .innerJoin(board.account, account)
                .where(eqNotNullDate(boardDate), eqAccountId(accountId))
                .fetch();
    }

    @Override
    public List<Board> findSearchByBoardTitleAndBoardDateAndBoardContentAndAccountId(String boardTitle, LocalDate boardDate, String boardContent, Long accountId) {
        return jpaQueryFactory
                .select(board)
                .from(board)
                .innerJoin(board.account, account)
                .where(eqNotNullDate(boardDate), likeTitle(boardTitle), likeContent(boardContent) ,eqAccountId(accountId))
                .fetch();
    }

    private BooleanExpression eqNotNullDate(LocalDate boardDate){
        return boardDate != null ? board.boardDate.eq(boardDate) : null;
    }

    private BooleanExpression eqAccountId(Long accountId){
        return accountId != null ? board.account.id.eq(accountId) : null;
    }

    private BooleanExpression likeTitle(String boardTitle){
        return StringUtils.hasText(boardTitle) ? board.title.contains(boardTitle) : null;
    }

    private BooleanExpression likeContent(String boardContent){
        return StringUtils.hasText(boardContent) ? board.content.contains(boardContent) : null;
    }
}
