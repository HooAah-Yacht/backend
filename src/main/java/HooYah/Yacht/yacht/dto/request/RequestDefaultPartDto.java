package HooYah.Yacht.yacht.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
public class RequestDefaultPartDto {

    private List<MultipartFile> file;

    @NotEmpty
    private String name; // yacht name
    private String nickName; // yacht nick name

}
