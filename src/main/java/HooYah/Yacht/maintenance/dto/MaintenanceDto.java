package HooYah.Yacht.maintenance.dto;

import HooYah.Yacht.maintenance.domain.Maintenance;
import HooYah.Yacht.maintenance.domain.MaintenanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MaintenanceDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        @NotNull(message = "정비 유형은 필수입니다")
        private String maintenanceType;

        private String description;

        @NotNull(message = "예정 정비일은 필수입니다")
        private LocalDate scheduledDate;

        @NotNull(message = "비용은 필수입니다")
        private Long cost;

        private String technician;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private String maintenanceType;
        private String description;
        private LocalDate scheduledDate;
        private Long cost;
        private String technician;
        private MaintenanceStatus status;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long yachtId;
        private String yachtName;
        private String maintenanceType;
        private String description;
        private LocalDate scheduledDate;
        private LocalDate completedDate;
        private MaintenanceStatus status;
        private Long cost;
        private String technician;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static Response from(Maintenance maintenance) {
            return Response.builder()
                    .id(maintenance.getId())
                    .yachtId(maintenance.getYacht().getId())
                    .yachtName(maintenance.getYacht().getName())
                    .maintenanceType(maintenance.getMaintenanceType())
                    .description(maintenance.getDescription())
                    .scheduledDate(maintenance.getScheduledDate())
                    .completedDate(maintenance.getCompletedDate())
                    .status(maintenance.getStatus())
                    .cost(maintenance.getCost())
                    .technician(maintenance.getTechnician())
                    .createdAt(maintenance.getCreatedAt())
                    .updatedAt(maintenance.getUpdatedAt())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ListResponse {
        private Long id;
        private Long yachtId;
        private String yachtName;
        private String maintenanceType;
        private LocalDate scheduledDate;
        private MaintenanceStatus status;
        private Long cost;

        public static ListResponse from(Maintenance maintenance) {
            return ListResponse.builder()
                    .id(maintenance.getId())
                    .yachtId(maintenance.getYacht().getId())
                    .yachtName(maintenance.getYacht().getName())
                    .maintenanceType(maintenance.getMaintenanceType())
                    .scheduledDate(maintenance.getScheduledDate())
                    .status(maintenance.getStatus())
                    .cost(maintenance.getCost())
                    .build();
        }
    }
}
