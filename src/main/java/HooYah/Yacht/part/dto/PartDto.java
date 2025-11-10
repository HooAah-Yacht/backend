package HooYah.Yacht.part.dto;

import HooYah.Yacht.part.domain.Part;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PartDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        @NotNull
        private String name;
        private String partNumber;
        @Min(0)
        private Integer quantity;
        @NotNull
        @Min(0)
        private Long unitPrice;
        private String supplier;
        private LocalDate lastReplacedDate;
        private String note;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private String name;
        private String partNumber;
        private Integer quantity;
        private Long unitPrice;
        private String supplier;
        private LocalDate lastReplacedDate;
        private String note;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QuantityUpdateRequest {
        @NotNull
        private Integer delta; // 증감량 (양수: 입고, 음수: 출고)
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long yachtId;
        private String yachtName;
        private String name;
        private String partNumber;
        private Integer quantity;
        private Long unitPrice;
        private String supplier;
        private LocalDate lastReplacedDate;
        private String note;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static Response from(Part part) {
            return Response.builder()
                    .id(part.getId())
                    .yachtId(part.getYacht().getId())
                    .yachtName(part.getYacht().getName())
                    .name(part.getName())
                    .partNumber(part.getPartNumber())
                    .quantity(part.getQuantity())
                    .unitPrice(part.getUnitPrice())
                    .supplier(part.getSupplier())
                    .lastReplacedDate(part.getLastReplacedDate())
                    .note(part.getNote())
                    .createdAt(part.getCreatedAt())
                    .updatedAt(part.getUpdatedAt())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ListResponse {
        private Long id;
        private String name;
        private String partNumber;
        private Integer quantity;
        private Long unitPrice;
        private LocalDate lastReplacedDate;

        public static ListResponse from(Part part) {
            return ListResponse.builder()
                    .id(part.getId())
                    .name(part.getName())
                    .partNumber(part.getPartNumber())
                    .quantity(part.getQuantity())
                    .unitPrice(part.getUnitPrice())
                    .lastReplacedDate(part.getLastReplacedDate())
                    .build();
        }
    }
}
