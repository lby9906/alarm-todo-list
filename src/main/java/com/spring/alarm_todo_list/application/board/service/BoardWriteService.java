package com.spring.alarm_todo_list.application.board.service;

import com.spring.alarm_todo_list.application.board.dto.request.BoardRequest;
import com.spring.alarm_todo_list.application.board.dto.request.BoardUpdateRequest;
import com.spring.alarm_todo_list.domain.account.entity.Account;
import com.spring.alarm_todo_list.domain.account.repository.AccountRepository;
import com.spring.alarm_todo_list.domain.board.repository.BoardRepository;
import com.spring.alarm_todo_list.domain.board.entity.Board;
import com.spring.alarm_todo_list.domain.board.enums.BoardType;
import com.spring.alarm_todo_list.exception.AlarmTodoListException;
import com.spring.alarm_todo_list.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardWriteService {

    private final BoardRepository boardRepository;

    public String create(Account account, BoardRequest boardRequest) {
        Board board = Board.of(boardRequest.getTitle(), boardRequest.getContent(), boardRequest.getBoardDate(), boardRequest.getBoardTime(),
                BoardType.TODO, account);

        boardRepository.save(board);
        return "일정 등록 성공";
    }

    public String update(Account account, Long boardId ,BoardUpdateRequest boardUpdateRequest) {
        Board board = boardRepository.findByIdAndAccountId(boardId, account.getId()).orElseThrow(() -> new AlarmTodoListException(ErrorCode.NOT_FOUND_ACCOUNT_REGISTER_BOARD));

        board.update(boardUpdateRequest.getTitle(), boardUpdateRequest.getContent(), boardUpdateRequest.getBoardDate(), boardUpdateRequest.getBoardTime());

        boardRepository.save(board);
        return "일정 수정 성공";
    }
}
