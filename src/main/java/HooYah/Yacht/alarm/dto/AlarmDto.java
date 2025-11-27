package HooYah.Yacht.alarm.dto;

import HooYah.Yacht.alarm.domain.Alarm;
import HooYah.Yacht.part.dto.response.PartDto;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AlarmDto {

    private PartDto part;
    private OffsetDateTime date;

    public static AlarmDto of(Alarm alarm) {
        AlarmDto alarmDto = new AlarmDto();
        alarmDto.date = alarm.getDate();
        alarmDto.part = PartDto.of(alarm.getPart(), null);
        return alarmDto;
    }

}
