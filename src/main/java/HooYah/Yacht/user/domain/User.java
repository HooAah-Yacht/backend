package HooYah.Yacht.user.domain;

import HooYah.Yacht.common.excetion.CustomException;
import HooYah.Yacht.common.excetion.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Column(name = "social_id")
    private String socialId;

    @Column(name = "fcm_token", length = 500)
    private String fcmToken;

    public void updatePassword(String newPassword, PasswordEncoder passwordEncoder) {
        if (passwordEncoder.matches(newPassword, password)) {
        }
        this.password = newPassword;
    }

    public void login(String email, String password, PasswordEncoder passwordEncoder) {
        if (passwordEncoder.matches(password, this.password) && email.equals(this.email))
            return;

        throw new CustomException(ErrorCode.BAD_REQUEST);
    }

    /**
     * FCM 토큰 업데이트
     * @param fcmToken Firebase Cloud Messaging 토큰
     */
    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    @Builder
    public User(String email, String password, String name, String socialId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.socialId = socialId;
    }

}
