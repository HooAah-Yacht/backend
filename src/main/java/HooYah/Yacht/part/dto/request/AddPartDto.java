package HooYah.Yacht.part.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class AddPartDto {

    private Long yachtId;

    private String name;
    private String manufacturer;
    private String model;
    private Long interval;

    // Frontend는 "lastRepair"로 보냄
    @JsonProperty("lastRepair")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDate latestMaintenanceDate;

}
