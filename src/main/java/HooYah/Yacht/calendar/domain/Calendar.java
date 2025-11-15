package HooYah.Yacht.calendar.domain;

import HooYah.Yacht.part.domain.Part;
import HooYah.Yacht.yacht.domain.Yacht;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CalendarType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
    private Part part;

    private OffsetDateTime startDate;

    private OffsetDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yacht_id")
    private Yacht yacht;

    @Column(nullable = false)
    private boolean completed;

    private String content;

    @Builder
    private Calendar(CalendarType type, Part part, Yacht yacht, OffsetDateTime startDate, OffsetDateTime endDate,
                     boolean completed, String content) {
        this.type = type;
        this.part = part;
        this.yacht = yacht;
        this.startDate = startDate;
        this.endDate = endDate;
        this.completed = completed;
        this.content = content;
    }

    public void update(CalendarType type, Part part, Yacht yacht, OffsetDateTime startDate, OffsetDateTime endDate,
                       Boolean completed, String content) {
        this.type = type;
        this.part = part;
        this.yacht = yacht;
        this.startDate = startDate;
        this.endDate = endDate;
        if (completed != null) {
            this.completed = completed;
        }
        this.content = content;
    }

    public void updateDates(OffsetDateTime startDate, OffsetDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}