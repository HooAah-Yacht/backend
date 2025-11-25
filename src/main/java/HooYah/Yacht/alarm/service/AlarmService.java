package HooYah.Yacht.alarm.service;

import HooYah.Yacht.alarm.domain.Alarm;
import HooYah.Yacht.alarm.dto.AlarmDto;
import HooYah.Yacht.alarm.repository.AlarmRepository;
import HooYah.Yacht.user.domain.User;
import HooYah.Yacht.user.repository.YachtUserPort;
import HooYah.Yacht.yacht.domain.Yacht;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final YachtUserPort  yachtUserPort;

    @Deprecated
    public void createAlarm() {
        // can not create, update, delete alarm by user!
    }

    public List<AlarmDto> getAlarmList(User user) {
        List<Yacht> yachtList = yachtUserPort.findYachtListByUser(user.getId());

        List<Alarm> alarmList = alarmRepository.findAllByYachtIds(yachtList.stream().map(Yacht::getId).toList());
        return alarmList.stream().map(AlarmDto::of).toList();
    }

}
