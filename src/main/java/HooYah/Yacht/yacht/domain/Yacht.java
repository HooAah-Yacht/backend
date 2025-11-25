package HooYah.Yacht.yacht.domain;

import HooYah.Yacht.alarm.domain.Alarm;
import HooYah.Yacht.part.domain.Part;
import HooYah.Yacht.user.domain.YachtUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "yacht")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Yacht {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String nickName;

    @OneToMany(mappedBy = "yacht", fetch = FetchType.LAZY)
    private List<YachtUser> yachtUser;

    @OneToMany(mappedBy = "yacht")
    private List<Part> parts;

    public void updateName(String name) {
        if(name != null && !name.isEmpty())
            this.name = name;
    }

    public void updateNickName(String nickName) {
        if(nickName != null && !nickName.isEmpty())
            this.nickName = nickName;
    }

}
