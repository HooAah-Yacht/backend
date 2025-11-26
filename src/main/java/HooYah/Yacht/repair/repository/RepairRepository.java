package HooYah.Yacht.repair.repository;

import HooYah.Yacht.repair.domain.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {

    List<Repair> findByUserId(Long userId);

    List<Repair> findByPartId(Long partId);

    /**
     * 특정 부품의 정비 이력 조회 (findByPartId와 동일, 명명 일관성 유지)
     */
    default List<Repair> findRepairListByPart(Long partId) {
        return findByPartId(partId);
    }
}
