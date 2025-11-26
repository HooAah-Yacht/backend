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

    // Flat 구조 지원 (기존)
    @JsonProperty("yachtName")
    private String yachtName;

    @JsonProperty("yachtAlias")
    private String yachtAlias;

    @JsonProperty("parts")
    @Valid
    private List<AddPartDto> parts;

    // 중첩 구조 지원 (Frontend 새 payload)
    @JsonProperty("yacht")
    private YachtInfo yacht;

    @JsonProperty("partList")
    @Valid
    private List<AddPartDto> partList;

    /**
     * 중첩 구조의 Yacht 정보
     */
    @Getter
    @Setter
    @NoArgsConstructor
    public static class YachtInfo {
        @NotEmpty
        private String name;
        
        private String nickName;
    }

    /**
     * 실제 요트 이름 반환 (Flat 또는 중첩 구조 모두 지원)
     */
    public String getYachtName() {
        if (yacht != null && yacht.getName() != null) {
            return yacht.getName();
        }
        return yachtName;
    }

    /**
     * 실제 요트 별명 반환 (Flat 또는 중첩 구조 모두 지원)
     */
    public String getYachtAlias() {
        if (yacht != null && yacht.getNickName() != null) {
            return yacht.getNickName();
        }
        return yachtAlias;
    }

    /**
     * 실제 부품 리스트 반환 (parts 또는 partList 모두 지원)
     */
    public List<AddPartDto> getParts() {
        if (partList != null && !partList.isEmpty()) {
            return partList;
        }
        return parts;
    }
}

