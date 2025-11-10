package HooYah.Yacht.schedule.domain;

import HooYah.Yacht.part.domain.Part;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * ERD 기준 Schedule 엔티티
 * - id, part_id, date
 */
@Entity
@Table(name = "schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @Column(nullable = false)
    private LocalDate date;

    @Builder
    public Schedule(Part part, LocalDate date) {
        this.part = part;
        this.date = date;
    }

    public void update(LocalDate date) {
        if (date != null) this.date = date;
    }
}
