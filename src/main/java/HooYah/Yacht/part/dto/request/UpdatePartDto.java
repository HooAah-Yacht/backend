package HooYah.Yacht.part.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class UpdatePartDto {
    private Long id;
    private String name;
    private String manufacturer;
    private String model;
    private Long interval;

    @JsonProperty("latestMaintenanceDate")
    private LocalDate latestMaintenanceDate;
}
