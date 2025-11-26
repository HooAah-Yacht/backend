package HooYah.Yacht.calendar.repository;

import HooYah.Yacht.calendar.domain.Calendar;
import HooYah.Yacht.calendar.domain.CalendarType;
import HooYah.Yacht.part.domain.Part;
import HooYah.Yacht.yacht.domain.Yacht;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findAllByPartId(Long partId);

    Optional<Calendar> findByPartAndTypeAndByUserFalse(Part part, CalendarType type);

    List<Calendar> findByPartAndType(Part part, CalendarType type);

    List<Calendar> findByYacht(Yacht yacht);
}

