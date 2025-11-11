package HooYah.Yacht.part.repository;

import HooYah.Yacht.common.excetion.CustomException;
import HooYah.Yacht.common.excetion.ErrorCode;
import HooYah.Yacht.part.domain.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartPort {

    private final PartRepository partRepository;

    public Part findPart(Long partId) {
        return partRepository.findById(partId).orElseThrow(
                ()->new CustomException(ErrorCode.NOT_FOUND)
        );
    }

}
