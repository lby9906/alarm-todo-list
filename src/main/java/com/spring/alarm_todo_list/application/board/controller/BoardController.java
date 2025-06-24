package com.spring.alarm_todo_list.application.board.controller;

import com.spring.alarm_todo_list.application.account.dto.request.AccountInfo;
import com.spring.alarm_todo_list.application.board.dto.request.BoardRequest;
import com.spring.alarm_todo_list.application.board.dto.request.BoardSearchRequest;
import com.spring.alarm_todo_list.application.board.dto.request.BoardUpdateRequest;
import com.spring.alarm_todo_list.application.board.dto.response.BoardSearchListResponse;
import com.spring.alarm_todo_list.application.board.service.BoardReadService;
import com.spring.alarm_todo_list.application.board.service.BoardWriteService;
import com.spring.alarm_todo_list.config.LoginUser;
import com.spring.alarm_todo_list.util.PaginationResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardWriteService boardWriteService;
    private final BoardReadService boardReadService;

    @PostMapping
    public String create(@LoginUser AccountInfo accountInfo, @RequestBody @Valid BoardRequest boardRequest){
        return boardWriteService.create(accountInfo.getId(), boardRequest);
    }

    @GetMapping
    public PaginationResponse<BoardSearchListResponse> findAll(@LoginUser AccountInfo accountInfo, @ModelAttribute BoardSearchRequest boardSearchRequest) {
        return boardReadService.findAll(accountInfo, boardSearchRequest);
    }

    @PutMapping("/{boardId}")
    public String update(@LoginUser AccountInfo accountInfo, @PathVariable Long boardId, @RequestBody @Valid BoardUpdateRequest boardUpdateRequest) {
        return boardWriteService.update(accountInfo, boardId, boardUpdateRequest);
    }
}
