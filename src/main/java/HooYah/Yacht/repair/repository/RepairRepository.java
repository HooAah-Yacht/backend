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
    
    @Query("select r from Repair r where r.part.id = :partId order by r.repairDate desc")
    Optional<Repair> findByIdOrderByRepairDateDesc(@Param("partId") Long partId);
}
