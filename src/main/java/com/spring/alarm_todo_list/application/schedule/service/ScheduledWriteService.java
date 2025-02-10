package com.spring.alarm_todo_list.application.schedule.service;

import com.spring.alarm_todo_list.application.schedule.dto.request.BoardResult;
import com.spring.alarm_todo_list.application.schedule.dto.request.ScheduleRequest;
import com.spring.alarm_todo_list.domain.board.BoardRepository;
import com.spring.alarm_todo_list.domain.board.entity.Board;
import com.spring.alarm_todo_list.domain.sendMessageHistory.entity.SendMessageHistory;
import com.spring.alarm_todo_list.domain.sendMessageHistory.enums.SendMessageHistoryType;
import com.spring.alarm_todo_list.domain.sendMessageHistory.repository.SendMessageHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kotlin.reflect.jvm.internal.impl.builtins.StandardNames.FqNames.list;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ScheduledWriteService {

    @Value("${coolsms.phoneNumber}")
    private String phoneNumber;

    @Value("${coolsms.apiKey}")
    private String apiKey;

    @Value("${coolsms.secretKey}")
    private String apiSecretKey;

    private final BoardRepository boardRepository;

    private final SendMessageHistoryRepository sendMessageHistoryRepository;

    @Scheduled(cron = "0 */1 * * * *")
    public void dispatchScheduled() {
        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        List<ScheduleRequest> sendMessageHistories = sendMessageHistoryRepository.findAllByCurrentDate(nowDate); //이미 발송된 문자내역의 boardId

        List<BoardResult> pendingSendList = boardRepository.findAllByCreatedAtEqualDateAndLessThanTime(nowDate, nowTime); // 보내줘야 하는 것들

        for (ScheduleRequest sendHistory : sendMessageHistories) {
            for (BoardResult pend : pendingSendList) {
                if (Objects.equals(pend.getBoardId(), sendHistory.getBoardId())) {
                    pendingSendList.remove(pend);
                    break;
                }
            }
        }

        List<ScheduleRequest> failHistory = sendMessageHistories.stream()
                .filter(h -> h.getSendHistoryType().equals(SendMessageHistoryType.SEND_FAIL))
                .collect(Collectors.toList());

        sendTodoSchedule(pendingSendList, failHistory);
    }

    public void sendTodoSchedule(List<BoardResult> pendingSendList, List<ScheduleRequest> failHistory) {
        DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
        messagePendingSendList(pendingSendList, messageService);
        messageFailHistorySendList(failHistory, messageService);
    }

    private Message createMessage(String userPhoneNumber, LocalDate boardDate, LocalTime boardTime, String title) {
        Message message = new Message();
        message.setFrom(phoneNumber);
        message.setTo(userPhoneNumber);
        message.setText(String.format("[Todo-list] %s %s 계획하신 %s 리마인드 알림입니다.", boardDate, boardTime, title));
        return message;
    }

    private void messageFailHistorySendList(List<ScheduleRequest> failHistory, DefaultMessageService messageService) {
        failHistory.forEach(scheduleRequest -> {
            Message message = createMessage(scheduleRequest.getUserPhoneNumber(), scheduleRequest.getBoardDate(), scheduleRequest.getBoardTime(), scheduleRequest.getTitle());
            SendMessageHistory sendHistory = sendMessageHistoryRepository.findById(scheduleRequest.getSendMessageHistoryId()).orElse(null);
            try {
                messageService.sendOne(new SingleMessageSendingRequest(message));
                sendHistory.success();

            } catch (Exception e) {
                sendHistory.fail(sendHistory.getFailCount() + 1);
            }
        });
    }

    private void messagePendingSendList(List<BoardResult> pendingSendList, DefaultMessageService messageService) {
        pendingSendList.forEach(boardResult -> {
            Message message = createMessage(boardResult.getPhoneNumber(), boardResult.getBoardDate(), boardResult.getBoardTime(), boardResult.getTitle());

            Board board = boardRepository.findById(boardResult.getBoardId()).orElse(null);

            SendMessageHistory sendMessageHistory = SendMessageHistory.create(board);
            try {
                messageService.sendOne(new SingleMessageSendingRequest(message));
                sendMessageHistory.success();

            } catch (Exception e) {
                sendMessageHistory.fail(1L);
            }
            sendMessageHistoryRepository.save(sendMessageHistory);
        });
    }
}
