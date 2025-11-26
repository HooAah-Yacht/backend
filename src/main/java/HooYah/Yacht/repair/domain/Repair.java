package HooYah.Yacht.repair.domain;

import HooYah.Yacht.part.domain.Part;
import HooYah.Yacht.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * ERD 기준 Repair 엔티티
 * - id, user_id, repair_date, part_id
 */
@Entity
@Table(name = "repair")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Repair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "repair_date", nullable = false)
    private LocalDate repairDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @Builder
    public Repair(User user, LocalDate repairDate, Part part) {
        this.user = user;
        this.repairDate = repairDate;
        this.part = part;
    }

    public void update(LocalDate repairDate, Part part) {
        if (repairDate != null)
            this.repairDate = repairDate;
        if (part != null)
            this.part = part;
    }

    /**
     * 정비 날짜만 업데이트
     * @param repairDate 새 정비 날짜
     */
    public void updateRepairDate(LocalDate repairDate) {
        if (repairDate != null) {
            this.repairDate = repairDate;
        }
    }
}
