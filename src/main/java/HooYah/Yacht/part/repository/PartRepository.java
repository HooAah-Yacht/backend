package HooYah.Yacht.part.repository;

import HooYah.Yacht.part.domain.Part;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {

    // 특정 요트의 부품 목록 조회
    Page<Part> findByYachtId(Long yachtId, Pageable pageable);

    // 재고 부족 부품 조회 (quantity가 특정 임계값 이하)
    List<Part> findByYachtIdAndQuantityLessThan(Long yachtId, Integer threshold);

    // 부품명 검색
    List<Part> findByYachtIdAndNameContaining(Long yachtId, String keyword);
}
