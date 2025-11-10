package HooYah.Yacht.part.controller;

import HooYah.Yacht.common.SuccessResponse;
import HooYah.Yacht.part.dto.PartDto;
import HooYah.Yacht.part.service.PartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PartController {

    private final PartService partService;

    // 특정 요트의 부품 목록 조회
    @GetMapping("/api/yachts/{yachtId}/parts")
    public ResponseEntity<SuccessResponse> getPartsByYacht(
            @PathVariable Long yachtId,
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<PartDto.Response> parts = partService.getPartsByYachtId(yachtId, pageable);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "부품 목록 조회 성공", parts));
    }

    // 부품 상세 조회
    @GetMapping("/api/parts/{id}")
    public ResponseEntity<SuccessResponse> getPartById(@PathVariable Long id) {
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "부품 상세 조회 성공", partService.getPartById(id)));
    }

    // 부품 등록
    @PostMapping("/api/yachts/{yachtId}/parts")
    public ResponseEntity<SuccessResponse> createPart(
            @PathVariable Long yachtId,
            @Valid @RequestBody PartDto.CreateRequest request) {
        PartDto.Response created = partService.createPart(yachtId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse(HttpStatus.CREATED, "부품 등록 성공", created));
    }

    // 부품 수정
    @PutMapping("/api/parts/{id}")
    public ResponseEntity<SuccessResponse> updatePart(
            @PathVariable Long id,
            @Valid @RequestBody PartDto.UpdateRequest request) {
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "부품 수정 성공", partService.updatePart(id, request)));
    }

    // 재고 수량 증감
    @PatchMapping("/api/parts/{id}/quantity")
    public ResponseEntity<SuccessResponse> updateQuantity(
            @PathVariable Long id,
            @Valid @RequestBody PartDto.QuantityUpdateRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse(HttpStatus.OK, "재고 수량 변경 성공", partService.updateQuantity(id, request.getDelta())));
    }

    // 교체 기록
    @PatchMapping("/api/parts/{id}/replacement")
    public ResponseEntity<SuccessResponse> recordReplacement(
            @PathVariable Long id,
            @RequestParam LocalDate replacementDate) {
        return ResponseEntity
                .ok(new SuccessResponse(HttpStatus.OK, "교체 기록 성공", partService.recordReplacement(id, replacementDate)));
    }

    // 부품 삭제
    @DeleteMapping("/api/parts/{id}")
    public ResponseEntity<Void> deletePart(@PathVariable Long id) {
        partService.deletePart(id);
        return ResponseEntity.noContent().build();
    }

    // 재고 부족 부품 조회
    @GetMapping("/api/yachts/{yachtId}/parts/low-stock")
    public ResponseEntity<SuccessResponse> getLowStockParts(
            @PathVariable Long yachtId,
            @RequestParam(defaultValue = "5") Integer threshold) {
        List<PartDto.Response> parts = partService.getLowStockParts(yachtId, threshold);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "재고 부족 부품 조회 성공", parts));
    }
}
