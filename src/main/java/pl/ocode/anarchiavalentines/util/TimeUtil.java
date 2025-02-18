package pl.ocode.anarchiavalentines.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;

@UtilityClass
public final class TimeUtil {

    public static String convertTime(long time) {
        if (time < 1000L) {
            return "< 1sek";
        }

        long days = TimeUnit.MILLISECONDS.toDays(time);
        long hours = TimeUnit.MILLISECONDS.toHours(time) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60;

        StringBuilder builder = new StringBuilder();
        if (days > 0) {
            builder.append(days).append("dni ").append(hours).append("godz ").append(minutes).append("min ").append(seconds).append("sek");
        } else if (hours > 0) {
            builder.append(hours).append("godz ").append(minutes).append("min ").append(seconds).append("sek");
        } else if (minutes > 0) {
            builder.append(minutes).append("min ").append(seconds).append("sek");
        } else {
            builder.append(seconds).append("sek");
        }

        return builder.toString().trim();
    }


    public static long stringToTime(@NonNull String string) {
        int time;
        string = string.toLowerCase();

        try {
            time = Integer.parseInt(string.substring(0, string.length() - 1));
        } catch (NumberFormatException e) {
            return -1L;
        }

        char type = string.charAt(string.length() - 1);
        return switch (type) {
            case 'y' -> TimeUnit.DAYS.toMillis((long) time * 365L);
            case 'w' -> TimeUnit.DAYS.toMillis((long) time * 7L);
            case 'd' -> TimeUnit.DAYS.toMillis(time);
            case 'h' -> TimeUnit.HOURS.toMillis(time);
            case 'm' -> TimeUnit.MINUTES.toMillis(time);
            case 's' -> TimeUnit.SECONDS.toMillis(time);
            default -> -1L;
        };
    }
}
