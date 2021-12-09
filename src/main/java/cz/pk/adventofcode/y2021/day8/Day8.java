package cz.pk.adventofcode.y2021.day8;

import cz.pk.adventofcode.util.StringCollector;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

import static cz.pk.adventofcode.y2021.day8.Day8.DIGITS.*;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day8 {

    private final boolean debug;

    @Data
    @AllArgsConstructor
    class Subject {
        DIGITS type;
        int size;
    }

    enum DIGITS {
        ZERO("cagedb", 0),     //6
        ONE("ab", 1),          //2 <-
        TWO("gcdfa", 2),       //5
        THREE("fbcad", 3),     //5
        FOUR("eafb", 4),       //4 <-
        FIVE("cdfbe", 5),      //5
        SIX("cdfgeb", 6),      //6
        SEVEN("dab", 7),       //3 <-
        EIGHT("acedgfb", 8),   //7 <-
        NINE("cefabd", 9),     //6
        ;

        private static final Map<String, DIGITS> values;

        static {
            values = new HashMap<>();
            Arrays.stream(values()).forEach(p -> values.put(p.value, p));
        }

        private final String value;
        private final int digit;

        DIGITS(String place, int digit) {
            this.value = sortString(place);
            this.digit = digit;
        }

        public static DIGITS get(String value) {
            return values.get(value);
        }

        public int getDigit(String value) {
            return digit;
        }

        public String toString() {
            return value;
        }
    }

//    private String segments = """
//          0:      1:      2:      3:      4:
//         aaaa    ....    aaaa    aaaa    ....
//        b    c  .    c  .    c  .    c  b    c
//        b    c  .    c  .    c  .    c  b    c
//         ....    ....    dddd    dddd    dddd
//        e    f  .    f  e    .  .    f  .    f
//        e    f  .    f  e    .  .    f  .    f
//         gggg    ....    gggg    gggg    ....
//
//          5:      6:      7:      8:      9:
//         aaaa    aaaa    aaaa    aaaa    aaaa
//        b    .  b    .  .    c  b    c  b    c
//        b    .  b    .  .    c  b    c  b    c
//         dddd    dddd    ....    dddd    dddd
//        .    f  e    f  .    f  e    f  .    f
//        .    f  e    f  .    f  e    f  .    f
//         gggg    gggg    ....    gggg    gggg   """;

    public static String sortString(String in) {
        char[] chars = in.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    private List<DIGITS> uniqueLength = List.of(ONE, FOUR, SEVEN, EIGHT);

    public boolean contains(String pattern, String digit) {
        for (char ch : pattern.toCharArray()) {
            if (digit.indexOf(ch) == -1) {
                return false;
            }
        }
        return true;
    }

    public String removeParts(String a, String b) {
        List<Character> listA = a.chars().mapToObj(c -> (char) c).collect(toList());
        for (Character ch : b.toCharArray()) {
            listA.remove(ch);
        }
        StringJoiner joiner = new StringJoiner("");
        for (Character ch: listA) {
            joiner.add(ch.toString());
        }
        return joiner.toString();
    }

    public Map<String, Integer>  countDigitMapping(List<String> numbers) {
        // determine unique digits - 1,4,7,8
        Map<Integer, String> digitsMapping = new HashMap<>();
        List<String> group5l = new ArrayList<>();
        List<String> group6l = new ArrayList<>();
        for (String number : numbers) {
            for (DIGITS digit : uniqueLength) {
                if (digit.value.length() == number.length()) {
                    digitsMapping.put(digit.digit, number);
                    break;
                }
            }
            if (number.length() == 5) {
                group5l.add(number);
            }
            if (number.length() == 6) {
                group6l.add(number);
            }
        }
        // 5 length - 2,3,5
        // 6 length - 0,6,9

        // determine 3: 3 contains 1
        for (String number : group5l) {
            if (contains(digitsMapping.get(1), number)) {
                digitsMapping.put(3, number);
                break;
            }
        }
        group5l.remove(digitsMapping.get(3));

        // determine 6: doesn't contain 1
        for (String number : group6l) {
            if (!contains(digitsMapping.get(1), number)) {
                digitsMapping.put(6, number);
                break;
            }
        }
        group6l.remove(digitsMapping.get(6));

        // 7 - 1 => top display (a-led)
        // 4 - 1 => b, d
        String bd = removeParts(digitsMapping.get(4), digitsMapping.get(1));

        // remains 2, 5, 0, 9

        // determine 5: contains cb
        for (String number : group5l) {
            if (contains(bd, number)) {
                digitsMapping.put(5, number);
                break;
            }
        }
        group5l.remove(digitsMapping.get(5));

        // determine 2: last one in 5-group
        digitsMapping.put(2, group5l.get(0));

        // determine 9: contains 5
        for (String number : group6l) {
            if (contains(digitsMapping.get(5), number)) {
                digitsMapping.put(9, number);
                break;
            }
        }
        group6l.remove(digitsMapping.get(9));

        // determine 0: last one in 6-group
        digitsMapping.put(0, group6l.get(0));

        Map<String, Integer> mapping = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            mapping.put(sortString(digitsMapping.get(i)), i);
        }

        return mapping;
    }

    public long solve(String file) {
        List<String> data = new StringCollector(file).process();
        int count = 0;
        for (String line : data) {
            String secondPart = line.split("\\|")[1].trim();
            String[] numbers = secondPart.split(" ");
            for (int i = 0; i < numbers.length; i++) {
                if (List.of(2,4,3,7).contains(numbers[i].length())) {
                    count++;
                }
            }
        }

        return count;
    }

    public long solve2(String file) {
        List<String> data = new StringCollector(file).process();
        int count = 0;
        for (String line : data) {
            int number = 0;
            String[] inOut = line.split("\\|");
            String input = inOut[0].trim();
            String output = inOut[1].trim();
            Map<String, Integer> mapping = countDigitMapping(Arrays.asList(input.split(" ")));
            String[] numbers = output.split(" ");
            for (int i = 0; i < numbers.length; i++) {
                if (!mapping.containsKey(sortString(numbers[i]))) {
                    System.out.println("Not found " + sortString(numbers[i]));
                }
                int digit = mapping.get(sortString(numbers[i]));
                number = number*10 + digit;
            }
            System.out.println(number);
            count += number;
        }

        return count;
    }

    public static void main(String[] args) {
        System.out.println(Day8.class);
        long count;
        //*
        count = new Day8(true).solve("2021/day8_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 26;

        count = new Day8(true).solve("2021/day8.txt");
        System.out.println("Result: " + count);
        assert count == 342;

        //*/

        count = new Day8(true).solve2("2021/day8_test.txt");
        System.out.println("Result: " + count);
        assert count == 61229;

        count = new Day8(true).solve2("2021/day8.txt");
        System.out.println("Result: " + count);
        assert count == 44;
        //*/
    }
}
