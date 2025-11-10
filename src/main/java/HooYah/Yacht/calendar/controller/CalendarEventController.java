package HooYah.Yacht.calendar.controller;

import HooYah.Yacht.calendar.dto.CalendarEventDto;
import HooYah.Yacht.calendar.service.CalendarEventService;
import HooYah.Yacht.common.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CalendarEventController {

    private final CalendarEventService calendarEventService;

    // 기간 내 요트 일정 조회 (공개 읽기)
    @GetMapping("/public/yachts/{yachtId}/calendar")
    public ResponseEntity<SuccessResponse<List<CalendarEventDto.Response>>> getEvents(
            @PathVariable Long yachtId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        LocalDateTime startDt = start.atStartOfDay();
        LocalDateTime endDt = end.atTime(LocalTime.MAX);
        List<CalendarEventDto.Response> events = calendarEventService.getEvents(yachtId, startDt, endDt);
        return ResponseEntity.ok(SuccessResponse.of(events));
    }

    // 일정 상세
    @GetMapping("/api/calendar/{id}")
    public ResponseEntity<SuccessResponse<CalendarEventDto.Response>> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(SuccessResponse.of(calendarEventService.getEvent(id)));
    }

    // 일정 생성 (인증 필요)
    @PostMapping("/api/yachts/{yachtId}/calendar")
    public ResponseEntity<SuccessResponse<CalendarEventDto.Response>> createEvent(
            @PathVariable Long yachtId,
            @Valid @RequestBody CalendarEventDto.CreateRequest request) {
        CalendarEventDto.Response created = calendarEventService.createEvent(yachtId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.of(created));
    }

    // 일정 수정 (인증 필요)
    @PutMapping("/api/calendar/{id}")
    public ResponseEntity<SuccessResponse<CalendarEventDto.Response>> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody CalendarEventDto.UpdateRequest request) {
        return ResponseEntity.ok(SuccessResponse.of(calendarEventService.updateEvent(id, request)));
    }

    // 일정 삭제 (인증 필요)
    @DeleteMapping("/api/calendar/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        calendarEventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
