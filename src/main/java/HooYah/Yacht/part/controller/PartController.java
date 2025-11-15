package HooYah.Yacht.part.controller;

import HooYah.Yacht.common.SuccessResponse;
import HooYah.Yacht.part.dto.request.AddPartDto;
import HooYah.Yacht.part.dto.response.PartDto;
import HooYah.Yacht.part.dto.request.UpdatePartDto;
import HooYah.Yacht.part.service.PartService;
import HooYah.Yacht.user.domain.User;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/part")
public class PartController {

    private final PartService partService;

    @GetMapping("/{yachtId}")
    public ResponseEntity getPartListByYacht(
            @PathVariable("yachtId") Long yachtId,
            @AuthenticationPrincipal User user
    ) {
        List<PartDto> dtoList = partService.getParListByYacht(yachtId, user);
        return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK, "success", Map.of("partList", dtoList)));
    }

    @PostMapping
    public ResponseEntity addPart(
            @RequestBody @Valid AddPartDto dto,
            @AuthenticationPrincipal User user
    ) {
        partService.addPart(dto.getYachtId(), dto, user);
        return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK, "success", null));
    }

    @PutMapping
    public ResponseEntity updatePart(
            @RequestBody @Valid UpdatePartDto dto,
            @AuthenticationPrincipal User user
    ) {
        partService.updatePart(dto, user);
        return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK, "success", null));
    }

    @DeleteMapping("/{partId}")
    public ResponseEntity deletePart(
            @PathVariable("partId") Long partId,
            @AuthenticationPrincipal User user
    ) {
        partService.deletePart(partId, user);
        return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK, "success", null));
    }

}
