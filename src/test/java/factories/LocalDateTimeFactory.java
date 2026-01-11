package factories;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class LocalDateTimeFactory {
    public static Collection<LocalDateTime> createMultiple(int amount, int year, int month) {
        var dates = new ArrayList<LocalDateTime>();

        for (int i = 0; i < amount; i++) {
            dates.add(create(year, month));
        }

        return dates;
    }

    public static LocalDateTime create(int year, int month) {
        var ym = YearMonth.of(year, month);
        var randomDay = new Random().nextInt(1, ym.lengthOfMonth() + 1);
        return LocalDateTime.of(year, month, randomDay, 0, 0);
    }
}
