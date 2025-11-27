package HooYah.Yacht.alarm.service;

import HooYah.Yacht.common.excetion.CustomException;
import HooYah.Yacht.common.excetion.ErrorCode;
import HooYah.Yacht.user.domain.User;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FCMService {

    private static final String TITLE = "hooaah";

    public void send(User user, String body) {
        try {
            Message message = Message.builder()
                    .setToken(user.getToken())
                    .setNotification(
                            Notification.builder()
                                    .setTitle(TITLE)
                                    .setBody(body)
                                    .build())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("FCM 메시지 전송 성공: {}, response {}", body, response);

        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.CONFLICT);
        }
    }
}
