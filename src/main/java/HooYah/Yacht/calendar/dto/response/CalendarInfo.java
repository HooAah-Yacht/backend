package HooYah.Yacht.calendar.dto.response;

import HooYah.Yacht.calendar.domain.Calendar;
import HooYah.Yacht.calendar.domain.CalendarType;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CalendarInfo {

    private Long id;
    private CalendarType type;
    private Long partId;
    private Long yachtId;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private boolean completed;
    private String content;

    public static CalendarInfo from(Calendar calendar) {
        return CalendarInfo.builder()
                .id(calendar.getId())
                .type(calendar.getType())
                .partId(calendar.getPart() != null ? calendar.getPart().getId() : null)
                .yachtId(calendar.getYacht() != null ? calendar.getYacht().getId() : null)
                .startDate(calendar.getStartDate())
                .endDate(calendar.getEndDate())
                .completed(calendar.isCompleted())
                .content(calendar.getContent())
                .build();
    }
}