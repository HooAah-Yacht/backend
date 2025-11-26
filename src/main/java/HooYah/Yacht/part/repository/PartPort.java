package HooYah.Yacht.part.repository;

import HooYah.Yacht.common.excetion.CustomException;
import HooYah.Yacht.common.excetion.ErrorCode;
import HooYah.Yacht.part.domain.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Part Repository Port (Hexagonal Architecture)
 * Part 엔티티 조회를 위한 포트
 */
@Component
@RequiredArgsConstructor
public class PartPort {

    private final PartRepository partRepository;

    /**
     * Part 조회 (없으면 예외 발생)
     * @param partId 부품 ID
     * @return Part 엔티티
     * @throws CustomException 부품을 찾을 수 없는 경우
     */
    public Part findPart(Long partId) {
        return partRepository.findById(partId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }
}


