package HooYah.Yacht.yacht.controller;

import HooYah.Yacht.yacht.dto.YachtDto;
import HooYah.Yacht.yacht.service.YachtService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping
@Validated
public class YachtController {

    private final YachtService yachtService;

    public YachtController(YachtService yachtService) {
        this.yachtService = yachtService;
    }

    @GetMapping("/public/yachts")
    public ResponseEntity<Page<YachtDto.Response>> list(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable p = PageRequest.of(page, size);
        Page<YachtDto.Response> result = yachtService.list(location, minCapacity, maxPrice, p);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/public/yachts/{id}")
    public ResponseEntity<YachtDto.Response> get(@PathVariable Long id) {
        return ResponseEntity.ok(yachtService.get(id));
    }

    @PostMapping("/api/yachts")
    public ResponseEntity<Long> create(@Valid @RequestBody YachtDto.CreateRequest req) {
        Long id = yachtService.create(req);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/api/yachts/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody YachtDto.UpdateRequest req) {
        yachtService.update(id, req);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/yachts/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        yachtService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
