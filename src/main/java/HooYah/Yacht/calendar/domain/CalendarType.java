package HooYah.Yacht.calendar.domain;

public enum CalendarType {
    MAINTENANCE, // 정비/점검
    RESERVATION, // 예약(향후 예약 도메인 연계)
    BLOCKED, // 차단(비공개 일정/사용불가)
    EVENT // 기타 이벤트
}
