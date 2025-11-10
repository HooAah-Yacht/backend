package HooYah.Yacht.maintenance.service;

import HooYah.Yacht.maintenance.domain.Maintenance;
import HooYah.Yacht.maintenance.domain.MaintenanceStatus;
import HooYah.Yacht.maintenance.dto.MaintenanceDto;
import HooYah.Yacht.maintenance.repository.MaintenanceRepository;
import HooYah.Yacht.yacht.domain.Yacht;
import HooYah.Yacht.yacht.repository.YachtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final YachtRepository yachtRepository;

    // 특정 요트의 정비 목록 조회
    public Page<MaintenanceDto.Response> getMaintenancesByYachtId(Long yachtId, Pageable pageable) {
        Page<Maintenance> maintenances = maintenanceRepository.findByYachtId(yachtId, pageable);
        return maintenances.map(MaintenanceDto.Response::from);
    }

    // 정비 상세 조회
    public MaintenanceDto.Response getMaintenanceById(Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance not found with id: " + id));
        return MaintenanceDto.Response.from(maintenance);
    }

    // 정비 일정 생성
    @Transactional
    public MaintenanceDto.Response createMaintenance(Long yachtId, MaintenanceDto.CreateRequest request) {
        Yacht yacht = yachtRepository.findById(yachtId)
                .orElseThrow(() -> new RuntimeException("Yacht not found with id: " + yachtId));

        Maintenance maintenance = Maintenance.builder()
                .yacht(yacht)
                .maintenanceType(request.getMaintenanceType())
                .description(request.getDescription())
                .scheduledDate(request.getScheduledDate())
                .cost(request.getCost())
                .technician(request.getTechnician())
                .status(MaintenanceStatus.SCHEDULED)
                .build();

        Maintenance saved = maintenanceRepository.save(maintenance);
        return MaintenanceDto.Response.from(saved);
    }

    // 정비 수정
    @Transactional
    public MaintenanceDto.Response updateMaintenance(Long id, MaintenanceDto.UpdateRequest request) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance not found with id: " + id));

        maintenance.update(
                request.getMaintenanceType(),
                request.getDescription(),
                request.getScheduledDate(),
                request.getCost(),
                request.getTechnician());

        if (request.getStatus() != null) {
            maintenance.updateStatus(request.getStatus());
        }

        return MaintenanceDto.Response.from(maintenance);
    }

    // 정비 삭제
    @Transactional
    public void deleteMaintenance(Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance not found with id: " + id));
        maintenanceRepository.delete(maintenance);
    }
}
