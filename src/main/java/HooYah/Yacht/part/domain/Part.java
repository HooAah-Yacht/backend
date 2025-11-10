package HooYah.Yacht.part.domain;

import HooYah.Yacht.yacht.domain.Yacht;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(length = 50)
    private String partNumber; // 부품 번호/모델명

    @Column(nullable = false)
    private Integer quantity; // 재고 수량

    @Column(nullable = false)
    private Long unitPrice; // 단가

    @Column(length = 100)
    private String supplier; // 공급업체

    @Column(name = "last_replaced_date")
    private LocalDate lastReplacedDate; // 마지막 교체일

    @Column(columnDefinition = "TEXT")
    private String note;

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
    public Part(Yacht yacht, String name, String partNumber, Integer quantity,
            Long unitPrice, String supplier, LocalDate lastReplacedDate, String note) {
        this.yacht = yacht;
        this.name = name;
        this.partNumber = partNumber;
        this.quantity = quantity != null ? quantity : 0;
        this.unitPrice = unitPrice;
        this.supplier = supplier;
        this.lastReplacedDate = lastReplacedDate;
        this.note = note;
    }

    public void updateQuantity(int delta) {
        this.quantity += delta;
        if (this.quantity < 0) {
            throw new IllegalStateException("재고는 음수가 될 수 없습니다.");
        }
    }

    public void update(String name, String partNumber, Integer quantity, Long unitPrice,
            String supplier, LocalDate lastReplacedDate, String note) {
        if (name != null)
            this.name = name;
        if (partNumber != null)
            this.partNumber = partNumber;
        if (quantity != null)
            this.quantity = quantity;
        if (unitPrice != null)
            this.unitPrice = unitPrice;
        if (supplier != null)
            this.supplier = supplier;
        if (lastReplacedDate != null)
            this.lastReplacedDate = lastReplacedDate;
        if (note != null)
            this.note = note;
    }

    public void recordReplacement(LocalDate replacementDate) {
        this.lastReplacedDate = replacementDate;
    }
}
