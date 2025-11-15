package HooYah.Yacht.calendar.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "calendar")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long partId;

    private OffsetDateTime startDate;

    private OffsetDateTime endDate;

    private String content;

    @Builder
    private Calendar(Long partId, OffsetDateTime startDate, OffsetDateTime endDate, String content) {
        this.partId = partId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.content = content;
    }

    public void update(Long partId, OffsetDateTime startDate, OffsetDateTime endDate, String content) {
        this.partId = partId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.content = content;
    }

    public void updateDates(OffsetDateTime startDate, OffsetDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}