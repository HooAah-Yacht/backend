package HooYah.Yacht.yacht.service;

import HooYah.Yacht.part.dto.response.PartDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class YachtDefaultService {

    public List<PartDto> getPartList(String name, List<MultipartFile> files) {
        List<PartDto> partList = getDefaultPartList(name);
        if(files != null && !files.isEmpty()) {
            partList = getAdditionalPartList(partList, files);
        }

        return partList;
    }

    public List<PartDto> getDefaultPartList(String name) {
        // todo : add ai
        return dummyData;
    }

    private List<PartDto> getAdditionalPartList(List<PartDto> defaultPartList, List<MultipartFile> files) {
        // todo : add ai
        return defaultPartList;
    }

    // 나중에 구현 후 삭제
    private List<PartDto> dummyData = List.of(
            PartDto.builder().name("엔진").model("S23").manufacturer("삼성").interval(3L).build() ,
            PartDto.builder().name("모터").model("A5").manufacturer("엘지").interval(1L).build() ,
            PartDto.builder().name("전등").model("구명").manufacturer("기아").interval(10L).build()
    );

}
