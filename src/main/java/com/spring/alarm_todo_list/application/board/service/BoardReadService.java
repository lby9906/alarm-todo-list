package com.spring.alarm_todo_list.application.board.service;

import com.spring.alarm_todo_list.application.account.dto.request.AccountInfo;
import com.spring.alarm_todo_list.application.board.dto.response.BoardListResponse;
import com.spring.alarm_todo_list.application.board.dto.response.BoardResponse;
import com.spring.alarm_todo_list.application.board.dto.response.BoardSearchListResponse;
import com.spring.alarm_todo_list.application.board.dto.response.BoardSearchResponse;
import com.spring.alarm_todo_list.domain.board.repository.BoardRepository;
import com.spring.alarm_todo_list.domain.board.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardReadService {

    private final BoardRepository boardRepository;

    public BoardResponse findAll(AccountInfo accountInfo, LocalDate boardDate) {

        if (boardDate == null) {
            boardDate = LocalDate.now();
        }
        List<Board> boardList = boardRepository.findAllByBoardDateAndAccountId(accountInfo.getId(), boardDate);

        List<BoardListResponse> boardListResponses = boardList.stream()
                .map(list -> new BoardListResponse(list.getId(),
                        list.getTitle(), list.getBoardDate(),
                        list.getBoardTime(), list.getBoardType()))
                .collect(Collectors.toList());

        return new BoardResponse(accountInfo.getNickName(), boardListResponses);
    }

    public BoardSearchResponse findSearch(String boardTitle, LocalDate boardDate, String boardContent ,AccountInfo accountInfo){

        List<Board> boardList = boardRepository.findSearchByBoardTitleAndBoardDateAndBoardContentAndAccountId(boardTitle, boardDate, boardContent ,accountInfo.getId());

        List<BoardSearchListResponse> boardSearchListResponses = boardList.stream()
                .map(board -> new BoardSearchListResponse(board.getId(), board.getTitle(), board.getContent()
                        ,board.getBoardDate(), board.getBoardTime(), board.getBoardType()))
                .collect(Collectors.toList());

        return new BoardSearchResponse(boardSearchListResponses);
    }
}
