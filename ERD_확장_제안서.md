# ERD 확장 제안서

**작성일:** 2025년 11월 10일  
**대상:** HooAah-Yacht 개발팀  
**목적:** PDF 기능명세서 구현을 위한 ERD 필드 확장 제안

---

## 📌 요약

현재 ERD는 최소한의 핵심 필드만 정의되어 있어, PDF 기능명세서에 명시된 요구사항을 구현할 수 없습니다.  
본 문서는 **필수 기능 구현을 위해 추가되어야 할 필드**와 **DDL 마이그레이션 스크립트**를 제시합니다.

---

## 🔴 현황 및 문제점

### 1. Yacht (요트) 테이블

**현재 ERD:** `id`, `name`  
**문제점:**

- 요트 설명, 위치, 가격, 정원 정보 없음 → **목록 필터링 불가**
- 이미지 URL 없음 → **UI 표시 불가**
- 예약 가능 여부 없음 → **예약 시스템 구현 불가**

**영향:** 요트 목록 조회, 상세 조회, 예약 기능 전체 구현 불가능

---

### 2. Part (부품) 테이블

**현재 ERD:** `id`, `yacht_id`, `name`, `manufacturer`, `model`, `interval`  
**문제점:**

- 재고 수량, 단가, 공급업체 정보 없음 → **재고 관리 불가**
- 마지막 교체일 없음 → **교체 알림 계산 불가**

**영향:** 부품 재고 관리, 비용 집계, 자동 알림 기능 구현 불가능

---

### 3. Repair (정비) 테이블

**현재 ERD:** `id`, `user_id`, `part_id`, `repair_date`  
**문제점:**

- 정비 대상 요트(yacht_id) 없음 → **요트별 정비 이력 조회 불가**
- 비용 정보 없음 → **정비 비용 통계 불가**
- 정비 유형, 상태, 설명 없음 → **상세 관리 불가**

**영향:** 정비 이력 관리, 비용 통계, 예약 일정 관리 불가능

---

### 4. Calendar (캘린더) 테이블

**현재 ERD:** `id`, `part_id`, `start_date`, `end_date`, `content`  
**문제점:**

- 일정 제목 없음 (content만으로 부족)
- 요트별 일정 조회 불가 (yacht_id 없음)
- 일정 유형 구분 없음

**영향:** 요트 예약 일정, 정비 일정 통합 관리 불가

---

### 5. User (사용자) 테이블

**현재 ERD:** `id`, `email`, `password`, `name`, `social_id`  
**문제점:**

- 권한(role) 정보 없음 → **관리자/일반 유저 구분 불가**
- 연락처 없음 → **예약 알림 발송 불가**
- 프로필 이미지 없음

---

### 6. 누락된 엔티티

**Reservation (예약)** 테이블이 ERD에 없음  
→ **예약 시스템 전체 구현 불가**

---

## ✅ 해결 방안: ERD 확장

### 우선순위 분류

#### 🔴 우선순위 1 (필수 - 즉시 추가)

- Yacht: `description`, `location`, `capacity`, `price_per_hour`, `available`
- Part: `quantity`, `unit_price`, `supplier`
- Repair: `yacht_id`, `cost`, `repair_type`, `description`, `status`
- Reservation: **신규 테이블 생성**

#### 🟡 우선순위 2 (권장 - 2주 내 추가)

- Yacht: `thumbnail_url`, `owner_id`, `created_at`, `updated_at`
- Part: `last_replaced_date`, `part_number`, `reorder_level`
- Repair: `technician`, `scheduled_date`, `completed_date`
- Calendar: `title`, `event_type`, `yacht_id`, `all_day`
- User: `role`, `phone_number`, `profile_image_url`

#### 🟢 우선순위 3 (선택 - 향후 검토)

- Part: `note`, `warranty_expiry`
- YachtUser: `role`, `assigned_at`, `permissions`
- User: `last_login_at`, `is_active`

---

## 📝 DDL 마이그레이션 스크립트

아래 스크립트는 **기존 데이터를 보존하면서** 필드를 추가합니다.

### 1. User 테이블 확장

```sql
-- 우선순위 2: 권한 및 프로필 정보 추가
ALTER TABLE user
ADD COLUMN role VARCHAR(20) DEFAULT 'USER' COMMENT '권한 (USER, ADMIN, OWNER)',
ADD COLUMN phone_number VARCHAR(20) COMMENT '연락처',
ADD COLUMN profile_image_url VARCHAR(500) COMMENT '프로필 이미지 URL',
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '가입일',
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
ADD COLUMN is_active BOOLEAN DEFAULT TRUE COMMENT '계정 활성화 여부';

-- role에 인덱스 추가 (권한별 조회 성능 향상)
CREATE INDEX idx_user_role ON user(role);
```

---

### 2. Yacht 테이블 확장 (🔴 필수)

```sql
-- 우선순위 1: 필수 필드 추가
ALTER TABLE yacht
ADD COLUMN description TEXT COMMENT '요트 설명',
ADD COLUMN location VARCHAR(200) NOT NULL DEFAULT '미정' COMMENT '위치 (필터링용)',
ADD COLUMN capacity INT NOT NULL DEFAULT 0 COMMENT '정원 (필터링용)',
ADD COLUMN price_per_hour DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '시간당 가격',
ADD COLUMN available BOOLEAN DEFAULT TRUE COMMENT '예약 가능 여부';

-- 우선순위 2: 권장 필드 추가
ALTER TABLE yacht
ADD COLUMN thumbnail_url VARCHAR(500) COMMENT '썸네일 이미지 URL',
ADD COLUMN owner_id BIGINT COMMENT '소유자 ID (User FK)',
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일';

-- 외래키 및 인덱스
ALTER TABLE yacht
ADD CONSTRAINT fk_yacht_owner FOREIGN KEY (owner_id) REFERENCES user(id) ON DELETE SET NULL;

CREATE INDEX idx_yacht_location ON yacht(location);
CREATE INDEX idx_yacht_price ON yacht(price_per_hour);
CREATE INDEX idx_yacht_capacity ON yacht(capacity);
CREATE INDEX idx_yacht_available ON yacht(available);

-- 기존 데이터 기본값 처리 (필요 시 수동 업데이트)
-- UPDATE yacht SET location = '서울 마리나', capacity = 10, price_per_hour = 500000.00 WHERE location = '미정';
```

---

### 3. Part 테이블 확장 (🔴 필수)

```sql
-- 우선순위 1: 재고 관리 필수 필드
ALTER TABLE part
ADD COLUMN quantity INT NOT NULL DEFAULT 0 COMMENT '재고 수량',
ADD COLUMN unit_price DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '단가',
ADD COLUMN supplier VARCHAR(200) COMMENT '공급업체';

-- 우선순위 2: 교체 관리 및 알림
ALTER TABLE part
ADD COLUMN part_number VARCHAR(100) UNIQUE COMMENT '부품 번호',
ADD COLUMN last_replaced_date DATE COMMENT '마지막 교체일',
ADD COLUMN reorder_level INT DEFAULT 5 COMMENT '재주문 기준 수량',
ADD COLUMN note TEXT COMMENT '비고',
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- 인덱스
CREATE INDEX idx_part_quantity ON part(quantity);
CREATE INDEX idx_part_supplier ON part(supplier);
```

---

### 4. Repair 테이블 확장 (🔴 필수)

```sql
-- 우선순위 1: 정비 관리 필수 필드
ALTER TABLE repair
ADD COLUMN yacht_id BIGINT NOT NULL COMMENT '정비 대상 요트 ID',
ADD COLUMN cost DECIMAL(12, 2) NOT NULL DEFAULT 0.00 COMMENT '정비 비용',
ADD COLUMN repair_type VARCHAR(50) NOT NULL DEFAULT '일반정비' COMMENT '정비 유형 (정기점검, 긴급수리, 부품교체 등)',
ADD COLUMN description TEXT COMMENT '정비 내용 상세',
ADD COLUMN status VARCHAR(20) DEFAULT '예정' COMMENT '상태 (예정, 진행중, 완료)';

-- 우선순위 2: 일정 및 담당자 관리
ALTER TABLE repair
ADD COLUMN technician VARCHAR(100) COMMENT '정비 담당자',
ADD COLUMN scheduled_date DATE COMMENT '정비 예정일',
ADD COLUMN completed_date DATE COMMENT '정비 완료일',
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- 외래키 및 인덱스
ALTER TABLE repair
ADD CONSTRAINT fk_repair_yacht FOREIGN KEY (yacht_id) REFERENCES yacht(id) ON DELETE CASCADE;

CREATE INDEX idx_repair_yacht ON repair(yacht_id);
CREATE INDEX idx_repair_date ON repair(repair_date);
CREATE INDEX idx_repair_status ON repair(status);
CREATE INDEX idx_repair_cost ON repair(cost);

-- repair_type ENUM 변환 (선택사항)
-- ALTER TABLE repair MODIFY repair_type ENUM('정기점검', '긴급수리', '부품교체', '일반정비') DEFAULT '일반정비';
```

---

### 5. Calendar 테이블 확장

```sql
-- 우선순위 2: 일정 관리 개선
ALTER TABLE calendar
ADD COLUMN title VARCHAR(200) NOT NULL DEFAULT '일정' COMMENT '일정 제목',
ADD COLUMN event_type VARCHAR(50) DEFAULT '기타' COMMENT '일정 유형 (정비, 예약, 점검 등)',
ADD COLUMN yacht_id BIGINT COMMENT '관련 요트 ID (예약/정비 일정용)',
ADD COLUMN all_day BOOLEAN DEFAULT FALSE COMMENT '종일 이벤트 여부',
ADD COLUMN color VARCHAR(20) DEFAULT '#3788d8' COMMENT 'UI 표시용 색상',
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- 외래키 (yacht_id nullable이므로 선택적)
ALTER TABLE calendar
ADD CONSTRAINT fk_calendar_yacht FOREIGN KEY (yacht_id) REFERENCES yacht(id) ON DELETE SET NULL;

CREATE INDEX idx_calendar_yacht ON calendar(yacht_id);
CREATE INDEX idx_calendar_type ON calendar(event_type);
```

---

### 6. Reservation 테이블 생성 (🔴 신규 필수)

```sql
-- 예약 시스템 구현을 위한 신규 테이블
CREATE TABLE reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '예약 ID',
    yacht_id BIGINT NOT NULL COMMENT '예약 요트 ID',
    user_id BIGINT NOT NULL COMMENT '예약자 ID',
    start_datetime TIMESTAMP NOT NULL COMMENT '예약 시작 시간',
    end_datetime TIMESTAMP NOT NULL COMMENT '예약 종료 시간',
    total_price DECIMAL(12, 2) NOT NULL DEFAULT 0.00 COMMENT '총 예약 금액',
    status VARCHAR(20) DEFAULT '대기' COMMENT '예약 상태 (대기, 확정, 취소, 완료)',
    payment_status VARCHAR(20) DEFAULT '미결제' COMMENT '결제 상태 (미결제, 완료, 환불)',
    guest_count INT DEFAULT 1 COMMENT '탑승 인원',
    note TEXT COMMENT '요청사항',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '예약 생성일',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',

    CONSTRAINT fk_reservation_yacht FOREIGN KEY (yacht_id) REFERENCES yacht(id) ON DELETE CASCADE,
    CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,

    -- 예약 시간 검증 제약
    CONSTRAINT chk_reservation_time CHECK (end_datetime > start_datetime),
    CONSTRAINT chk_guest_count CHECK (guest_count > 0)
) COMMENT '요트 예약 정보';

-- 인덱스
CREATE INDEX idx_reservation_yacht ON reservation(yacht_id);
CREATE INDEX idx_reservation_user ON reservation(user_id);
CREATE INDEX idx_reservation_datetime ON reservation(start_datetime, end_datetime);
CREATE INDEX idx_reservation_status ON reservation(status);

-- 예약 중복 방지를 위한 복합 인덱스
CREATE INDEX idx_reservation_overlap ON reservation(yacht_id, start_datetime, end_datetime);
```

---

### 7. YachtUser 테이블 확장

```sql
-- 우선순위 2: 권한 관리 개선
ALTER TABLE yacht_user
ADD COLUMN role VARCHAR(20) DEFAULT 'VIEWER' COMMENT '역할 (OWNER, MANAGER, VIEWER)',
ADD COLUMN assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '배정일',
ADD COLUMN permissions JSON COMMENT '세부 권한 (읽기/쓰기/삭제 등)';

CREATE INDEX idx_yacht_user_role ON yacht_user(role);
```

---

## 📊 마이그레이션 실행 순서

### 1단계: 백업

```sql
-- 전체 DB 백업 (실행 전 필수!)
-- mysqldump -u root -p yacht_db > backup_before_migration_$(date +%Y%m%d).sql
```

### 2단계: 우선순위 1 필드 추가 (필수)

```sql
-- Yacht, Part, Repair 확장
-- Reservation 테이블 생성
-- 위 DDL 스크립트 중 "우선순위 1" 섹션 실행
```

### 3단계: 데이터 검증

```sql
-- 필드 추가 확인
DESCRIBE yacht;
DESCRIBE part;
DESCRIBE repair;
DESCRIBE reservation;

-- 기존 데이터 무결성 확인
SELECT COUNT(*) FROM yacht WHERE location = '미정';  -- 기본값 확인
SELECT COUNT(*) FROM part WHERE quantity < 0;  -- 비정상 데이터 체크
```

### 4단계: 우선순위 2 필드 추가 (권장)

```sql
-- User, Calendar, YachtUser 확장
-- 위 DDL 스크립트 중 "우선순위 2" 섹션 실행
```

### 5단계: 애플리케이션 엔티티 업데이트

```
- Java Entity 클래스에 추가된 필드 반영
- DTO 클래스 업데이트
- Repository 쿼리 메서드 추가
```

---

## ⚠️ 주의사항

### 1. 기존 데이터 처리

- `NOT NULL` 필드는 기본값을 설정했지만, **실제 데이터는 수동 업데이트 필요**
- 예: 요트 위치, 가격 등은 관리자가 직접 입력해야 함

### 2. 외래키 제약

- `yacht.owner_id`, `repair.yacht_id` 등 새로운 FK 추가 시 **참조 무결성 확인 필요**
- 기존 데이터에 해당 ID가 없으면 에러 발생

### 3. 인덱스 성능

- 인덱스 추가로 **INSERT 속도는 느려질 수 있지만**, SELECT 성능은 향상됨
- 대용량 데이터 환경에서는 인덱스 생성 시간 고려

### 4. Enum vs VARCHAR

- `repair_type`, `status` 등은 VARCHAR로 정의했지만, **ENUM으로 변환 권장** (데이터 무결성)
- 단, ENUM 변경 시 ALTER TABLE 필요 → 유연성 떨어짐

---

## 📈 예상 효과

### 기능 구현 가능 여부

| 기능                              | 현재 ERD | 확장 후 |
| --------------------------------- | -------- | ------- |
| 요트 목록 필터링 (위치/가격/정원) | ❌       | ✅      |
| 요트 예약 시스템                  | ❌       | ✅      |
| 부품 재고 관리                    | ❌       | ✅      |
| 정비 비용 통계                    | ❌       | ✅      |
| 정비 일정 알림                    | ❌       | ✅      |
| 권한 기반 접근 제어               | ❌       | ✅      |

### 데이터베이스 크기 증가 예상

- 필드 추가로 테이블당 약 **20~30% 크기 증가**
- 인덱스로 추가 **10~15% 증가**

---

## 🗓️ 실행 계획 (제안)

### Week 1: 우선순위 1 (필수)

- Yacht, Part, Repair 확장
- Reservation 테이블 생성
- 기본 CRUD API 구현

### Week 2: 우선순위 2 (권장)

- User, Calendar 확장
- 권한 시스템 구현
- 알림 기능 추가

### Week 3~4: 우선순위 3 (선택)

- 고급 기능 (통계, 대시보드)
- 성능 최적화
- 테스트 및 배포

---

## 📎 참고자료

- PDF 기능명세서: 요트.pdf, 부품.pdf, 정비.pdf, 사용자.pdf
- 현재 ERD: (팀 공유 ERD 이미지)
- Hibernate DDL Auto 설정: `spring.jpa.hibernate.ddl-auto=validate` 권장 (운영 환경)

---

## ✅ 승인 요청

본 제안서를 검토하시고, 다음 사항을 결정해 주시기 바랍니다:

1. **우선순위 1 필드 즉시 추가 승인 여부**
2. **Reservation 테이블 생성 승인 여부**
3. **마이그레이션 실행 일정 (스테이징/프로덕션)**
4. **추가 논의 필요 항목**

---

**작성자:** GitHub Copilot (Backend Team)  
**문의:** PR #2 - feat: 요트 도메인·정비·부품 스캐폴딩 추가
