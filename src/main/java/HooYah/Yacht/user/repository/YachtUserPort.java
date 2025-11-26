package HooYah.Yacht.user.repository;

import HooYah.Yacht.common.excetion.CustomException;
import HooYah.Yacht.common.excetion.ErrorCode;
import HooYah.Yacht.yacht.domain.Yacht;
import HooYah.Yacht.yacht.repository.YachtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Yacht-User Repository Port (Hexagonal Architecture)
 * 요트와 사용자 관계 검증을 위한 포트
 */
@Component
@RequiredArgsConstructor
public class YachtUserPort {

    private final YachtRepository yachtRepository;

    /**
     * 요트 조회 및 사용자 권한 검증
     * @param yachtId 요트 ID
     * @param userId 사용자 ID
     * @return Yacht 엔티티
     * @throws CustomException 요트를 찾을 수 없거나 권한이 없는 경우
     */
    public Yacht findYacht(Long yachtId, Long userId) {
        Yacht yacht = yachtRepository.findById(yachtId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        
        validateYachtUser(yacht, userId);
        
        return yacht;
    }

    /**
     * 사용자가 해당 요트에 접근 권한이 있는지 검증
     * @param yacht 요트 엔티티
     * @param userId 사용자 ID
     * @throws CustomException 권한이 없는 경우
     */
    public void validateYachtUser(Yacht yacht, Long userId) {
        // 요트 소유자인지 확인
        if (!yacht.getUser().getId().equals(userId)) {
            // TODO: yacht_user 테이블에서 참조인 권한도 확인 필요
            // 현재는 소유자만 접근 가능
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}


