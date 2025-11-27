package HooYah.Yacht.calendar.dto.request;

import HooYah.Yacht.calendar.domain.CalendarType;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CalendarUpdateRequest {

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

    private Boolean byUser = true;

    private String content;

    private String review;

    private List<Long> userList = new ArrayList<>();

}

