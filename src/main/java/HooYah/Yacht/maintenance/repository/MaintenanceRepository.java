package HooYah.Yacht.maintenance.repository;

import HooYah.Yacht.maintenance.domain.Maintenance;
import HooYah.Yacht.maintenance.domain.MaintenanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    // 특정 요트의 정비 이력 조회
    Page<Maintenance> findByYachtId(Long yachtId, Pageable pageable);

    // 특정 요트의 특정 상태 정비 조회
    List<Maintenance> findByYachtIdAndStatus(Long yachtId, MaintenanceStatus status);

    // 특정 날짜 범위의 정비 조회
    List<Maintenance> findByScheduledDateBetween(LocalDate startDate, LocalDate endDate);

    // 특정 요트의 예정된 정비 조회
    List<Maintenance> findByYachtIdAndStatusOrderByScheduledDateAsc(Long yachtId, MaintenanceStatus status);
}
