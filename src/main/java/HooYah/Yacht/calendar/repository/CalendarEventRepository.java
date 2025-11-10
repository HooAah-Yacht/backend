package HooYah.Yacht.calendar.repository;

import HooYah.Yacht.calendar.domain.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

    // 주어진 기간과 겹치는 일정 조회 (start <= endParam && end >= startParam)
    @Query("SELECT e FROM CalendarEvent e WHERE e.yacht.id = :yachtId AND e.startAt <= :end AND e.endAt >= :start ORDER BY e.startAt ASC")
    List<CalendarEvent> findOverlapping(@Param("yachtId") Long yachtId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
