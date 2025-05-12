package com.spring.alarm_todo_list.application.board.controller;

import com.spring.alarm_todo_list.application.board.dto.request.BoardRequest;
import com.spring.alarm_todo_list.application.board.dto.request.BoardUpdateRequest;
import com.spring.alarm_todo_list.application.board.dto.response.BoardListResponse;
import com.spring.alarm_todo_list.application.board.dto.response.BoardResponse;
import com.spring.alarm_todo_list.application.board.service.BoardReadService;
import com.spring.alarm_todo_list.application.board.service.BoardWriteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public BoardResponse findAll(@PathVariable Long accountId, @RequestParam(required = false) LocalDate boardDate) {
        return boardReadService.findAll(accountId, boardDate);
    }

    @PutMapping("/{accountId}/{boardId}")
    public String update(@PathVariable Long accountId, @PathVariable Long boardId, @RequestBody @Valid BoardUpdateRequest boardUpdateRequest) {
        return boardWriteService.update(accountId, boardId, boardUpdateRequest);
    }
}
