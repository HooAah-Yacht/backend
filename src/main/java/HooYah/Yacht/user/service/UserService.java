package HooYah.Yacht.user.service;

import HooYah.Yacht.common.excetion.CustomException;
import HooYah.Yacht.common.excetion.ErrorCode;
import HooYah.Yacht.repair.domain.Repair;
import HooYah.Yacht.repair.repository.RepairRepository;
import HooYah.Yacht.user.domain.User;
import HooYah.Yacht.user.domain.YachtUser;
import HooYah.Yacht.user.dto.request.LoginDto;
import HooYah.Yacht.user.dto.request.RegisterDto;
import HooYah.Yacht.user.repository.UserRepository;
import HooYah.Yacht.user.repository.YachtUserPort;
import HooYah.Yacht.yacht.domain.Yacht;
import HooYah.Yacht.yacht.repository.YachtRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RepairRepository repairRepository;
    private final YachtRepository yachtRepository;

    @Transactional
    public User registerWithEmail(RegisterDto dto) {
        if(userRepository.findByEmail(dto.getEmail()).isPresent())
            throw new CustomException(ErrorCode.CONFLICT);

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = dto.toEntity(encodedPassword);
        return userRepository.save(user);
    }

    @Transactional
    public User login(LoginDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(
                ()->new CustomException(ErrorCode.NOT_FOUND)
        );
        user.login(dto.getEmail(), dto.getPassword(), passwordEncoder);

        return user;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void deleteUser(User user) {
        // User가 생성한 Repair값에 set User null
        List<Repair> repairList = repairRepository.findByUser(user);
        for(Repair repair : repairList)
            repair.setUserNull();

        List<Yacht> yachtList = user.getYachtUsers().stream().map(YachtUser::getYacht).toList();

        userRepository.delete(user);

        // yacht List의 값들 중 User가 없는 yacht 삭제
        for(Yacht yacht : yachtList) {
            if(yacht.getYachtUser().isEmpty())
                yachtRepository.delete(yacht);
        }
    }

}
