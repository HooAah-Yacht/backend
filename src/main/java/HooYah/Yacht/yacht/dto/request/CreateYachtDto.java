package HooYah.Yacht.yacht.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreateYachtDto {

    @NotEmpty
    private String name;

}
