package com.spring.alarm_todo_list.application.board.controller;

import com.spring.alarm_todo_list.application.board.dto.BoardRequest;
import com.spring.alarm_todo_list.application.board.service.BoardWriteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardWriteService boardWriteService;

    @PostMapping("/{accountId}")
    public String create(@PathVariable Long accountId, @RequestBody @Valid BoardRequest boardRequest){
        return boardWriteService.create(accountId, boardRequest);
    }
}
