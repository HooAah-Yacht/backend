package HooYah.Yacht.yacht.dto.response;

import HooYah.Yacht.yacht.domain.Yacht;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ResponseYachtDto {

    private Long id;
    private String name;

    @JsonProperty("alias")
    private String alias;

    public static ResponseYachtDto of (Yacht yacht) {
        ResponseYachtDto responseYachtDto = new ResponseYachtDto();
        responseYachtDto.id = yacht.getId();
        responseYachtDto.name = yacht.getName();
        responseYachtDto.alias = yacht.getAlias();
        return responseYachtDto;
    }

}
