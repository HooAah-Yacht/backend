package HooYah.Yacht.calendar.domain;

import HooYah.Yacht.part.domain.Part;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * ERD 기준 Calendar 엔티티
 * - id, part_id, start_date, end_date, content
 */
@Entity
@Table(name = "calendar")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder
    public CalendarEvent(Part part, LocalDate startDate, LocalDate endDate, String content) {
        this.part = part;
        this.startDate = startDate;
        this.endDate = endDate;
        this.content = content;
    }

    public void update(LocalDate startDate, LocalDate endDate, String content) {
        if (startDate != null)
            this.startDate = startDate;
        if (endDate != null)
            this.endDate = endDate;
        if (content != null)
            this.content = content;
    }
}
