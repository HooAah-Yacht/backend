package HooYah.Yacht.repair.repository;

import HooYah.Yacht.common.excetion.CustomException;
import HooYah.Yacht.common.excetion.ErrorCode;
import HooYah.Yacht.part.domain.Part;
import HooYah.Yacht.repair.domain.Repair;
import HooYah.Yacht.part.repository.PartRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RepairPort {

    private final RepairRepository repairRepository;
    private final PartRepository partRepository;

    public Optional<Repair> findLastRepair(Long partId) {
        Part part = partRepository.findById(partId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND)
        );
        return findLastRepair(part);
    }

    public Optional<Repair> findLastRepair(Part part) {
        return repairRepository.findByIdOrderByRepairDateDesc(part.getId());
    }

}
