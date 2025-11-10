package HooYah.Yacht.part.service;

import HooYah.Yacht.part.domain.Part;
import HooYah.Yacht.part.dto.PartDto;
import HooYah.Yacht.part.repository.PartRepository;
import HooYah.Yacht.yacht.domain.Yacht;
import HooYah.Yacht.yacht.repository.YachtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartService {

    private final PartRepository partRepository;
    private final YachtRepository yachtRepository;

    // 특정 요트의 부품 목록 조회
    public Page<PartDto.Response> getPartsByYachtId(Long yachtId, Pageable pageable) {
        Page<Part> parts = partRepository.findByYachtId(yachtId, pageable);
        return parts.map(PartDto.Response::from);
    }

    // 부품 상세 조회
    public PartDto.Response getPartById(Long id) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part not found id=" + id));
        return PartDto.Response.from(part);
    }

    // 부품 등록
    @Transactional
    public PartDto.Response createPart(Long yachtId, PartDto.CreateRequest request) {
        Yacht yacht = yachtRepository.findById(yachtId)
                .orElseThrow(() -> new RuntimeException("Yacht not found id=" + yachtId));

        Part part = Part.builder()
                .yacht(yacht)
                .name(request.getName())
                .partNumber(request.getPartNumber())
                .quantity(request.getQuantity() != null ? request.getQuantity() : 0)
                .unitPrice(request.getUnitPrice())
                .supplier(request.getSupplier())
                .lastReplacedDate(request.getLastReplacedDate())
                .note(request.getNote())
                .build();

        Part saved = partRepository.save(part);
        return PartDto.Response.from(saved);
    }

    // 부품 수정
    @Transactional
    public PartDto.Response updatePart(Long id, PartDto.UpdateRequest request) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part not found id=" + id));

        part.update(
                request.getName(),
                request.getPartNumber(),
                request.getQuantity(),
                request.getUnitPrice(),
                request.getSupplier(),
                request.getLastReplacedDate(),
                request.getNote());

        return PartDto.Response.from(part);
    }

    // 재고 수량 증감
    @Transactional
    public PartDto.Response updateQuantity(Long id, Integer delta) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part not found id=" + id));
        part.updateQuantity(delta);
        return PartDto.Response.from(part);
    }

    // 교체 기록 (교체일 업데이트)
    @Transactional
    public PartDto.Response recordReplacement(Long id, LocalDate replacementDate) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part not found id=" + id));
        part.recordReplacement(replacementDate);
        return PartDto.Response.from(part);
    }

    // 부품 삭제
    @Transactional
    public void deletePart(Long id) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part not found id=" + id));
        partRepository.delete(part);
    }

    // 재고 부족 부품 조회
    public List<PartDto.Response> getLowStockParts(Long yachtId, Integer threshold) {
        List<Part> parts = partRepository.findByYachtIdAndQuantityLessThan(yachtId, threshold);
        return parts.stream().map(PartDto.Response::from).toList();
    }
}
