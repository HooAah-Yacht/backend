package HooYah.Yacht.calendar.controller;

import HooYah.Yacht.calendar.dto.request.CalendarCreateRequest;
import HooYah.Yacht.calendar.dto.request.CalendarUpdateRequest;
import HooYah.Yacht.calendar.dto.response.CalendarInfo;
import HooYah.Yacht.calendar.service.CalendarService;
import HooYah.Yacht.common.SuccessResponse;
import HooYah.Yacht.user.domain.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendars")
public class CalendarController {

    private final CalendarService calendarService;

    @PostMapping
    public ResponseEntity<SuccessResponse> createCalendar(@Valid @RequestBody CalendarCreateRequest request,
                                                          @AuthenticationPrincipal User user) {
        CalendarInfo response = calendarService.createCalendar(request, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse(HttpStatus.CREATED, "success", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse> getCalendar(@PathVariable Long id,
                                                        @AuthenticationPrincipal User user) {
        CalendarInfo response = calendarService.getCalendar(id, user);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "success", response));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse> getCalendars(@RequestParam(value = "partId", required = false) Long partId,
                                                         @AuthenticationPrincipal User user) {
        List<CalendarInfo> responses = calendarService.getCalendars(partId, user);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "success", responses));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> updateCalendar(@PathVariable Long id,
                                                          @Valid @RequestBody CalendarUpdateRequest request,
                                                          @AuthenticationPrincipal User user) {
        CalendarInfo response = calendarService.updateCalendar(id, request, user);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "success", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteCalendar(@PathVariable Long id,
                                                          @AuthenticationPrincipal User user) {
        calendarService.deleteCalendar(id, user);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "success", null));
    }
}

