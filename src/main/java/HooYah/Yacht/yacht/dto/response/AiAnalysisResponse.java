package HooYah.Yacht.yacht.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * AI 분석 결과 응답 DTO
 * Python Flask AI API로부터 받는 응답 형식
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AiAnalysisResponse {
    
    private Boolean success;
    private String yachtId;
    private String yachtName;
    private List<AiPartDto> parts;
    private Integer totalParts;
    private DocumentInfo documentInfo;
    private String error;
    
    /**
     * AI 분석 부품 DTO
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AiPartDto {
        private String id;
        private String name;
        private String manufacturer;
        private String model;
        private Integer interval;
        private MaintenanceDetails maintenanceDetails;
    }
    
    /**
     * 정비 세부 정보
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MaintenanceDetails {
        private String recommendedInterval;
        private String maintenanceMethod;
        private String notes;
    }
    
    /**
     * 문서 정보 (PDF 분석 시)
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocumentInfo {
        private String fileName;
        private String manufacturer;
        private String model;
        private Integer year;
    }
    
    /**
     * 성공 여부 확인
     */
    public boolean isSuccess() {
        return success != null && success;
    }
    
    /**
     * 에러 여부 확인
     */
    public boolean hasError() {
        return error != null && !error.isEmpty();
    }
}

