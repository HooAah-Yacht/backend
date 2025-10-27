package HooYah.Yacht.user.controller;

import HooYah.Yacht.user.JWTUtil;
import HooYah.Yacht.user.domain.User;
import HooYah.Yacht.user.dto.request.LoginDto;
import HooYah.Yacht.user.dto.request.RegisterDto;
import HooYah.Yacht.user.dto.response.UserInfoDto;
import HooYah.Yacht.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/public/user/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDto dto) {
        userService.registerWithEmail(dto);
        return ResponseEntity.ok().body("success");
    }

    @PostMapping("/public/user/login")
    public ResponseEntity login(@RequestBody @Valid LoginDto dto, HttpServletResponse response) {
        User user = userService.login(dto);
        String token = JWTUtil.generateToken(user.getId());
        String cookie = JWTUtil.generateCookie(token);

        response.addHeader("Set-Cookie", cookie);

        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/api/user")
    public ResponseEntity getUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(UserInfoDto.of(user));
    }

}
