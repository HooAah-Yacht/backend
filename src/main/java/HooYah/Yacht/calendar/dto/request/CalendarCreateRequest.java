package HooYah.Yacht.calendar.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CalendarCreateRequest {

    @NotNull
    private Long partId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private String content;
}

