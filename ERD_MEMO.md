# ERD 변경 요약 메모

목적

- ERDCloud에 가져올 DDL과 함께 팀원들이 빠르게 이해하도록 간단한 메모를 남깁니다.
- 원본 PNG의 엔티티명(Untitled, Untitled2, ...)을 그대로 사용했습니다.

한줄 요약

- 기존 ERD의 테이블/컬럼 구조는 유지하면서 데이터 무결성(Primary Key, Foreign Key), 인덱스, 자동증가(AUTO_INCREMENT)와 일부 유효성(EMAIL UNIQUE 등)을 추가했습니다.

중요 변경점 (간단히)

- Untitled (사용자)

  - `eamil` 오타를 `email`로 수정
  - `id`에 AUTO_INCREMENT PK 추가, `email`에 UNIQUE 추가
  - 코드에 있는 `password` 컬럼을 DDL에 반영

- Untitled2 (요트)

  - `id` AUTO_INCREMENT PK 추가
  - `name`을 NOT NULL로 지정 (요트명은 필수)

- Untitled5 (부품)

  - `id` AUTO_INCREMENT PK, `yacht_id`에 인덱스 추가
  - `yacht_id` → `Untitled2(id)` FK 추가(ON DELETE CASCADE)

- Untitled3 (Yacht_User)

  - `user_id`, `yacht_id` 복합 PK 유지
  - 두 FK 추가(사용자/요트 삭제 시 매핑 자동 제거)

- Untitled4 (정비내역)

  - `id` AUTO_INCREMENT PK 추가
  - `user_id` FK (ON DELETE SET NULL 권장), `part_id` FK (ON DELETE CASCADE)
  - 인덱스 추가로 조회 성능 보강

- Untitled7 (알림/스케줄)

  - 원본의 잘못된 복합 PK(`id`, `part_id`)를 `id` 단일 PK로 정리
  - `part_id` FK 추가

- Untitled8 (캘린더)

  - `id` AUTO_INCREMENT PK, `part_id` FK 추가

- Untitled6
  - 현재 역할 미정(빈 엔티티). 임시로 `id` PK만 생성. 필요 시 Reservation 등으로 확장 가능.

왜 이렇게 했나? (간단한 이유)

- PK/AUTO_INCREMENT: 레코드 식별과 ORM 매핑을 위해 필요합니다.
- FK/ON DELETE 규칙: 부모-자식 관계 변동 시 데이터 정합성 유지(예: 요트 삭제 → 해당 부품/일정 자동 제거).
- 인덱스: 조인/조회 성능을 위해 기본적인 인덱스 추가.
- UNIQUE(email): 회원가입 중복 방지 및 인증 무결성 확보.

주의사항(팀에게 전달할 내용)

- `repairDate` 컬럼 네이밍은 코드(Repair.java)와 DDL 사이에 camelCase 차이가 있습니다. (DB는 보통 `repair_date`로 표준화). 코드 기준으로 유지할지 DB 기준으로 변경할지 팀 정책 결정 필요.
- `Untitled6`의 역할을 빨리 결정하면 예약(Reservation) 등 기능 설계가 쉬워집니다.

다음 권장 액션

1. Untitled6을 Reservation으로 확정할지 결정
2. FK의 ON DELETE 동작(SET NULL vs CASCADE)을 팀 정책에 맞춰 확정
3. 확정 후 DDL을 Flyway 마이그레이션으로 변환하여 배포 준비

참고 파일

- ERD 확장 제안서(자세한 DDL/설명): `ERD_확장_제안서.md`
- 복사해서 바로 붙여넣을 DDL(간단 버전): `ERD_확장_제안서.md` 내 상단 스크립트 참조

작성자: 자동요약 (요청에 따라 생성)
작성일: 2025-11-10
