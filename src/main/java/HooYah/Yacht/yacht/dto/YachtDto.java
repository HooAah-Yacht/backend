package HooYah.Yacht.yacht.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

public class YachtDto {

    @Data
    public static class CreateRequest {
        @NotBlank
        private String name;

        private String description;

        private Integer capacity;

        private BigDecimal pricePerHour;

        private String location;
    }

    @Data
    public static class UpdateRequest {
        private String name;
        private String description;
        private Integer capacity;
        private BigDecimal pricePerHour;
        private String location;
        private Boolean available;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private String description;
        private Integer capacity;
        private BigDecimal pricePerHour;
        private String location;
        private Boolean available;
        private String thumbnailPath;
    }

    @Data
    public static class ListResponse {
        private List<Response> content;
        private int page;
        private int size;
        private long totalElements;
    }
}
