package HooYah.Yacht.repair.service;

import HooYah.Yacht.common.excetion.CustomException;
import HooYah.Yacht.common.excetion.ErrorCode;
import HooYah.Yacht.part.domain.Part;
import HooYah.Yacht.part.repository.PartPort;
import HooYah.Yacht.repair.domain.Repair;
import HooYah.Yacht.repair.dto.RepairDto;
import HooYah.Yacht.repair.repository.RepairPort;
import HooYah.Yacht.repair.repository.RepairRepository;
import HooYah.Yacht.user.domain.User;
import HooYah.Yacht.user.repository.YachtUserPort;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RepairService {

    private final RepairRepository repairRepository;
    private final PartPort partPort;
    private final YachtUserPort yachtUserPort;
    private final RepairPort repairPort;

    @Transactional
    public List<RepairDto> getRepairListByPart(
            Long partId, User user
    ) {
        Part part = partPort.findPart(partId);
        yachtUserPort.validateYachtUser(part.getYacht(), user.getId());

        List<Repair> repairList = repairRepository.findRepairListByPart(partId);
        return repairList.stream().map(RepairDto::of).toList();
    }

    @Transactional
    public void addRepair(Long partId, OffsetDateTime repairDate, User user) {
        Part part = partPort.findPart(partId);
        yachtUserPort.validateYachtUser(part.getYacht(), user.getId());

        Repair repair = Repair
                .builder()
                .part(part)
                .user(user)
                .repairDate(repairDate)
                .build();

        repairRepository.save(repair);

        // after flush

        updateCalenderAndAlarm(part);
    }

    @Transactional
    public void updateRepair(Long repairId, OffsetDateTime updateDate, User user) {
        Repair repair = repairRepository.findById(repairId).orElseThrow(
                ()->new CustomException(ErrorCode.NOT_FOUND)
        );

        Part part = repair.getPart();
        yachtUserPort.validateYachtUser(part.getYacht(), user.getId());

        repair.updateRepairDate(updateDate);

        updateCalenderAndAlarm(part);
    }

    @Transactional
    public void deleteRepair(Long repairId, User user) {
        Repair repair = repairRepository.findById(repairId).orElseThrow(
                ()->new CustomException(ErrorCode.NOT_FOUND)
        );
        Part part = repair.getPart();
        yachtUserPort.validateYachtUser(part.getYacht(), user.getId());

        repairRepository.delete(repair);

        // after flush

        updateCalenderAndAlarm(part);
    }

    private void updateCalenderAndAlarm(Part part) {
        if (part.getInterval() == null)
            return; // part에 interval정보가 없다면 알림 켈린더 생성 / 수정하지 않음

        // 이전 repair 정보가 있는지 확인
        Optional<Repair> lastRepair = repairPort.findLastRepair(part);

        if(lastRepair.isEmpty())
            return; // 정비 이력이 없는 경우 켈린더 알림 생성 / 수정하지 않음

        // 다음 repair date 구함
        OffsetDateTime nextRepairDate = part.nextRepairDate(lastRepair.get().getRepairDate());

        // 켈린더 최신화 (캘린더에 is update column 필요 -> 사용자가 설정한 캘린더는 수정하지 않음)
        // 사용자가 수정했다면 수정하지 않음
        // next repair date가 오늘 이후라면 -> 수정함
        // next repqir date가 오늘 이전이라면 -> 오늘로 캘린더 생성?
        // 알림
        // 이전 알림의 날짜를 수정함 : 다른 로직 없어도 됨 그냥 수정
    }

}
