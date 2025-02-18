package pl.ocode.anarchiavalentines.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Random;

@UtilityClass
public class NumberUtil {
    public static double round(double number, int places) {
        double factor = Math.pow(10.0, places);
        return (double)Math.round(number * factor) / factor;
    }

    public static int findLargestNumber(@NonNull List<Integer> list) {
        int largest = list.get(0);
        for (int number : list) {
            if (number <= largest) continue;
            largest = number;
        }

        return largest;
    }

    public static int getRandomInteger(int min, int max) {
        return new Random().ints(min, max).findAny().orElse(min);
    }

    public static long getRandomLong(long min, long max) {
        return new Random().longs(min, max).findAny().orElse(min);
    }

    public static double getRandomDouble(double min, double max) {
        return new Random().doubles(min, max).findAny().orElse(min);
    }

    public static boolean reachChance(double chance) {
        return chance >= 100.0 || chance >= getRandomDouble(0.0, 100.0);
    }
}
