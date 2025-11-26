package HooYah.Yacht.calendar.dto.response;

import HooYah.Yacht.calendar.domain.CalendarEvent;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CalendarInfo {

    private Long id;
    private Long partId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String content;

    public static CalendarInfo from(CalendarEvent calendar) {
        return CalendarInfo.builder()
                .id(calendar.getId())
                .partId(calendar.getPart().getId())
                .startDate(calendar.getStartDate())
                .endDate(calendar.getEndDate())
                .content(calendar.getContent())
                .build();
    }
}