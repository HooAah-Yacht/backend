package HooYah.Yacht.repair.repository;

import HooYah.Yacht.repair.domain.Repair;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepairRepository extends JpaRepository<Repair, Long> {
    @Query("select r from Repair r where r.part.id = :partId")
    List<Repair> findRepairListByPart (@Param("partId") Long partId);
    Optional<Repair> findByIdOrderByRepairDateDesc(Long id);

    @Query(value = """
            select 
                t.*
            from
                ( 
                    select Repair.* , ROW_NUMBER() OVER (PARTITION BY Repair.part_id ORDER BY repair_date) last_date
                    from Repair
                ) t
            where t.last_date = 1 """, nativeQuery = true)
    List<Repair> findAllLastRepair(@Param("partIdList") List<Long> partIdList);
}
