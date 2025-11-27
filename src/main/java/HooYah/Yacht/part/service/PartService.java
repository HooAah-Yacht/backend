package HooYah.Yacht.part.service;

import HooYah.Yacht.part.domain.Part;
import HooYah.Yacht.repair.domain.Repair;
import HooYah.Yacht.part.dto.request.AddPartDto;
import HooYah.Yacht.part.dto.response.PartDto;
import HooYah.Yacht.part.dto.request.UpdatePartDto;
import HooYah.Yacht.part.repository.PartPort;
import HooYah.Yacht.part.repository.PartRepository;
import HooYah.Yacht.repair.repository.RepairPort;
import HooYah.Yacht.user.domain.User;
import HooYah.Yacht.user.repository.YachtUserPort;
import HooYah.Yacht.yacht.domain.Yacht;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartService {

    private final YachtUserPort yachtUserPort;
    private final PartRepository partRepository;
    private final PartPort partPort;
    private final RepairPort repairPort;

    public List<PartDto> getParListByYacht(Long yachtId, User user) {
        Yacht yacht = yachtUserPort.findYacht(yachtId, user.getId());
        List<Part> partList = partRepository.findPartListByYacht(yachtId);
        return partList.stream().map(part -> {
            PartDto dto = PartDto.of(part);
            // 최신 정비일(Repair 테이블 기준) 조회
            Optional<Repair> latestRepair = repairPort.findLastRepair(part);
            LocalDate lastRepairDate = latestRepair.map(Repair::getRepairDate).orElse(null);
            dto.setLastRepairDate(lastRepairDate);
            // 다음 정비 예정일 계산 (interval 기반)
            LocalDate nextRepairDate = (lastRepairDate != null) ? part.nextRepairDate(lastRepairDate) : null;
            dto.setNextRepairDate(nextRepairDate);
            return dto;
        }).toList();
    }

    @Transactional
    public void addPart(AddPartDto dto, User user) {
        Yacht yacht = yachtUserPort.findYacht(dto.getYachtId(), user.getId());

        // 기본값 적용: 최근정비일 미입력 시 now(), interval 미입력 시 12개월
        LocalDate latestMaintenanceDate = dto.getLatestMaintenanceDate();
        if (latestMaintenanceDate == null) {
            latestMaintenanceDate = LocalDate.now();
        }

        Long interval = dto.getInterval();
        if (interval == null) {
            interval = 12L;
        }

        Part newPart = Part
                .builder()
                .yacht(yacht)
                .name(dto.getName())
                .manufacturer(dto.getManufacturer())
                .model(dto.getModel())
                .interval(interval)
                .latestMaintenanceDate(latestMaintenanceDate)
                .build();
        partRepository.save(newPart);

        // TODO: 캘린더/알림 생성 로직 연결 (nextRepairDate 기준으로 스케줄 생성)
    }

    @Transactional
    public void updatePart(UpdatePartDto dto, User user) {
        Part part = partPort.findPart(dto.getId());
        yachtUserPort.validateYachtUser(part.getYacht(), user.getId());

        part.update(dto.getName(), dto.getManufacturer(), dto.getModel(), dto.getInterval(),
                dto.getLatestMaintenanceDate());

        if (dto.getInterval() != null) {
            updateCalenderAndAlarm(part);
        }
    }

    @Transactional
    public void deletePart(Long id, User user) {
        Part part = partPort.findPart(id);
        yachtUserPort.validateYachtUser(part.getYacht(), user.getId());

        // delete 알림, 켈린더 :: delete by cascade

        partRepository.delete(part);
    }

    private void updateCalenderAndAlarm(Part part) {
        if (part.getInterval() == null)
            return; // part에 interval정보가 없다면 알림 켈린더 생성 / 수정하지 않음

        // 이전 repair 정보가 있는지 확인
        Optional<Repair> lastRepair = repairPort.findLastRepair(part);

        if (lastRepair.isEmpty())
            return; // 정비 이력이 없는 경우 켈린더 알림 생성 / 수정하지 않음

        // 다음 repair date 구함
        LocalDate nextRepairDate = part.nextRepairDate(lastRepair.get().getRepairDate());

        // 켈린더 최신화 (캘린더에 is update column 필요 -> 사용자가 설정한 캘린더는 수정하지 않음)
        // 사용자가 수정했다면 수정하지 않음
        // next repair date가 오늘 이후라면 -> 수정함
        // next repqir date가 오늘 이전이라면 -> 오늘로 캘린더 생성?
        // 알림
        // 이전 알림의 날짜를 수정함 : 다른 로직 없어도 됨 그냥 수정
    }

}
