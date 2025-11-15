package HooYah.Yacht.yacht.service;

import HooYah.Yacht.part.domain.Part;
import HooYah.Yacht.part.repository.PartRepository;
import HooYah.Yacht.yacht.domain.Yacht;
import HooYah.Yacht.yacht.dto.YachtDto;
import HooYah.Yacht.yacht.dto.request.CreateYachtWithPartsDto;
import HooYah.Yacht.yacht.dto.response.ResponseYachtDto;
import HooYah.Yacht.yacht.repository.YachtRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ERD 기준 YachtService
 * Yacht는 id, name, alias 포함
 */
@Service
@Transactional(readOnly = true)
public class YachtService {

    private final YachtRepository yachtRepository;
    private final PartRepository partRepository;

    public YachtService(YachtRepository yachtRepository, PartRepository partRepository) {
        this.yachtRepository = yachtRepository;
        this.partRepository = partRepository;
    }

    public Page<YachtDto.Response> list(Pageable pageable) {
        Page<Yacht> page = yachtRepository.findAll(pageable);
        return page.map(this::toResponse);
    }

    public YachtDto.Response get(Long id) {
        Yacht y = yachtRepository.findById(id).orElseThrow(() -> new RuntimeException("Yacht not found"));
        return toResponse(y);
    }

    @Transactional
    public Long create(YachtDto.CreateRequest req) {
        Yacht y = Yacht.builder()
                .name(req.getName())
                .alias(req.getAlias())
                .build();
        return yachtRepository.save(y).getId();
    }

    @Transactional
    public ResponseYachtDto createYachtWithParts(CreateYachtWithPartsDto dto) {
        // 1. Yacht 생성
        Yacht yacht = Yacht.builder()
                .name(dto.getYachtName())
                .alias(dto.getYachtAlias())
                .build();
        Yacht savedYacht = yachtRepository.save(yacht);

        // 2. Parts 생성 (if provided)
        if (dto.getParts() != null && !dto.getParts().isEmpty()) {
            dto.getParts().forEach(partDto -> {
                Part part = Part.builder()
                        .yacht(savedYacht)
                        .name(partDto.getName())
                        .manufacturer(partDto.getManufacturer())
                        .model(partDto.getModel())
                        .interval(partDto.getInterval() != null ? partDto.getInterval().intValue() : null)
                        .latestMaintenanceDate(partDto.getLatestMaintenanceDate())
                        .build();
                partRepository.save(part);
            });
        }

        // 3. Response 생성
        return ResponseYachtDto.of(savedYacht);
    }

    @Transactional
    public void update(Long id, YachtDto.UpdateRequest req) {
        Yacht y = yachtRepository.findById(id).orElseThrow(() -> new RuntimeException("Yacht not found"));
        if (req.getName() != null) y.setName(req.getName());
        if (req.getAlias() != null) y.setAlias(req.getAlias());
        yachtRepository.save(y);
    }

    @Transactional
    public void delete(Long id) {
        yachtRepository.deleteById(id);
    }

    private YachtDto.Response toResponse(Yacht y) {
        return new YachtDto.Response(
                y.getId(),
                y.getName(),
                null, // description - ERD에 없음
                null, // capacity - ERD에 없음
                null, // pricePerHour - ERD에 없음
                null, // location - ERD에 없음
                null, // available - ERD에 없음
                null  // thumbnailPath - ERD에 없음
        );
    }
}