package HooYah.Yacht.repair.service;

import HooYah.Yacht.calendar.service.CalendarService;
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
    public void addRepair(Long partId, String content, OffsetDateTime repairDate, User user) {
        Part part = partPort.findPart(partId);
        yachtUserPort.validateYachtUser(part.getYacht(), user.getId());

        Repair repair = Repair
                .builder()
                .part(part)
                .user(user)
                .repairDate(repairDate)
                .content(content)
                .build();

        repairRepository.save(repair);

        updateCalenderAndAlarm(part, repairDate);
    }

    @Transactional
    public void updateRepair(Long repairId, String content, OffsetDateTime updateDate, User user) {
        Repair repair = repairRepository.findById(repairId).orElseThrow(
                ()->new CustomException(ErrorCode.NOT_FOUND)
        );

        Part part = repair.getPart();
        yachtUserPort.validateYachtUser(part.getYacht(), user.getId());

        repair.updateRepairDate(updateDate);
        repair.updateContent(content);

        updateCalenderAndAlarm(part, updateDate);
    }

    @Transactional
    public void deleteRepair(Long repairId, User user) {
        Repair repair = repairRepository.findById(repairId).orElseThrow(
                ()->new CustomException(ErrorCode.NOT_FOUND)
        );
        Part part = repair.getPart();
        yachtUserPort.validateYachtUser(part.getYacht(), user.getId());

        repairRepository.delete(repair);


    }

    private void updateCalenderAndAlarm(Part part, OffsetDateTime repairDate) {
        
        // TODO: 알림 로직 추가
    }

}
