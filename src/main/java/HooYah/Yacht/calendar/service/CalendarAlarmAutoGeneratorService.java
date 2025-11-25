package HooYah.Yacht.calendar.service;

import HooYah.Yacht.calendar.domain.Calendar;
import HooYah.Yacht.calendar.domain.CalendarType;
import HooYah.Yacht.calendar.repository.CalendarRepository;
import HooYah.Yacht.part.domain.Part;
import HooYah.Yacht.repair.domain.Repair;
import HooYah.Yacht.repair.repository.RepairPort;
import HooYah.Yacht.repair.repository.RepairRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CalendarAlarmAutoGeneratorService {

    private final CalendarRepository calendarRepository;
    private final RepairPort repairPort;

    @Transactional
    public void generate(Part part) {
        if(part == null || part.getInterval() == null)
            return;

        Optional<Repair> lastRepairOpt = repairPort.findLastRepair(part);

        if(lastRepairOpt.isEmpty())
            return;

        OffsetDateTime nextRepairDate = part.nextRepairDate(lastRepairOpt.get().getRepairDate());

        generateCalendar(part, nextRepairDate);
        generateAlarm(part, nextRepairDate);
    }

    // part interval, repair new / update / delete -> update Calendar
        // erase old Calendar / create new Calendar
    private void generateCalendar(Part part, OffsetDateTime nextRepairDate) {
        List<Calendar> oldCalendarList = calendarRepository.findAllByPartId(part.getId());
        oldCalendarList.stream()
                // .filter(c->!c.isByUser())
                .forEach(c->calendarRepository.delete(c));

        calendarRepository.save(Calendar
                .builder()
                .type(CalendarType.PART)
                .completed(false)
                .startDate(nextRepairDate)
                .endDate(nextRepairDate)
                .part(part)
                .yacht(part.getYacht())
                .build());
    }

    private void generateAlarm(Part part, OffsetDateTime nextRepairDate) {

    }

}
