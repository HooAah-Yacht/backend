package HooYah.Yacht.yacht.service;

import HooYah.Yacht.common.excetion.CustomException;
import HooYah.Yacht.common.excetion.ErrorCode;
import HooYah.Yacht.user.domain.User;
import HooYah.Yacht.user.repository.YachtUserPort;
import HooYah.Yacht.user.repository.YachtUserRepository;
import HooYah.Yacht.yacht.domain.Yacht;
import HooYah.Yacht.yacht.dto.request.CreateYachtDto;
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

    public void createYacht (CreateYachtDto dto, User user) {
        Yacht yacht = yachtRepository.save(Yacht
                .builder()
                .name(dto.getName())
                .build()
        );
        yachtUserPort.addUser(yacht, user);
    }

    @Transactional
    public void updateYacht(User user, UpdateYachtDto dto) {
        Yacht yacht = yachtUserPort.findYacht(dto.getId(), user.getId());
        yacht.updateName(dto.getName());
    }

    public void deleteYacht() {
        // not yet
    }

    public List<ResponseYachtDto> yachtList(User user) {
        List<Yacht> yachtList = yachtUserPort.findYachtListByUser(user.getId());
        return yachtList.stream().map(ResponseYachtDto::of).toList();
    }

    public long getYachtCode(Long yachtId, User user) {
        Yacht yacht = yachtUserPort.findYacht(yachtId, user.getId());

        Optional<Yacht> yachtOptional = yachtUserRepository.findYacht(yacht.getId(), user.getId());
        if (yachtOptional.isPresent())
            throw new CustomException(ErrorCode.CONFLICT);

        return toHash(yacht.getId());
    }

    @Transactional
    public void inviteYachtByHash(Long code, User user) {
        long yachtId = decodeHash(code);

        Yacht yacht = yachtRepository.findById(yachtId).orElseThrow(
                ()->new CustomException(ErrorCode.NOT_FOUND)
        );

        yachtUserPort.addUser(yacht, user);
    }

    private long toHash(Long id) {
        return id; // 추후 hash 적용
    }

    private long decodeHash(Long hash) {
        return hash; // 추후 hash 적용
    }

}
