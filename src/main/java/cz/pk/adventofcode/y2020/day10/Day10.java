package cz.pk.adventofcode.y2020.day10;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cz.pk.adventofcode.util.LongCollector;

public class Day10 {

    Map<Long, Integer> diffs = new HashMap<>();
    Map<Long, Long> cache = new HashMap<>();

    public static void main(String[] args) throws IOException {
        int diff = new Day10().findJoytageDiff("2020/day10_test.txt", 3);
        System.out.println("Diff = " + diff);
        assert diff == 7 * 5;
        int diff2 = new Day10().findJoytageDiff("2020/day10_test2.txt", 3);
        System.out.println("Diff = " + diff2);
        assert diff2 == 22 * 10;
        int diff3 = new Day10().findJoytageDiff("2020/day10.txt", 3);
        System.out.println("Diff = " + diff3);
        long ways = new Day10().findJoytageArrangements("2020/day10_test0.txt", 3);
        System.out.println("Ways = " + ways);
        assert ways == 7;
        ways = new Day10().findJoytageArrangements("2020/day10_test.txt", 3);
        System.out.println("Ways = " + ways);
        assert ways == 8;
        ways = new Day10().findJoytageArrangements("2020/day10_test2.txt", 3);
        System.out.println("Ways = " + ways);
        assert ways == 19208;
        ways = new Day10().findJoytageArrangements("2020/day10.txt", 3);
        System.out.println("Ways = " + ways);
    }

    public int findJoytageDiff(String file, long allowedDiff) throws IOException {
        List<Long> outputJoltages = new LongCollector(file).process().stream().map(i -> i).collect(Collectors.toList());
        outputJoltages.sort(Long::compareTo);
        System.out.println(outputJoltages);
        long joltage = 0;
        for (int i = 0; i < outputJoltages.size(); i++) {
            long diff = outputJoltages.get(i) - joltage;
            assert diff <= allowedDiff;
            if (diffs.containsKey(diff)) {
                diffs.put(diff, diffs.get(diff) + 1);
            } else {
                diffs.put(diff, 1);
            }
            joltage = outputJoltages.get(i);
        }
        System.out.println(diffs);
        return diffs.get(1L) * (diffs.get(3L) + 1);
    }

    public long findJoytageArrangements(String file, long allowedDiff) throws IOException {
        List<Long> outputJoltages = new LongCollector(file).process().stream().map(i -> i).collect(Collectors.toList());
        outputJoltages.add(0, 0L);
        outputJoltages.sort(Long::compareTo);
        System.out.println(outputJoltages);
        //return count(0, outputJoltages, allowedDiff);
        return countReverse(0, outputJoltages, allowedDiff);
    }

    int count(int i, List<Long> outputJoltages, long allowedDiff) {
        if (i == outputJoltages.size()) {
            return 1;
        }
        int ways = 0;
        // first subtree
        //   i+1 - next
        ways += count(i + 1, outputJoltages, allowedDiff);

        // second subtree
        //   i+2 - skip 1
        if (i + 2 < outputJoltages.size()) {
            if (outputJoltages.get(i + 2) - outputJoltages.get(i) <= allowedDiff) {
                ways += count(i + 2, outputJoltages, allowedDiff);
            }
        }

        // third
        //   i+3 - skip 2
        if (i + 3 < outputJoltages.size()) {
            if (outputJoltages.get(i + 3) - outputJoltages.get(i) <= allowedDiff) {
                ways += count(i + 3, outputJoltages, allowedDiff);
            }
        }

        return ways;
    }

    long countReverse(int i, List<Long> outputJoltages, long allowedDiff) {
        if (i == outputJoltages.size()) {
            return 1;
        }

        if (cache.containsKey(outputJoltages.get(i))) {
            return cache.get(outputJoltages.get(i));
        }

        long ways = 0;
        // third
        //   i+3 - skip 2
        if (i + 3 < outputJoltages.size()) {
            if (outputJoltages.get(i + 3) - outputJoltages.get(i) <= allowedDiff) {
                ways += countReverse(i + 3, outputJoltages, allowedDiff);
            }
        }

        // second subtree
        //   i+2 - skip 1
        if (i + 2 < outputJoltages.size()) {
            if (outputJoltages.get(i + 2) - outputJoltages.get(i) <= allowedDiff) {
                ways += countReverse(i + 2, outputJoltages, allowedDiff);
            }
        }

        // first subtree
        //   i+1 - next
        ways += countReverse(i + 1, outputJoltages, allowedDiff);

        cache.put(outputJoltages.get(i), ways);
        return ways;
    }
}