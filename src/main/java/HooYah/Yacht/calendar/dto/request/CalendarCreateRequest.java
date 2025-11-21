package HooYah.Yacht.calendar.dto.request;

import HooYah.Yacht.calendar.domain.CalendarType;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CalendarCreateRequest {

    @NotNull
    private CalendarType type;

    private Long partId;

    @NotNull
    private Long yachtId;

    @NotNull
    private OffsetDateTime startDate;

    @NotNull
    private OffsetDateTime endDate;

    private Boolean completed;

    private Boolean byUser;

    private String content;

    private String review;
}

