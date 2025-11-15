package HooYah.Yacht.yacht.dto.request;

import HooYah.Yacht.part.dto.request.AddPartDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CreateYachtWithPartsDto {

    @NotEmpty
    @JsonProperty("yachtName")
    private String yachtName;

    @JsonProperty("yachtAlias")
    private String yachtAlias;

    @JsonProperty("parts")
    @Valid
    private List<AddPartDto> parts;
}

