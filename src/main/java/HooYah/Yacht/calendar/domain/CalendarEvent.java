package HooYah.Yacht.calendar.domain;

import HooYah.Yacht.yacht.domain.Yacht;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "calendar_event")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yacht_id", nullable = false)
    private Yacht yacht;

    @Column(nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CalendarType type;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Column(name = "all_day", nullable = false)
    private boolean allDay;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(length = 20)
    private String color; // 프론트 표시 색상(hex/rgb)

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Builder
    public CalendarEvent(Yacht yacht, String title, CalendarType type,
            LocalDateTime startAt, LocalDateTime endAt,
            boolean allDay, String note, String color) {
        this.yacht = yacht;
        this.title = title;
        this.type = type;
        this.startAt = startAt;
        this.endAt = endAt;
        this.allDay = allDay;
        this.note = note;
        this.color = color;
    }

    public void update(String title, CalendarType type, LocalDateTime startAt,
            LocalDateTime endAt, Boolean allDay, String note, String color) {
        if (title != null)
            this.title = title;
        if (type != null)
            this.type = type;
        if (startAt != null)
            this.startAt = startAt;
        if (endAt != null)
            this.endAt = endAt;
        if (allDay != null)
            this.allDay = allDay;
        if (note != null)
            this.note = note;
        if (color != null)
            this.color = color;
    }
}
