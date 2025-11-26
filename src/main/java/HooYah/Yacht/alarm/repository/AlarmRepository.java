package HooYah.Yacht.alarm.repository;

import HooYah.Yacht.alarm.domain.Alarm;
import HooYah.Yacht.part.domain.Part;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Query("select a "
            + "from Alarm a "
            + "left join a.part p "
            + "left join p.yacht y "
            + "where y.id in :yachtIds")
    List<Alarm> findAllByYachtIds(@Param("yachtIds") List<Long> yachtIds);

    void deleteAllByPart(Part part);
}
