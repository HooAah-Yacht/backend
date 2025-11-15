package HooYah.Yacht.user.repository;

import HooYah.Yacht.common.excetion.CustomException;
import HooYah.Yacht.common.excetion.ErrorCode;
import HooYah.Yacht.part.domain.Part;
import HooYah.Yacht.user.domain.User;
import HooYah.Yacht.user.domain.YachtUser;
import HooYah.Yacht.yacht.domain.Yacht;
import HooYah.Yacht.yacht.repository.YachtRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class YachtUserPort {

    private final YachtUserRepository yachtUserRepository;
    private final YachtRepository yachtRepository;
    private final UserRepository userRepository;

    public Yacht findYacht(Long yachtId, Long userId) {
        return yachtUserRepository.findYacht(yachtId, userId).orElseThrow(
                ()->new CustomException(ErrorCode.NOT_FOUND)
        );
    }

    public void validateYachtUser(Yacht yacht, Long userId) {
        yachtUserRepository.findYacht(yacht.getId(), userId).orElseThrow(
                ()->new CustomException(ErrorCode.CONFLICT)
        );
    }

    public List<Yacht> findYachtListByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new CustomException(ErrorCode.NOT_FOUND)
        );

        return yachtUserRepository.findByUser(user).stream().map(YachtUser::getYacht).toList();
    }

    public List<User> findUserListByYacht(Long yachtId) {
        Yacht yacht = yachtRepository.findById(yachtId).orElseThrow(
                ()-> new CustomException(ErrorCode.NOT_FOUND)
        );

        return yachtUserRepository.findByYacht(yacht).stream().map(YachtUser::getUser).toList();
    }

    public YachtUser addUser(Yacht yacht, User user) {
        return yachtUserRepository.save(YachtUser
                .builder()
                .yacht(yacht)
                .user(user)
                .build()
        );
    }

}
