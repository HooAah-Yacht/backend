package HooYah.Yacht.part.domain;

import HooYah.Yacht.yacht.domain.Yacht;
import jakarta.persistence.*;
import lombok.*;

/**
 * ERD 기준 Part 엔티티
 * - id, yacht_id, name, manufacturer, model, interval
 * (재고/가격 등 추가 필드는 팀 협의 후 확장 예정)
 */
@Entity
@Table(name = "part")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yacht_id", nullable = false)
    private Yacht yacht;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String manufacturer;

    @Column(length = 100)
    private String model;

    @Column(name = "interval_value")
    private Integer interval;

    @Builder
    public Part(Yacht yacht, String name, String manufacturer, String model, Integer interval) {
        this.yacht = yacht;
        this.name = name;
        this.manufacturer = manufacturer;
        this.model = model;
        this.interval = interval;
    }

    public void update(String name, String manufacturer, String model, Integer interval) {
        if (name != null)
            this.name = name;
        if (manufacturer != null)
            this.manufacturer = manufacturer;
        if (model != null)
            this.model = model;
        if (interval != null)
            this.interval = interval;
    }
}
