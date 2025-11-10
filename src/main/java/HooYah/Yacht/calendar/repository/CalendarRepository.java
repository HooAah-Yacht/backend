package HooYah.Yacht.calendar.repository;

import HooYah.Yacht.calendar.domain.Calendar;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findAllByPartId(Long partId);
}

