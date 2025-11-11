package HooYah.Yacht.repair.controller;

import HooYah.Yacht.common.SuccessResponse;
import HooYah.Yacht.repair.dto.RequestRepairDto;
import HooYah.Yacht.repair.dto.RepairDto;
import HooYah.Yacht.repair.service.RepairService;
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
@RequestMapping("/api/repair")
public class RepairController {

    private final RepairService repairService;

    @GetMapping("{partId}")
    public ResponseEntity getPairList(
            @PathVariable("partId") Long partId,
            @AuthenticationPrincipal User user
    ){
        List<RepairDto> dtoList = repairService.getRepairListByPart(partId, user);
        return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK, "success", Map.of("repairList", dtoList)));
    }

    @PostMapping
    public ResponseEntity addRepair(
            @RequestBody @Valid RequestRepairDto dto,
            @AuthenticationPrincipal User user
    ) {
        repairService.addRepair(dto.getId(), dto.getDate(), user);
        return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK, "success", null));
    }

    @PutMapping
    public ResponseEntity updateRepair(
            @RequestBody @Valid RequestRepairDto dto,
            @AuthenticationPrincipal User user
    ) {
        repairService.updateRepair(dto.getId(), dto.getDate(), user);
        return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK, "success", null));
    }

    @DeleteMapping("/{repairId}")
    public ResponseEntity deleteRepair(
            @PathVariable("repairId") Long repairId,
            @AuthenticationPrincipal User user
    ) {
        repairService.deleteRepair(repairId, user);
        return  ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK, "success", null));
    }

}
