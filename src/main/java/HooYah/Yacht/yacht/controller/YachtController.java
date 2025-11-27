package HooYah.Yacht.yacht.controller;

import HooYah.Yacht.common.SuccessResponse;
import HooYah.Yacht.part.dto.response.PartDto;
import HooYah.Yacht.user.domain.User;
import HooYah.Yacht.user.dto.response.UserInfoDto;
import HooYah.Yacht.yacht.dto.request.CreateYachtDto;
import HooYah.Yacht.yacht.dto.request.InviteYachtDto;
import HooYah.Yacht.yacht.dto.request.RequestDefaultPartDto;
import HooYah.Yacht.yacht.dto.request.UpdateYachtDto;
import HooYah.Yacht.yacht.dto.response.ResponseYachtDto;
import HooYah.Yacht.yacht.service.YachtDefaultService;
import HooYah.Yacht.yacht.service.YachtService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/yacht")
public class YachtController {

    private final YachtService yachtService;
    private final YachtDefaultService yachtDefaultService;

    @PostMapping
    public ResponseEntity createYacht(@RequestBody @Valid CreateYachtDto dto, @AuthenticationPrincipal User user) {
        yachtService.createYacht(dto, user);
        return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK, "success", null));
    }

    @PutMapping
    public ResponseEntity updateYacht(@RequestBody @Valid UpdateYachtDto dto, @AuthenticationPrincipal User user) {
        yachtService.updateYacht(user, dto);
        return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK, "success", null));
    }

    @GetMapping
    public ResponseEntity getYachtList(@AuthenticationPrincipal User user) {
        List<ResponseYachtDto> yachtList = yachtService.yachtList(user);
        return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK, "success", Map.of("list", yachtList)));
    }

    @GetMapping("/user/{yachtId}")
    public ResponseEntity getYachtUserList(
            @AuthenticationPrincipal User user ,
            @PathVariable("yachtId") Long yachtId
    ) {
        List<UserInfoDto> response = yachtService.yachtUserList(yachtId, user);
        return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK, "success", Map.of("userList", response)));
    }

    @GetMapping("/invite")
    public ResponseEntity getYachtInviteCode(@RequestParam("yachtId") Long yachtId, @AuthenticationPrincipal User user) {
        Long code = yachtService.getYachtCode(yachtId, user);
        return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK, "success", Map.of("code", code)));
    }

    @PostMapping("/invite")
    public ResponseEntity inviteWithCode(@RequestBody @Valid InviteYachtDto dto, @AuthenticationPrincipal User user) {
        yachtService.inviteYachtByHash(dto.getCode(), user);
        return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK, "success", null));
    }

    @PostMapping("/part-list")
    public ResponseEntity getDefaultPartList(@ModelAttribute @Valid RequestDefaultPartDto dto) {
        List<PartDto> partList = yachtDefaultService.getPartList(dto.getName(), dto.getFile());
        return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK, "success", partList));
    }

    @DeleteMapping("/{yachtId}")
    public ResponseEntity deleteYacht(@AuthenticationPrincipal User user, @PathVariable("yachtId") Long yachtId) {
        yachtService.deleteYacht(user, yachtId);
        return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK, "success", null));
    }

}
