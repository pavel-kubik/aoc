package cz.pk.adventofcode.y2020.day18;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import cz.pk.adventofcode.util.FileReadUtil;
import cz.pk.adventofcode.y2020.day1.Day1;
import lombok.Data;

@Data
public class Day18 {

    public long evaluateWithoutBrackets(String example, int part) {
        if (part == 1) {
            return evaluateWithoutBrackets(Arrays.asList(example.split(" ")), part);
        } else {
            String[] subExamples = example.split("\\*");
            long mul = 1;
            for (String subExample : subExamples) {
                System.out.println(subExample.trim());
                mul *= evaluateWithoutBrackets(Arrays.asList(subExample.trim().split(" ")), part);
            }
            return mul;
        }
    }

    public long evaluateWithoutBrackets(List<String> parts, int part) {
        if (parts.size() == 1) {
            return Long.valueOf(parts.get(0));
        } else {
            long base = Long.valueOf(parts.get(parts.size() - 1));
            if (parts.get(parts.size() - 2).equals("+")) {
                return base + evaluateWithoutBrackets(parts.subList(0, parts.size() - 2), part);
            } else if (parts.get(parts.size() - 2).equals("*")) {
                return base * evaluateWithoutBrackets(parts.subList(0, parts.size() - 2), part);
            } else {
                assert false;
                return 0;
            }
        }
    }

    public long evaluateWithBrackets(String example, int part) {
        System.out.println(example);
        int firstBracket = example.indexOf("(");
        if (firstBracket == -1) {
            return evaluateWithoutBrackets(example, part);
        } else {
            // replace brackets and simplify
            int deep = 1;
            boolean containsParenthesis = false;
            StringBuilder subExample = new StringBuilder();
            int matchBracket = -1;
            for (int i = firstBracket + 1; i < example.length(); i++) {
                if (example.charAt(i) == '(') {
                    containsParenthesis = true;
                    deep++;
                } else if (example.charAt(i) == ')') {
                    deep--;
                    if (deep == 0) {
                        // end of sub-example
                        matchBracket = i;
                        break;
                    }
                }
                subExample.append(example.charAt(i));
            }
            String firstPart = example.substring(0, firstBracket);
            String middlePart = subExample.toString();
            String lastPart = matchBracket + 1 < example.length() ? example.substring(matchBracket + 1, example.length()) : "";
            long middleNumber = containsParenthesis ?
                    evaluateWithBrackets(middlePart, part) : evaluateWithoutBrackets(middlePart, part);
            return evaluateWithBrackets(firstPart + middleNumber + lastPart, part);
        }
    }

    public long solve(String file, int part) throws IOException {
        List<String> lines = FileReadUtil.readAllLines(file);
        long sum = 0;
        for (String line : lines) {
            sum += evaluateWithBrackets(line, part);
        }
        return sum;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(Day18.class);
        long count;
        //*
        count = new Day18().evaluateWithoutBrackets("1 + 2 * 3 + 4 * 5 + 6", 1);
        System.out.println("Result: " + count);
        assert count == 71;

        count = new Day18().evaluateWithBrackets("2 * 3 + (4 * 5)", 1);
        System.out.println("Result: " + count);
        assert count == 26;

        count = new Day18().evaluateWithBrackets("5 + (8 * 3 + 9 + 3 * 4 * 3)", 1);
        System.out.println("Result: " + count);
        assert count == 437;

        count = new Day18().evaluateWithBrackets("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))", 1);
        System.out.println("Result: " + count);
        assert count == 12240;

        count = new Day18().evaluateWithBrackets("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2", 1);
        System.out.println("Result: " + count);
        assert count == 13632;

        count = new Day18().solve("2020/day18.txt", 1);
        System.out.println("Result: " + count);
        assert count == 75592527415659l;

        count = new Day18().evaluateWithoutBrackets("1 + 2 * 3 + 4 * 5 + 6", 2);
        System.out.println("Result: " + count);
        assert count == 231;

        count = new Day18().solve("2020/day18.txt", 2);
        System.out.println("Result: " + count);
        //assert count == 27016;

        //*/
    }
}
