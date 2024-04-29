package org.bits.devops;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        long epochTime = 1714413944;
        String dateTime = convertEpochToDateTime(epochTime);
        System.out.println("Epoch Time: " + epochTime);
        System.out.println("Date and Time: " + dateTime);
    }

    public static String convertEpochToDateTime(long epochTime) {
        Instant instant = Instant.ofEpochSecond(epochTime);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.ofHoursMinutes(5,30));
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}