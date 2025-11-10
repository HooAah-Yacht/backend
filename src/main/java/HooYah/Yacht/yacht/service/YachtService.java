package HooYah.Yacht.yacht.service;

import HooYah.Yacht.yacht.domain.Yacht;
import HooYah.Yacht.yacht.dto.YachtDto;
import HooYah.Yacht.yacht.repository.YachtRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ERD 기준 YachtService
 * Yacht는 id, name만 포함하므로 간소화된 로직
 */
@Service
@Transactional(readOnly = true)
public class YachtService {

    private final YachtRepository yachtRepository;

    public YachtService(YachtRepository yachtRepository) {
        this.yachtRepository = yachtRepository;
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
                .build();
        return yachtRepository.save(y).getId();
    }

    @Transactional
    public void update(Long id, YachtDto.UpdateRequest req) {
        Yacht y = yachtRepository.findById(id).orElseThrow(() -> new RuntimeException("Yacht not found"));
        if (req.getName() != null) y.setName(req.getName());
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

