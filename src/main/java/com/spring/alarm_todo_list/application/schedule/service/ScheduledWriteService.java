package com.spring.alarm_todo_list.application.schedule.service;

import com.spring.alarm_todo_list.application.schedule.dto.request.ScheduleRequest;
import com.spring.alarm_todo_list.domain.board.BoardRepository;
import com.spring.alarm_todo_list.domain.board.entity.Board;
import com.spring.alarm_todo_list.domain.sendMessageHistory.entity.SendMessageHistory;
import com.spring.alarm_todo_list.domain.sendMessageHistory.enums.SendMessageHistoryType;
import com.spring.alarm_todo_list.domain.sendMessageHistory.repository.SendMessageHistoryRepository;
import com.spring.alarm_todo_list.exception.AlarmTodoListException;
import com.spring.alarm_todo_list.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
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
    public void scheduleFulfillment() {
        LocalDateTime now = LocalDateTime.now();
        List<Long> sendMessageHistories = sendMessageHistoryRepository.findAllByNowDateTimeCompareCreatedAt(now); //이미 발송된 문자내역의 boardId
        if (sendMessageHistories.isEmpty()) {
            sendTodoSchedule(sendMessageHistories);
        }
    }

    public String sendTodoSchedule(List<Long> sendMessageHistories){

        List<ScheduleRequest> scheduleRequests = boardRepository.schedule();

        List<ScheduleRequest> list = scheduleRequests.stream()
                .filter(scheduleRequest -> !sendMessageHistories.contains(scheduleRequest.getBoardId()))
                .toList();

        DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");

        scheduleRequests.forEach(scheduleRequest -> {
            Message message = new Message();
            message.setFrom(phoneNumber);
            message.setTo(scheduleRequest.getUserPhoneNumber());
            message.setText("[Todo-list] " + scheduleRequest.getBoardDate() + " " + scheduleRequest.getBoardTime() + " 계획하신 " + scheduleRequest.getTitle() + " 리마인드 알림입니다.");

            Board board = boardRepository.findById(scheduleRequest.getBoardId()).orElseThrow(() -> new AlarmTodoListException(ErrorCode.NOT_FOUND_ACCOUNT_REGISTER_BOARD));

            Long failCount = sendMessageHistoryRepository.countByBoardIdAndSendMessageHistoryType(board.getId(), SendMessageHistoryType.SEND_FAIL);

            try {
                messageService.sendOne(new SingleMessageSendingRequest(message));
                SendMessageHistory sendMessageHistory = SendMessageHistory.of(board, SendMessageHistoryType.SEND_SUCCESS, failCount);
                sendMessageHistoryRepository.save(sendMessageHistory);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());

                Long newCount = failCount + 1;

                SendMessageHistory sendMessageHistory = SendMessageHistory.of(board, SendMessageHistoryType.SEND_FAIL, newCount);
                sendMessageHistoryRepository.save(sendMessageHistory);
            }
        });
        return "문자 발송 완료";
    }
}
