package cz.pk.adventofcode.y2020.day9;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import cz.pk.adventofcode.util.LongCollector;
import cz.pk.adventofcode.y2020.day1.Day1;

public class Day9 {

    public static void main(String[] args) throws IOException {
        System.out.println(Day9.class);
        //System.out.println(new Day9().findWeeknes(25)); //70639851
        System.out.println(new Day9().findSum(70639851));
    }

    public long findWeeknes(int preamble) throws IOException {
        List<Long> numbers = new LongCollector("2020/day9.txt").process().stream().map(i -> i).toList();
        for (int i = preamble; i < numbers.size(); i++) {
            long currentNumber = numbers.get(i);
            if (!isValid(i, preamble, numbers)) {
                return currentNumber;
            }
        }
        return -1;
    }

    boolean isValid(int i, int preamble, List<Long> numbers) {
        long currentNumber = numbers.get(i);
        for (int j = i - preamble; j < i; j++) {
            long a = numbers.get(j);
            for (int k = j + 1; k < i; k++) {
                long b = numbers.get(k);
                if (currentNumber == a + b) {
                    return true;
                }
            }
        }
        return false;
    }

    public long findSum(long sum) throws IOException {
        List<Long> numbers = new LongCollector("2020/day9.txt").process().stream().map(i -> i).toList();
        for (int i = 0; i < numbers.size(); i++) {
            for (int j = i + 1; j < numbers.size(); j++) {
                if (sum(i, j, numbers) == sum) {
                    long min = min(i, j, numbers);
                    long max = max(i, j, numbers);
                    return min + max;
                }
            }
        }
        return -1;
    }

    long sum(int start, int end, List<Long> numbers) {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += numbers.get(i);
        }
        return sum;
    }

    long min(int start, int end, List<Long> numbers) {
        long min = Long.MAX_VALUE;
        for (int i = start; i < end; i++) {
            if (numbers.get(i) < min) {
                min = numbers.get(i);
            }
        }
        assert min != Long.MAX_VALUE;
        return min;
    }

    long max(int start, int end, List<Long> numbers) {
        long max = Long.MIN_VALUE;
        for (int i = start; i < end; i++) {
            if (numbers.get(i) > max) {
                max = numbers.get(i);
            }
        }
        assert max != Long.MIN_VALUE;
        return max;
    }
}