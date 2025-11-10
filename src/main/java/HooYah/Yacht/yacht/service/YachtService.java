package HooYah.Yacht.yacht.service;

import HooYah.Yacht.yacht.domain.Yacht;
import HooYah.Yacht.yacht.dto.YachtDto;
import HooYah.Yacht.yacht.repository.YachtRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class YachtService {

    private final YachtRepository yachtRepository;

    public YachtService(YachtRepository yachtRepository) {
        this.yachtRepository = yachtRepository;
    }

    public Page<YachtDto.Response> list(String location, Integer minCapacity, BigDecimal maxPrice, Pageable pageable) {
    Specification<Yacht> spec = (root, query, cb) -> cb.conjunction();
        if (location != null && !location.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%"));
        }
        if (minCapacity != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("capacity"), minCapacity));
        }
        if (maxPrice != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("pricePerHour"), maxPrice));
        }

        Page<Yacht> page = yachtRepository.findAll(spec, pageable);
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
                .description(req.getDescription())
                .capacity(req.getCapacity())
                .pricePerHour(req.getPricePerHour())
                .location(req.getLocation())
                .available(Boolean.TRUE)
                .build();
        return yachtRepository.save(y).getId();
    }

    @Transactional
    public void update(Long id, YachtDto.UpdateRequest req) {
        Yacht y = yachtRepository.findById(id).orElseThrow(() -> new RuntimeException("Yacht not found"));
        if (req.getName() != null) y.setName(req.getName());
        if (req.getDescription() != null) y.setDescription(req.getDescription());
        if (req.getCapacity() != null) y.setCapacity(req.getCapacity());
        if (req.getPricePerHour() != null) y.setPricePerHour(req.getPricePerHour());
        if (req.getLocation() != null) y.setLocation(req.getLocation());
        if (req.getAvailable() != null) y.setAvailable(req.getAvailable());
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
                y.getDescription(),
                y.getCapacity(),
                y.getPricePerHour(),
                y.getLocation(),
                y.getAvailable(),
                y.getThumbnailPath()
        );
    }
}
