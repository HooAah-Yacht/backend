package HooYah.Yacht.alarm.controller;

import HooYah.Yacht.alarm.service.AlarmService;
import HooYah.Yacht.alarm.service.FCMService;
import HooYah.Yacht.common.SuccessResponse;
import HooYah.Yacht.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/alarm")
public class AlarmController {

    private final AlarmService alarmService;
    private final FCMService fCMService;

    @GetMapping
    public ResponseEntity getAlarmList(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK, "succes", alarmService.getAlarmList(user)));
    }

    @PostMapping
    @Scheduled(cron = "0 0 9 * * *") // 매일 아침 9시 전송됨
    public ResponseEntity sendAlarm() {
        log.info("total send alarm start");
        alarmService.sendAlarm();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/fcm-test")
    @Transactional
    public ResponseEntity fcmTest(@RequestParam("token") String token, @AuthenticationPrincipal User user) {
        user.setToken(token);
        fCMService.send(user, "test message");
        return ResponseEntity.ok().build();
    }

}
