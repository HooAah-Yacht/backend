package HooYah.Yacht.part.dto.response;

import HooYah.Yacht.part.domain.Part;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class PartDto {

    private Long id;
    private String name;
    private String manufacturer;
    private String model;
    private Long interval;

    @JsonProperty("latestMaintenanceDate")
    private LocalDate latestMaintenanceDate;

    @JsonProperty("lastRepairDate")
    private LocalDate lastRepairDate;

    @JsonProperty("nextRepairDate")
    private LocalDate nextRepairDate;

    public static PartDto of(Part part) {
        PartDto partDto = new PartDto();
        partDto.id = part.getId();
        partDto.name = part.getName();
        partDto.manufacturer = part.getManufacturer();
        partDto.model = part.getModel();
        partDto.interval = part.getInterval();
        partDto.latestMaintenanceDate = part.getLatestMaintenanceDate();
        return partDto;
    }

}
