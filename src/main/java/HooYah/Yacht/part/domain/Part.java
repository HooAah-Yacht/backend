package HooYah.Yacht.part.domain;

import HooYah.Yacht.alarm.domain.Alarm;
import HooYah.Yacht.calendar.domain.Calendar;
import HooYah.Yacht.yacht.domain.Yacht;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "part")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "yachtId")
    private Yacht yacht;

    @Column
    private String name;
    @Column
    private String manufacturer;
    @Column
    private String model;
    @Column(name = "`interval`")
    private Long interval; // 몇달

    @OneToMany(mappedBy = "part")
    private List<Alarm> alarms;

    @OneToMany(mappedBy = "part")
    private List<Calendar> calendars;

    public void update(String name, String manufacturer, String model) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.model = model;
    }

    public void updateInterval(Long interval) {
        this.interval = interval;
    }

    public OffsetDateTime nextRepairDate(OffsetDateTime oldRepairDate) {
        return oldRepairDate.plusMonths(interval);
    }

}
