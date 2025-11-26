package HooYah.Yacht.repair.repository;

import HooYah.Yacht.part.domain.Part;
import HooYah.Yacht.repair.domain.Repair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Repair Repository Port (Hexagonal Architecture)
 * Repair 엔티티 조회를 위한 포트
 */
@Component
@RequiredArgsConstructor
public class RepairPort {

    private final RepairRepository repairRepository;

    /**
     * 특정 부품의 가장 최근 정비 이력 조회
     * @param part 부품 엔티티
     * @return 가장 최근 정비 이력 (없으면 Optional.empty())
     */
    public Optional<Repair> findLastRepair(Part part) {
        List<Repair> repairList = repairRepository.findByPartId(part.getId());
        
        // 정비 날짜 기준 내림차순 정렬 후 첫 번째 항목 반환
        return repairList.stream()
                .max(Comparator.comparing(Repair::getRepairDate));
    }

    /**
     * 특정 부품의 모든 정비 이력 조회
     * @param partId 부품 ID
     * @return 정비 이력 리스트
     */
    public List<Repair> findRepairListByPart(Long partId) {
        return repairRepository.findByPartId(partId);
    }
}


