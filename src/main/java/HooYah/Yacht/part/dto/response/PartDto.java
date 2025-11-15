package HooYah.Yacht.part.dto.response;

import HooYah.Yacht.part.domain.Part;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PartDto {

    private Long id;
    private String name;
    private String manufacturer;
    private String model;
    private Long interval;

    public static PartDto of(Part part) {
        PartDto partDto = new PartDto();
        partDto.id = part.getId();
        partDto.name = part.getName();
        partDto.manufacturer = part.getManufacturer();
        partDto.model = part.getModel();
        partDto.interval = part.getInterval();
        return partDto;
    }

    @Builder
    public PartDto(String name, String manufacturer, String model, Long interval) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.model = model;
        this.interval = interval;
    }
}
