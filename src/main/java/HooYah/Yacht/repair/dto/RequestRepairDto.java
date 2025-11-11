package HooYah.Yacht.repair.dto;

import HooYah.Yacht.DateUtil;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RequestRepairDto {

    private Long id;
    private String date;

    public LocalDate getDate() {
        return DateUtil.toLocalDate(date);
    }

}
