package HooYah.Yacht.alarm.service;

import HooYah.Yacht.alarm.domain.Alarm;
import HooYah.Yacht.alarm.dto.AlarmDto;
import HooYah.Yacht.alarm.repository.AlarmRepository;
import HooYah.Yacht.part.domain.Part;
import HooYah.Yacht.user.domain.User;
import HooYah.Yacht.user.repository.YachtUserPort;
import HooYah.Yacht.yacht.domain.Yacht;
import HooYah.Yacht.yacht.repository.YachtRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final YachtUserPort  yachtUserPort;
    private final YachtRepository yachtRepository;
    private final FCMService fCMService;

    @Deprecated
    public void createAlarm() {
        // can not create, update, delete alarm by user!
    }

    public List<AlarmDto> getAlarmList(User user) {
        List<Yacht> yachtList = yachtUserPort.findYachtListByUser(user.getId());

        List<Alarm> alarmList = alarmRepository.findAllByYachtIds(yachtList.stream().map(Yacht::getId).toList());
        return alarmList.stream().map(AlarmDto::of).toList();
    }

    @Transactional
    public void sendAlarm() {
        List<Yacht> yachtList = yachtRepository.findAll();
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime oneDay = now.plusDays(1);
        OffsetDateTime oneWeek = now.plusWeeks(1);

        for (Yacht yacht : yachtList) {
            List<Part> partList = yacht.getParts();

            for(Part part : partList) {
                List<Alarm>  alarmList = part.getAlarms();

                if(alarmList != null && !alarmList.isEmpty()) {
                    Alarm alarm = alarmList.get(0);

                    // 최고의 코딩은 ctrl c + ctrl v 입니다! 하지만 로직 만들기 너무 귀찮은걸~
                    if (compareDay(alarm.getDate(), now))
                        sendAlarm(alarm);
                    if (compareDay(alarm.getDate(), oneDay))
                        sendAlarm(alarm);
                    if (compareDay(alarm.getDate(), oneWeek))
                        sendAlarm(alarm);
                }
            }
        }
    }

    private void sendAlarm(Alarm alarm) {
        Part part = alarm.getPart();
        String message = part.getName() + "의 정비일은 " + alarm.getDate() + " 입니다";

        List<User> yachtUserList = yachtUserPort.findUserListByYacht(part.getYacht().getId());

        yachtUserList.forEach(user -> {
            fCMService.send(user, message);
        });
    }

    private boolean compareDay(OffsetDateTime a, OffsetDateTime b) {
        if(a.getYear() != b.getYear())
            return false;
        if(a.getMonth() != b.getMonth())
            return false;
        if(a.getDayOfMonth() != b.getDayOfMonth())
            return false;

        return true;
    }

}
