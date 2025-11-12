# 백엔드 필수 수정 사항

## 🎯 프론트엔드 호환을 위한 변경 사항

### 1. Part Entity 수정

**현재 상태**:

```java
@Entity
public class Part {
    private Long id;
    private Yacht yacht;
    private String name;
    private String manufacturer;
    private String model;
    private Integer interval;
    // ❌ latestMaintenanceDate 필드 없음
}
```

**수정 필요**:

```java
@Entity
public class Part {
    // ... 기존 필드

    @Column(name = "latest_maintenance_date")
    private LocalDate latestMaintenanceDate;  // ✨ 추가
}
```

**수정 파일**: `src/main/java/HooYah/Yacht/part/domain/Part.java`

---

### 2. Yacht Entity 수정

**현재 상태**:

```java
@Entity
public class Yacht {
    private Long id;
    private String name;
    // ❌ alias 필드 없음
}
```

**수정 필요**:

```java
@Entity
public class Yacht {
    private Long id;
    private String name;

    @Column(name = "alias", length = 100)
    private String alias;  // ✨ 추가 (요트 별칭)
}
```

**수정 파일**: `src/main/java/HooYah/Yacht/yacht/domain/Yacht.java`

---

### 3. AddPartDto 수정

**현재 상태**:

```java
public class AddPartDto {
    private Long yachtId;
    private String name;
    private String manufacturer;
    private String model;
    private Long interval;  // Long 타입
    // ❌ latestMaintenanceDate 없음
}
```

**수정 필요**:

```java
public class AddPartDto {
    private Long yachtId;
    private String name;
    private String manufacturer;
    private String model;
    private Integer interval;  // Long → Integer

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate latestMaintenanceDate;  // ✨ 추가
}
```

**수정 파일**: `src/main/java/HooYah/Yacht/part/dto/request/AddPartDto.java`

---

### 4. 데이터베이스 스키마 변경

**SQL Migration**:

```sql
-- Part 테이블에 latest_maintenance_date 컬럼 추가
ALTER TABLE part
ADD COLUMN latest_maintenance_date DATE NULL
COMMENT '최근 정비일';

-- Yacht 테이블에 alias 컬럼 추가
ALTER TABLE yacht
ADD COLUMN alias VARCHAR(100) NULL
COMMENT '요트 별칭';

-- interval_value 컬럼 타입 확인 (BIGINT → INT 변경 필요 시)
-- ALTER TABLE part MODIFY COLUMN interval_value INT NULL;
```

---

## 🚀 구현 가이드

### Step 1: Entity 수정

**`Part.java` 수정**:

```java
package HooYah.Yacht.part.domain;

import HooYah.Yacht.yacht.domain.Yacht;
import jakarta.persistence.*;
import java.time.LocalDate;  // ✨ 추가
import lombok.*;

@Entity
@Table(name = "part")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yacht_id", nullable = false)
    private Yacht yacht;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String manufacturer;

    @Column(length = 100)
    private String model;

    @Column(name = "interval_value")
    private Integer interval;

    @Column(name = "latest_maintenance_date")
    private LocalDate latestMaintenanceDate;  // ✨ 추가

    @Builder
    public Part(Yacht yacht, String name, String manufacturer, String model,
                Integer interval, LocalDate latestMaintenanceDate) {  // ✨ 파라미터 추가
        this.yacht = yacht;
        this.name = name;
        this.manufacturer = manufacturer;
        this.model = model;
        this.interval = interval;
        this.latestMaintenanceDate = latestMaintenanceDate;  // ✨ 추가
    }

    public void update(String name, String manufacturer, String model,
                      Integer interval, LocalDate latestMaintenanceDate) {  // ✨ 파라미터 추가
        if (name != null)
            this.name = name;
        if (manufacturer != null)
            this.manufacturer = manufacturer;
        if (model != null)
            this.model = model;
        if (interval != null)
            this.interval = interval;
        if (latestMaintenanceDate != null)
            this.latestMaintenanceDate = latestMaintenanceDate;  // ✨ 추가
    }
}
```

**`Yacht.java` 수정**:

```java
package HooYah.Yacht.yacht.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "yacht")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Yacht {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 100)
    private String alias;  // ✨ 추가
}
```

---

### Step 2: DTO 수정

**`AddPartDto.java` 수정**:

```java
package HooYah.Yacht.part.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;  // ✨ 추가
import java.time.LocalDate;  // ✨ 추가
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AddPartDto {

    private Long yachtId;
    private String name;
    private String manufacturer;
    private String model;
    private Integer interval;  // Long → Integer

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate latestMaintenanceDate;  // ✨ 추가
}
```

**`UpdatePartDto.java` 수정** (동일하게 적용):

```java
package HooYah.Yacht.part.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdatePartDto {

    private Long id;
    private String name;
    private String manufacturer;
    private String model;
    private Integer interval;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate latestMaintenanceDate;  // ✨ 추가
}
```

**`CreateYachtDto.java` 확인/수정**:

```java
package HooYah.Yacht.yacht.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreateYachtDto {

    private String name;
    private String alias;  // ✨ alias 필드 있는지 확인, 없으면 추가
}
```

---

### Step 3: Service 수정

**`PartService.java` 수정**:

```java
@Service
@RequiredArgsConstructor
public class PartService {

    private final PartRepository partRepository;
    private final YachtRepository yachtRepository;

    public void addPart(AddPartDto dto, User user) {
        Yacht yacht = yachtRepository.findById(dto.getYachtId())
                .orElseThrow(() -> new CustomException(ErrorCode.YACHT_NOT_FOUND));

        Part part = Part.builder()
                .yacht(yacht)
                .name(dto.getName())
                .manufacturer(dto.getManufacturer())
                .model(dto.getModel())
                .interval(dto.getInterval())
                .latestMaintenanceDate(dto.getLatestMaintenanceDate())  // ✨ 추가
                .build();

        partRepository.save(part);
    }

    public void updatePart(UpdatePartDto dto, User user) {
        Part part = partRepository.findById(dto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.PART_NOT_FOUND));

        part.update(
            dto.getName(),
            dto.getManufacturer(),
            dto.getModel(),
            dto.getInterval(),
            dto.getLatestMaintenanceDate()  // ✨ 추가
        );

        partRepository.save(part);
    }
}
```

---

### Step 4: 통합 API 추가 (선택사항)

**`CreateYachtWithPartsDto.java` 신규 생성**:

```java
package HooYah.Yacht.yacht.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreateYachtWithPartsDto {

    private String yachtName;
    private String yachtAlias;
    private List<PartInfo> parts;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PartInfo {
        private String name;
        private String manufacturer;
        private String model;
        private Integer interval;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate latestMaintenanceDate;
    }
}
```

**`YachtController.java`에 메서드 추가**:

```java
@PostMapping("/api/yacht/register")
public ResponseEntity registerYachtWithParts(
    @RequestBody @Valid CreateYachtWithPartsDto dto,
    @AuthenticationPrincipal User user
) {
    yachtService.createYachtWithParts(dto, user);
    return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "success", null));
}
```

**`YachtService.java`에 메서드 추가**:

```java
@Transactional
public void createYachtWithParts(CreateYachtWithPartsDto dto, User user) {
    // 1. 요트 생성
    Yacht yacht = Yacht.builder()
            .name(dto.getYachtName())
            .alias(dto.getYachtAlias())
            .build();
    yachtRepository.save(yacht);

    // 2. 부품 일괄 등록
    for (CreateYachtWithPartsDto.PartInfo partInfo : dto.getParts()) {
        Part part = Part.builder()
                .yacht(yacht)
                .name(partInfo.getName())
                .manufacturer(partInfo.getManufacturer())
                .model(partInfo.getModel())
                .interval(partInfo.getInterval())
                .latestMaintenanceDate(partInfo.getLatestMaintenanceDate())
                .build();
        partRepository.save(part);
    }
}
```

---

## ✅ 체크리스트

- [ ] `Part.java`에 `latestMaintenanceDate` 필드 추가
- [ ] `Yacht.java`에 `alias` 필드 추가
- [ ] `AddPartDto.java` 수정
- [ ] `UpdatePartDto.java` 수정
- [ ] `CreateYachtDto.java` 확인/수정
- [ ] `PartService.java` 수정
- [ ] 데이터베이스 스키마 변경 (ALTER TABLE)
- [ ] 통합 API 추가 (선택)
- [ ] 테스트 코드 작성
- [ ] Postman/curl로 API 테스트

---

## 🧪 테스트 예시

```bash
# 요트 + 부품 등록
curl -X POST http://localhost:8080/api/yacht/register \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "yachtName": "Farr 40",
    "yachtAlias": "내 Farr 40",
    "parts": [
      {
        "name": "Impeller",
        "manufacturer": "Yamaha",
        "model": "6CE-44352-00",
        "latestMaintenanceDate": "2024-03-02",
        "interval": 12
      }
    ]
  }'
```

---

**우선순위**: 🔴 높음 (프론트엔드와 호환 불가)  
**예상 작업 시간**: 1-2시간  
**영향 범위**: Part, Yacht Entity, DTO, Service
