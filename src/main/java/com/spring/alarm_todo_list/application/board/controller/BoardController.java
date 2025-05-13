package com.spring.alarm_todo_list.application.board.controller;

import com.spring.alarm_todo_list.application.board.dto.request.BoardRequest;
import com.spring.alarm_todo_list.application.board.dto.request.BoardUpdateRequest;
import com.spring.alarm_todo_list.application.board.dto.response.BoardResponse;
import com.spring.alarm_todo_list.application.board.service.BoardReadService;
import com.spring.alarm_todo_list.application.board.service.BoardWriteService;
import com.spring.alarm_todo_list.config.LoginUser;
import com.spring.alarm_todo_list.domain.account.entity.Account;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardWriteService boardWriteService;
    private final BoardReadService boardReadService;

    @PostMapping
    public String create(@LoginUser Account account, @RequestBody @Valid BoardRequest boardRequest){
        return boardWriteService.create(account, boardRequest);
    }

    @GetMapping
    public BoardResponse findAll(@LoginUser Account account, @RequestParam(required = false) LocalDate boardDate) {
        return boardReadService.findAll(account, boardDate);
    }

    @PutMapping("/{boardId}")
    public String update(@LoginUser Account account, @PathVariable Long boardId, @RequestBody @Valid BoardUpdateRequest boardUpdateRequest) {
        return boardWriteService.update(account, boardId, boardUpdateRequest);
    }
}
