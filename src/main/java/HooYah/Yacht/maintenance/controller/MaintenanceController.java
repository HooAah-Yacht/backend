package HooYah.Yacht.maintenance.controller;

import HooYah.Yacht.common.SuccessResponse;
import HooYah.Yacht.maintenance.dto.MaintenanceDto;
import HooYah.Yacht.maintenance.service.MaintenanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    // 특정 요트의 정비 목록 조회 (공개 - 요트 소유자/관리자가 확인)
    @GetMapping("/api/yachts/{yachtId}/maintenance")
    public ResponseEntity<SuccessResponse<Page<MaintenanceDto.Response>>> getMaintenancesByYacht(
            @PathVariable Long yachtId,
            @PageableDefault(size = 10, sort = "scheduledDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MaintenanceDto.Response> maintenances = maintenanceService.getMaintenancesByYachtId(yachtId, pageable);
        return ResponseEntity.ok(SuccessResponse.of(maintenances));
    }

    // 정비 상세 조회
    @GetMapping("/api/maintenance/{id}")
    public ResponseEntity<SuccessResponse<MaintenanceDto.Response>> getMaintenanceById(
            @PathVariable Long id) {
        MaintenanceDto.Response maintenance = maintenanceService.getMaintenanceById(id);
        return ResponseEntity.ok(SuccessResponse.of(maintenance));
    }

    // 정비 일정 등록 (인증 필요 - 요트 소유자/관리자)
    @PostMapping("/api/yachts/{yachtId}/maintenance")
    public ResponseEntity<SuccessResponse<MaintenanceDto.Response>> createMaintenance(
            @PathVariable Long yachtId,
            @Valid @RequestBody MaintenanceDto.CreateRequest request) {
        MaintenanceDto.Response created = maintenanceService.createMaintenance(yachtId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.of(created));
    }

    // 정비 수정 (인증 필요)
    @PutMapping("/api/maintenance/{id}")
    public ResponseEntity<SuccessResponse<MaintenanceDto.Response>> updateMaintenance(
            @PathVariable Long id,
            @Valid @RequestBody MaintenanceDto.UpdateRequest request) {
        MaintenanceDto.Response updated = maintenanceService.updateMaintenance(id, request);
        return ResponseEntity.ok(SuccessResponse.of(updated));
    }

    // 정비 삭제 (인증 필요)
    @DeleteMapping("/api/maintenance/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long id) {
        maintenanceService.deleteMaintenance(id);
        return ResponseEntity.noContent().build();
    }
}
