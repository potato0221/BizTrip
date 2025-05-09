package com.ll.biztrip.standard.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class WeatherTimeUtil {

    private static final String[] BASE_TIMES = {
            "0200", "0500", "0800", "1100", "1400", "1700", "2000", "2300"
    };

    public static BaseDateTime resolveBaseDateTime(LocalDateTime now) {
        for (int i = BASE_TIMES.length - 1; i >= 0; i--) {
            LocalTime base = LocalTime.of(
                    Integer.parseInt(BASE_TIMES[i].substring(0, 2)),
                    Integer.parseInt(BASE_TIMES[i].substring(2))
            );
            if (now.toLocalTime().isAfter(base) || now.toLocalTime().equals(base)) {
                return new BaseDateTime(now.toLocalDate(), BASE_TIMES[i]);
            }
        }

        return new BaseDateTime(now.toLocalDate().minusDays(1), "2300");
    }

    public record BaseDateTime(LocalDate baseDate, String baseTime) {
        public String getBaseDateStr() {
            return baseDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
    }
}
