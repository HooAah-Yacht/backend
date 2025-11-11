package HooYah.Yacht.yacht.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdateYachtDto {

    @NotNull
    private Long id;

    @NotEmpty
    private String name;

}
