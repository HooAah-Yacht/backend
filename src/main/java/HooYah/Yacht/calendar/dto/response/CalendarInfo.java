package HooYah.Yacht.calendar.dto.response;

import HooYah.Yacht.calendar.domain.Calendar;
import HooYah.Yacht.calendar.domain.CalendarType;
import HooYah.Yacht.part.domain.Part;
import HooYah.Yacht.yacht.domain.Yacht;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CalendarInfo {

    private Long id;
    private CalendarType type;
    private Long partId;
    private Long yachtId;
    private String yachtName;
    private String yachtNickName;
    private List<PartInfo> partList;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private boolean completed;
    private boolean byUser;
    private String content;
    private OffsetDateTime lastRepairDate;
    private String review;

    public static CalendarInfo from(Calendar calendar) {
        return from(calendar, null, null);
    }

    public static CalendarInfo from(Calendar calendar, Yacht yacht, List<Part> parts) {
        return CalendarInfo.builder()
                .id(calendar.getId())
                .type(calendar.getType())
                .partId(calendar.getPart() != null ? calendar.getPart().getId() : null)
                .yachtId(calendar.getYacht() != null ? calendar.getYacht().getId() : null)
                .yachtName(yacht != null ? yacht.getName() : null)
                .yachtNickName(yacht != null ? yacht.getNickName() : null)
                .partList(parts != null ? parts.stream()
                        .map(PartInfo::from)
                        .collect(Collectors.toList()) : null)
                .startDate(calendar.getStartDate())
                .endDate(calendar.getEndDate())
                .completed(calendar.isCompleted())
                .byUser(calendar.isByUser())
                .content(calendar.getContent())
                .lastRepairDate(calendar.getLastRepairDate())
                .review(calendar.getReview())
                .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PartInfo {
        private Long id;
        private String name;
        private String manufacturer;
        private String model;
        private Long interval;

        public static PartInfo from(Part part) {
            return PartInfo.builder()
                    .id(part.getId())
                    .name(part.getName())
                    .manufacturer(part.getManufacturer())
                    .model(part.getModel())
                    .interval(part.getInterval())
                    .build();
        }
    }
}