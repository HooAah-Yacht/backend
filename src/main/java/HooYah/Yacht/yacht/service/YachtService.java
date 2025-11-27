package HooYah.Yacht.yacht.service;

import HooYah.Yacht.common.excetion.CustomException;
import HooYah.Yacht.common.excetion.ErrorCode;
import HooYah.Yacht.part.service.PartService;
import HooYah.Yacht.user.domain.User;
import HooYah.Yacht.user.domain.YachtUser;
import HooYah.Yacht.user.dto.response.UserInfoDto;
import HooYah.Yacht.user.repository.YachtUserPort;
import HooYah.Yacht.user.repository.YachtUserRepository;
import HooYah.Yacht.yacht.domain.Yacht;
import HooYah.Yacht.yacht.dto.request.CreateYachtDto;
import HooYah.Yacht.yacht.dto.request.CreateYachtDto.YachtInfo;
import HooYah.Yacht.yacht.dto.request.UpdateYachtDto;
import HooYah.Yacht.yacht.dto.response.ResponseYachtDto;
import HooYah.Yacht.yacht.repository.YachtRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class YachtService {

    private final YachtRepository yachtRepository;
    private final YachtUserPort yachtUserPort;
    private final YachtUserRepository yachtUserRepository;
    private final PartService partService;

    @Transactional
    public void createYacht (CreateYachtDto dto, User user) {
        Yacht createdYacht = createYacht(dto.getYacht(), user);

        dto.getPartList().forEach(
                part -> partService.addPart(createdYacht.getId(), part, user)
        );
    }

    private Yacht createYacht (YachtInfo yachtInfo, User user) {
        Yacht yacht = yachtRepository.save(Yacht
                .builder()
                .name(yachtInfo.getName())
                .nickName(yachtInfo.getNickName())
                .build()
        );
        yachtUserPort.addUser(yacht, user);
        return yacht;
    }

    @Transactional
    public void updateYacht(User user, UpdateYachtDto dto) {
        Yacht yacht = yachtUserPort.findYacht(dto.getId(), user.getId());
        yacht.updateName(dto.getName());
        yacht.updateNickName(dto.getNickName());
    }

    public void deleteYacht(User user, Long yachtId) {
        Yacht yacht = yachtUserPort.findYacht(yachtId, user.getId()); // throw not found
        yachtRepository.delete(yacht);
    }

    public List<ResponseYachtDto> yachtList(User user) {
        List<Yacht> yachtList = yachtUserPort.findYachtListByUser(user.getId());
        return yachtList.stream().map(ResponseYachtDto::of).toList();
    }

    @Transactional
    public List<UserInfoDto> yachtUserList(Long yachtId, User user) {
        Yacht yacht = yachtUserPort.findYacht(yachtId, user.getId());
        return yacht.getYachtUser()
                .stream()
                .map(YachtUser::getUser)
                .map(UserInfoDto::of)
                .toList();
    }

    public long getYachtCode(Long yachtId, User user) {
        Yacht yacht = yachtUserPort.findYacht(yachtId, user.getId());

        return toHash(yacht.getId());
    }

    @Transactional
    public void inviteYachtByHash(Long code, User user) {
        long yachtId = decodeHash(code);

        Yacht yacht = yachtRepository.findById(yachtId).orElseThrow(
                ()->new CustomException(ErrorCode.NOT_FOUND)
        );

        Optional<Yacht> yachtOptional = yachtUserRepository.findYacht(yacht.getId(), user.getId());
        if (yachtOptional.isPresent())
            throw new CustomException(ErrorCode.CONFLICT);

        yachtUserPort.addUser(yacht, user);
    }

    private long toHash(Long id) {
        return id; // 추후 hash 적용
    }

    private long decodeHash(Long hash) {
        return hash; // 추후 hash 적용
    }

}
