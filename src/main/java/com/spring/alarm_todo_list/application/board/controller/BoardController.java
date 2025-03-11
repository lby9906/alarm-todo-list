package com.spring.alarm_todo_list.application.board.controller;

import com.spring.alarm_todo_list.application.board.dto.request.BoardRequest;
import com.spring.alarm_todo_list.application.board.dto.response.BoardListResponse;
import com.spring.alarm_todo_list.application.board.dto.response.BoardResponse;
import com.spring.alarm_todo_list.application.board.service.BoardReadService;
import com.spring.alarm_todo_list.application.board.service.BoardWriteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardWriteService boardWriteService;
    private final BoardReadService boardReadService;

    @PostMapping("/{accountId}")
    public String create(@PathVariable Long accountId, @RequestBody @Valid BoardRequest boardRequest){
        return boardWriteService.create(accountId, boardRequest);
    }

    @GetMapping("/{accountId}")
    public BoardResponse findAll(@PathVariable Long accountId) {
        return boardReadService.findAll(accountId);
    }
}
