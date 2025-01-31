package com.spring.alarm_todo_list.application.schedule.controller;

import com.spring.alarm_todo_list.application.schedule.service.ScheduledWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduledController {


    private final ScheduledWriteService scheduledWriteService;

    @PostMapping
    public void sendTodoSchedule() {
        scheduledWriteService.scheduleFulfillment();
    }
}
