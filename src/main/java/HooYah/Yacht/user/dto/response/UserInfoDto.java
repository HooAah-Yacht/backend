package HooYah.Yacht.user.dto.response;

import HooYah.Yacht.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserInfoDto {

    private String email;
    private String name;

    public static UserInfoDto of(User user) {
        UserInfoDto dto = new UserInfoDto();
        dto.email = user.getEmail();
        dto.name = user.getName();
        return dto;
    }

}
