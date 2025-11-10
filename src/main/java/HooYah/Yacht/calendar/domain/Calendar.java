package HooYah.Yacht.calendar.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String content;

    @Builder
    private Calendar(Long partId, LocalDateTime startDate, LocalDateTime endDate, String content) {
        this.partId = partId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.content = content;
    }

    public void update(Long partId, LocalDateTime startDate, LocalDateTime endDate, String content) {
        this.partId = partId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.content = content;
    }

    public void updateDates(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}