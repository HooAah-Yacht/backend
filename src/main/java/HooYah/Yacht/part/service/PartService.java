package HooYah.Yacht.part.service;

import HooYah.Yacht.calendar.service.CalendarAlarmAutoGeneratorService;
import HooYah.Yacht.part.domain.Part;
import HooYah.Yacht.repair.domain.Repair;
import HooYah.Yacht.part.dto.request.AddPartDto;
import HooYah.Yacht.part.dto.response.PartDto;
import HooYah.Yacht.part.dto.request.UpdatePartDto;
import HooYah.Yacht.part.repository.PartPort;
import HooYah.Yacht.part.repository.PartRepository;
import HooYah.Yacht.repair.repository.RepairPort;
import HooYah.Yacht.repair.service.RepairService;
import HooYah.Yacht.user.domain.User;
import HooYah.Yacht.user.repository.YachtUserPort;
import HooYah.Yacht.yacht.domain.Yacht;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
    private final RepairService repairService;
    private final CalendarAlarmAutoGeneratorService calendarAlarmAutoGeneratorService;

    public List<PartDto> getParListByYacht(Long yachtId, User user) {
        Yacht yacht = yachtUserPort.findYacht(yachtId, user.getId());

        List<Part> partList = partRepository.findPartListByYacht(yacht.getId());
        List<Repair> lastRepairList = repairPort.findAllLastRepair(partList);
        Map<Long, Repair> lastRepairMap = lastRepairList.stream().collect(Collectors.toMap(
                repair -> repair.getPart().getId(),
                repair -> repair
        ));

        return partList.stream().map(part -> PartDto.of(part, lastRepairMap.get(part.getId()))).toList();
    }

    @Transactional
    public Part addPart(Long yachtId, AddPartDto dto, User user) {
        Yacht yacht = yachtUserPort.findYacht(yachtId, user.getId());

        Part newPart = Part
                .builder()
                .yacht(yacht)
                .name(dto.getName())
                .manufacturer(dto.getManufacturer())
                .model(dto.getModel())
                .interval(dto.getInterval())
                .build();
        newPart = partRepository.save(newPart);

        if(dto.getLastRepair() != null)
            repairService.addRepair(newPart.getId(), null, dto.getLastRepair(), user);

        return newPart;
    }

    @Transactional
    public void updatePart(UpdatePartDto dto, User user) {
        Part part = partPort.findPart(dto.getId());
        yachtUserPort.validateYachtUser(part.getYacht(), user.getId());

        part.update(dto.getName(), dto.getManufacturer(), dto.getModel());

        if(dto.getInterval() != null) {
            part.updateInterval(dto.getInterval());
            calendarAlarmAutoGeneratorService.generate(part);
        }
    }

    @Transactional
    public void deletePart(Long id, User user) {
        Part part = partPort.findPart(id);
        yachtUserPort.validateYachtUser(part.getYacht(), user.getId());

        // delete 알림, 켈린더 :: delete by cascade

        partRepository.delete(part);
    }

}
