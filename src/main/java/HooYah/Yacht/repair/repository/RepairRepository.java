package HooYah.Yacht.repair.repository;

import HooYah.Yacht.repair.domain.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {

    List<Repair> findByUserId(Long userId);

    List<Repair> findByPartId(Long partId);
}
