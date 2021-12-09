package cz.pk.adventofcode.util;

import java.util.List;

public class PKMath {

    public static Integer min(int number, int ... numbers) {
        int min = number;
        for (int a : numbers) {
            if (a < min) {
                min = a;
            }
        }
        return min;
    }

    public static Integer min(List<Integer> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return null;
        }
        int min = numbers.get(0);
        for (Integer n : numbers) {
            if (n.compareTo(min) < 0) {
                min = n;
            }
        }
        return min;
    }

    public static boolean isMin(int number, List<Integer> numbers) {
        return number < min(numbers);
    }

    public static boolean isMinOrEqual(int number, List<Integer> numbers) {
        return number <= min(numbers);
    }

    public static Integer max(int number, int ... numbers) {
        int min = number;
        for (int a : numbers) {
            if (a > min) {
                min = a;
            }
        }
        return min;
    }

    public static Integer max(List<Integer> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return null;
        }
        int min = numbers.get(0);
        for (Integer n : numbers) {
            if (n.compareTo(min) > 0) {
                min = n;
            }
        }
        return min;
    }

    public static boolean isMax(int number, List<Integer> numbers) {
        return number > max(numbers);
    }

    public static boolean isMaxOrEqual(int number, List<Integer> numbers) {
        return number >= max(numbers);
    }

}
