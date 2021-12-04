package cz.pk.adventofcode.y2020.day1;

import static java.lang.String.format;

public class Day1 {

    public static void main(String[] args) {
        System.out.println(Day1.class);
        int result = new Day1().findSubAndMultiple(DataDay1.data, 2020);
        System.out.println("Result is " + result);
        result = new Day1().findSubThreeAndMultiple(DataDay1.data, 2020);
        System.out.println("Result is " + result);
    }

    public int findSubAndMultiple(int[] data, int sum) {
        Integer numberA = null;
        Integer numberB = null;
        for (int i = 0; i < data.length; i++) {
            for (int j = i; j < data.length; j++) {
                if (data[i] + data[j] == sum) {
                    numberA = i;
                    numberB = j;
                    break;
                }
            }
        }
        if (numberA == null || numberB == null) {
            throw new RuntimeException(format("Two numbers giving %s not found in array", sum));
        }
        return data[numberA] * data[numberB];
    }

    public int findSubThreeAndMultiple(int[] data, int sum) {
        Integer numberA = null;
        Integer numberB = null;
        Integer numberC = null;
        for (int i = 0; i < data.length; i++) {
            for (int j = i; j < data.length; j++) {
                for (int k = j; k < data.length; k++) {
                    if (data[i] + data[j] + data[k] == sum) {
                        numberA = i;
                        numberB = j;
                        numberC = k;
                        break;
                    }
                }
            }
        }
        if (numberA == null || numberB == null) {
            throw new RuntimeException(format("Three numbers giving %s not found in array", sum));
        }
        return data[numberA] * data[numberB] * data[numberC];
    }

}
