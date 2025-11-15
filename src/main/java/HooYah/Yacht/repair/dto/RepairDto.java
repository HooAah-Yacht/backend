package HooYah.Yacht.repair.dto;

import HooYah.Yacht.repair.domain.Repair;
import HooYah.Yacht.user.dto.response.UserInfoDto;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RepairDto {

    private Long id;
    private OffsetDateTime repairDate;
    private UserInfoDto user;

    public static RepairDto of(Repair repair) {
        RepairDto repairDto = new RepairDto();
        repairDto.id = repair.getId();
        repairDto.repairDate = repair.getRepairDate();
        repairDto.user = UserInfoDto.of(repair.getUser());
        return repairDto;
    }

}
