package HooYah.Yacht.user.dto.request;

import HooYah.Yacht.user.domain.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RegisterDto {

    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String name;

    private String token;

    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .name(name)
                .token(token)
                .build();
    }
}
