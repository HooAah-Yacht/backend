package HooYah.Yacht.calendar.service;

import HooYah.Yacht.calendar.domain.Calendar;
import HooYah.Yacht.calendar.domain.CalendarType;
import HooYah.Yacht.calendar.dto.request.CalendarCreateRequest;
import HooYah.Yacht.calendar.dto.request.CalendarUpdateRequest;
import HooYah.Yacht.calendar.dto.response.CalendarInfo;
import HooYah.Yacht.calendar.repository.CalendarRepository;
import HooYah.Yacht.common.excetion.CustomException;
import HooYah.Yacht.common.excetion.ErrorCode;
import HooYah.Yacht.part.domain.Part;
import HooYah.Yacht.part.repository.PartRepository;
import HooYah.Yacht.yacht.domain.Yacht;
import HooYah.Yacht.yacht.repository.YachtRepository;
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
    private final PartRepository partRepository;
    private final YachtRepository yachtRepository;

    @Transactional
    public CalendarInfo createCalendar(CalendarCreateRequest request) {
        validateDateRange(request.getStartDate(), request.getEndDate());

        Part part = findPartOrNull(request.getPartId());
        Yacht yacht = findYachtOrNull(request.getYachtId());
        
        boolean completed = Boolean.TRUE.equals(request.getCompleted());
        
        if (request.getType() == CalendarType.SAILING && completed) {
            // TODO: 세일링 완료 체크 후 문제 유무 여부, 정비 이력 등록 로직 추가)
        }
        
        Calendar calendar = Calendar.builder()
                .type(request.getType())
                .part(part)
                .yacht(yacht)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .completed(completed)
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
        Part part = findPartOrNull(request.getPartId());
        Yacht yacht = findYachtOrNull(request.getYachtId());
        calendar.update(
                request.getType(),
                part,
                yacht,
                request.getStartDate(),
                request.getEndDate(),
                request.getCompleted(),
                request.getContent()
        );

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

    private Part findPartOrNull(Long partId) {
        if (partId == null) {
            return null;
        }

        return partRepository.findById(partId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    private Yacht findYachtOrNull(Long yachtId) {
        if (yachtId == null) {
            return null;
        }

        return yachtRepository.findById(yachtId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    private void validateDateRange(OffsetDateTime startDate, OffsetDateTime endDate) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }
}

