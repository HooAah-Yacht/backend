package HooYah.Yacht.alarm.repository;

import HooYah.Yacht.alarm.domain.Alarm;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Query("select a "
            + "from Yacht y "
            + "left join Part p on p.yacht = y "
            + "left join Alarm a on a.part = p "
            + "where y.id in :yachtIds")
    List<Alarm> findAllByYachtIds(@Param("yachtIds") List<Long> yachtIds);

}
