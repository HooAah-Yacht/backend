package HooYah.Yacht.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OffsetDateTimeConfig {

    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
    }

}
