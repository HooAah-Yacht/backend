package HooYah.Yacht.calendar.service;

import HooYah.Yacht.calendar.domain.CalendarEvent;
import HooYah.Yacht.calendar.dto.CalendarEventDto;
import HooYah.Yacht.calendar.repository.CalendarEventRepository;
import HooYah.Yacht.yacht.domain.Yacht;
import HooYah.Yacht.yacht.repository.YachtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarEventService {

    private final CalendarEventRepository calendarEventRepository;
    private final YachtRepository yachtRepository;

    // 기간 내 일정 조회
    public List<CalendarEventDto.Response> getEvents(Long yachtId, LocalDateTime start, LocalDateTime end) {
        List<CalendarEvent> events = calendarEventRepository.findOverlapping(yachtId, start, end);
        return events.stream().map(CalendarEventDto.Response::from).toList();
    }

    // 일정 단건 조회
    public CalendarEventDto.Response getEvent(Long id) {
        CalendarEvent event = calendarEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CalendarEvent not found id=" + id));
        return CalendarEventDto.Response.from(event);
    }

    // 일정 생성
    @Transactional
    public CalendarEventDto.Response createEvent(Long yachtId, CalendarEventDto.CreateRequest request) {
        Yacht yacht = yachtRepository.findById(yachtId)
                .orElseThrow(() -> new RuntimeException("Yacht not found id=" + yachtId));

        // 기본값 설정
        boolean allDay = request.getAllDay() != null && request.getAllDay();

        CalendarEvent event = CalendarEvent.builder()
                .yacht(yacht)
                .title(request.getTitle())
                .type(request.getType())
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .allDay(allDay)
                .note(request.getNote())
                .color(request.getColor())
                .build();

        CalendarEvent saved = calendarEventRepository.save(event);
        return CalendarEventDto.Response.from(saved);
    }

    // 일정 수정
    @Transactional
    public CalendarEventDto.Response updateEvent(Long id, CalendarEventDto.UpdateRequest request) {
        CalendarEvent event = calendarEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CalendarEvent not found id=" + id));

        event.update(
                request.getTitle(),
                request.getType(),
                request.getStartAt(),
                request.getEndAt(),
                request.getAllDay(),
                request.getNote(),
                request.getColor());

        return CalendarEventDto.Response.from(event);
    }

    // 일정 삭제
    @Transactional
    public void deleteEvent(Long id) {
        CalendarEvent event = calendarEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CalendarEvent not found id=" + id));
        calendarEventRepository.delete(event);
    }
}
