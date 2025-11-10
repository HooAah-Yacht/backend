package HooYah.Yacht.schedule.repository;

import HooYah.Yacht.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
    List<Schedule> findByPartId(Long partId);
    
    List<Schedule> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
