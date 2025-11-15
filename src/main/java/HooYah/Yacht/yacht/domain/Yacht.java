package HooYah.Yacht.yacht.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * ERD 기준 Yacht 엔티티 - id, name, alias 포함
 * (추가 필드는 팀 협의 후 확장 예정)
 */
@Entity
@Table(name = "yacht")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Yacht {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 100)
    private String alias;
}
