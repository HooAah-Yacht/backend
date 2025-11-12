package HooYah.Yacht.calendar.service;

import HooYah.Yacht.calendar.domain.Calendar;
import HooYah.Yacht.calendar.dto.request.CalendarCreateRequest;
import HooYah.Yacht.calendar.dto.request.CalendarUpdateRequest;
import HooYah.Yacht.calendar.dto.response.CalendarInfo;
import HooYah.Yacht.calendar.repository.CalendarRepository;
import HooYah.Yacht.common.excetion.CustomException;
import HooYah.Yacht.common.excetion.ErrorCode;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {

    private final CalendarRepository calendarRepository;

    @Transactional
    public CalendarInfo createCalendar(CalendarCreateRequest request) {
        validateDateRange(request.getStartDate(), request.getEndDate());

        Calendar calendar = Calendar.builder()
                .partId(request.getPartId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .content(request.getContent())
                .build();

        Calendar saved = calendarRepository.save(calendar);
        return CalendarInfo.from(saved);
    }

    public CalendarInfo getCalendar(Long id) {
        Calendar calendar = getCalendarOrThrow(id);
        return CalendarInfo.from(calendar);
    }

    public List<CalendarInfo> getCalendars(Long partId) {
        List<Calendar> calendars = partId != null
                ? calendarRepository.findAllByPartId(partId)
                : calendarRepository.findAll();

        return calendars.stream()
                .map(CalendarInfo::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public CalendarInfo updateCalendar(Long id, CalendarUpdateRequest request) {
        validateDateRange(request.getStartDate(), request.getEndDate());

        Calendar calendar = getCalendarOrThrow(id);
        calendar.update(request.getPartId(), request.getStartDate(), request.getEndDate(), request.getContent());

        return CalendarInfo.from(calendar);
    }

    @Transactional
    public void deleteCalendar(Long id) {
        Calendar calendar = getCalendarOrThrow(id);
        calendarRepository.delete(calendar);
    }

    private Calendar getCalendarOrThrow(Long id) {
        return calendarRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    private void validateDateRange(OffsetDateTime startDate, OffsetDateTime endDate) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }
}

