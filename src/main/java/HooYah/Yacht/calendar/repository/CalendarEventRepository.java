package HooYah.Yacht.calendar.repository;

import HooYah.Yacht.calendar.domain.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * ERD 기준 Calendar Repository
 */
@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
    
    List<CalendarEvent> findByPartId(Long partId);
    
    List<CalendarEvent> findByStartDateBetween(LocalDate start, LocalDate end);
}
