package HooYah.Yacht.yacht.domain;

import HooYah.Yacht.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "yacht_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class YachtUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yacht_id", nullable = false)
    private Yacht yacht;

    @Builder
    public YachtUser(User user, Yacht yacht) {
        this.user = user;
        this.yacht = yacht;
    }
}
