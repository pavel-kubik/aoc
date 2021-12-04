package cz.pk.adventofcode.y2020.day3;

import java.io.IOException;
import java.util.List;

import cz.pk.adventofcode.util.StringCollector;
import cz.pk.adventofcode.y2020.day1.Day1;

public class Day3 {

    public static void main(String[] args) throws IOException {
        System.out.println(Day3.class);
        //int slopes = new Day3().checkSlope(3, false);
        //System.out.println("Slopes " + slopes);
        int[] rightDiffs = {1, 3, 5, 7};
        int slopes = 1;
        for (int i = 0; i < rightDiffs.length; i++) {
            slopes *= new Day3().checkSlope(rightDiffs[i], false);
        }
        slopes *= new Day3().checkSlope(1, true);
        System.out.println("Slopes " + slopes);
        assert slopes == 1666768320;
    }

    int checkSlope(int rightDiff, boolean skip) throws IOException {
        List<String> data = new StringCollector("2020/day3.txt").process();
        int col = 0;
        int slopes = 0;
        for (int row = 0; row < data.size(); row++) {
            if (skip) {
                if (row % 2 == 1) {
                    continue;
                }
            }
            int length = data.get(row).length();
            if (data.get(row).charAt(col % length) == '#') {
                slopes++;
            }
            col += rightDiff;
        }
        return slopes;
    }
}
