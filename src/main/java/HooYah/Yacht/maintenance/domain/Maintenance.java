package HooYah.Yacht.maintenance.domain;

import HooYah.Yacht.yacht.domain.Yacht;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yacht_id", nullable = false)
    private Yacht yacht;

    @Column(name = "maintenance_type", nullable = false, length = 50)
    private String maintenanceType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDate scheduledDate;

    @Column(name = "completed_date")
    private LocalDate completedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MaintenanceStatus status;

    @Column(nullable = false)
    private Long cost;

    @Column(length = 100)
    private String technician;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = MaintenanceStatus.SCHEDULED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Builder
    public Maintenance(Yacht yacht, String maintenanceType, String description,
            LocalDate scheduledDate, LocalDate completedDate,
            MaintenanceStatus status, Long cost, String technician) {
        this.yacht = yacht;
        this.maintenanceType = maintenanceType;
        this.description = description;
        this.scheduledDate = scheduledDate;
        this.completedDate = completedDate;
        this.status = status != null ? status : MaintenanceStatus.SCHEDULED;
        this.cost = cost;
        this.technician = technician;
    }

    public void updateStatus(MaintenanceStatus newStatus) {
        this.status = newStatus;
        if (newStatus == MaintenanceStatus.COMPLETED && this.completedDate == null) {
            this.completedDate = LocalDate.now();
        }
    }

    public void update(String maintenanceType, String description, LocalDate scheduledDate,
            Long cost, String technician) {
        if (maintenanceType != null)
            this.maintenanceType = maintenanceType;
        if (description != null)
            this.description = description;
        if (scheduledDate != null)
            this.scheduledDate = scheduledDate;
        if (cost != null)
            this.cost = cost;
        if (technician != null)
            this.technician = technician;
    }
}
