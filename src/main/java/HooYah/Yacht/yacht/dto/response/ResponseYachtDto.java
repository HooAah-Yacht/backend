package HooYah.Yacht.yacht.dto.response;

import HooYah.Yacht.yacht.domain.Yacht;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ResponseYachtDto {

    private Long id;
    private String name;
    private String nickName;

    public static ResponseYachtDto of (Yacht yacht) {
        ResponseYachtDto responseYachtDto = new ResponseYachtDto();
        responseYachtDto.id = yacht.getId();
        responseYachtDto.name = yacht.getName();
        responseYachtDto.nickName = yacht.getNickName();
        return responseYachtDto;
    }

}
