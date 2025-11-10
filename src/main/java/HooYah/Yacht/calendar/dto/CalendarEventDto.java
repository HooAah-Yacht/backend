package HooYah.Yacht.calendar.dto;

import HooYah.Yacht.calendar.domain.CalendarEvent;
import HooYah.Yacht.calendar.domain.CalendarType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class CalendarEventDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        @NotNull
        private String title;
        @NotNull
        private CalendarType type;
        @NotNull
        private LocalDateTime startAt;
        @NotNull
        private LocalDateTime endAt;
        private Boolean allDay; // optional, default false
        private String note;
        private String color; // optional (예: #FF0000)
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private String title;
        private CalendarType type;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private Boolean allDay;
        private String note;
        private String color;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long yachtId;
        private String title;
        private CalendarType type;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private boolean allDay;
        private String note;
        private String color;

        public static Response from(CalendarEvent event) {
            return Response.builder()
                    .id(event.getId())
                    .yachtId(event.getYacht().getId())
                    .title(event.getTitle())
                    .type(event.getType())
                    .startAt(event.getStartAt())
                    .endAt(event.getEndAt())
                    .allDay(event.isAllDay())
                    .note(event.getNote())
                    .color(event.getColor())
                    .build();
        }
    }
}
