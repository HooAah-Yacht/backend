package HooYah.Yacht.calendar.dto.response;

import HooYah.Yacht.calendar.domain.Calendar;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CalendarInfo {

    private Long id;
    private Long partId;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private String content;

    public static CalendarInfo from(Calendar calendar) {
        return CalendarInfo.builder()
                .id(calendar.getId())
                .partId(calendar.getPartId())
                .startDate(calendar.getStartDate())
                .endDate(calendar.getEndDate())
                .content(calendar.getContent())
                .build();
    }
}