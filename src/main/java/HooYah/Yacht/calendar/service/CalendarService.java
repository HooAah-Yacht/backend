package HooYah.Yacht.calendar.service;

import HooYah.Yacht.calendar.domain.CalendarEvent;
import HooYah.Yacht.calendar.dto.request.CalendarCreateRequest;
import HooYah.Yacht.calendar.dto.request.CalendarUpdateRequest;
import HooYah.Yacht.calendar.dto.response.CalendarInfo;
import HooYah.Yacht.calendar.repository.CalendarEventRepository;
import HooYah.Yacht.common.excetion.CustomException;
import HooYah.Yacht.common.excetion.ErrorCode;
import HooYah.Yacht.part.domain.Part;
import HooYah.Yacht.part.repository.PartRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {

    private final CalendarEventRepository calendarRepository;
    private final PartRepository partRepository;

    @Transactional
    public CalendarInfo createCalendar(CalendarCreateRequest request) {
        validateDateRange(request.getStartDate(), request.getEndDate());

        Part part = partRepository.findById(request.getPartId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        CalendarEvent calendar = CalendarEvent.builder()
                .part(part)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .content(request.getContent())
                .build();

        CalendarEvent saved = calendarRepository.save(calendar);
        return CalendarInfo.from(saved);
    }

    public CalendarInfo getCalendar(Long id) {
        CalendarEvent calendar = getCalendarOrThrow(id);
        return CalendarInfo.from(calendar);
    }

    public List<CalendarInfo> getCalendars(Long partId) {
        List<CalendarEvent> calendars = partId != null
                ? calendarRepository.findByPartId(partId)
                : calendarRepository.findAll();

        return calendars.stream()
                .map(CalendarInfo::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public CalendarInfo updateCalendar(Long id, CalendarUpdateRequest request) {
        validateDateRange(request.getStartDate(), request.getEndDate());

        CalendarEvent calendar = getCalendarOrThrow(id);
        calendar.update(request.getStartDate(), request.getEndDate(), request.getContent());

        return CalendarInfo.from(calendar);
    }

    @Transactional
    public void deleteCalendar(Long id) {
        CalendarEvent calendar = getCalendarOrThrow(id);
        calendarRepository.delete(calendar);
    }

    private CalendarEvent getCalendarOrThrow(Long id) {
        return calendarRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }
}

