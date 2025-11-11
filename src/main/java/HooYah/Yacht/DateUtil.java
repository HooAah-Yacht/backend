package HooYah.Yacht;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static LocalDate toLocalDate(String dateStr) {
        return LocalDate.parse(dateStr, formatter);
    }
}
