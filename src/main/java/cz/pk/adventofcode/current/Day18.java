package cz.pk.adventofcode.current;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import lombok.Data;

@Data
public class Day18 {

    public long evaluateWithoutBrackets(String example) {
        return evaluateWithoutBrackets(Arrays.asList(example.split(" ")));
    }

    public long evaluateWithoutBrackets(List<String> parts) {
        if (parts.size() == 1) {
            return Long.valueOf(parts.get(0));
        } else {
            long base = Long.valueOf(parts.get(parts.size() - 1));
            if (parts.get(parts.size() - 2).equals("+")) {
                return base + evaluateWithoutBrackets(parts.subList(0, parts.size() - 2));
            } else if (parts.get(parts.size() - 2).equals("*")) {
                return base * evaluateWithoutBrackets(parts.subList(0, parts.size() - 2));
            } else {
                assert false;
                return 0;
            }
        }
    }

    public long evaluateWithBrackets(String example) {
        System.out.println(example);
        int firstBracket = example.indexOf("(");
        if (firstBracket == -1) {
            return evaluateWithoutBrackets(example);
        } else {
            // replace brackets and simplify
            int deep = 1;
            boolean containsParenthesis = false;
            StringBuilder subExample = new StringBuilder();
            int matchBracket = -1;
            for (int i = firstBracket+1; i < example.length(); i++) {
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
                    evaluateWithBrackets(middlePart) : evaluateWithoutBrackets(middlePart);
            return evaluateWithBrackets(firstPart + middleNumber + lastPart);
        }
    }

    public long solve(String file) throws IOException {
        URL resource = getClass().getClassLoader().getResource(file);
        List<String> lines = Files.readAllLines(Path.of(resource.getPath()));
        long sum = 0;
        for (String line : lines) {
            sum += evaluateWithBrackets(line);
        }
        return sum;
    }

    public static void main(String[] args) throws IOException {
        long count;
        //*
        count = new Day18().evaluateWithoutBrackets("1 + 2 * 3 + 4 * 5 + 6");
        System.out.println("Result: " + count);
        assert count == 71;

        count = new Day18().evaluateWithBrackets("2 * 3 + (4 * 5)");
        System.out.println("Result: " + count);
        assert count == 26;

        count = new Day18().evaluateWithBrackets("5 + (8 * 3 + 9 + 3 * 4 * 3)");
        System.out.println("Result: " + count);
        assert count == 437;

        count = new Day18().evaluateWithBrackets("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))");
        System.out.println("Result: " + count);
        assert count == 12240;

        count = new Day18().evaluateWithBrackets("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2");
        System.out.println("Result: " + count);
        assert count == 13632;

        count = new Day18().solve("day18.txt");
        System.out.println("Result: " + count);
        //assert count == 845;

        /*/

        count = new Day13(true).solve2("2020/day12_test.txt");
        System.out.println("Result: " + count);
        assert count == 25;

        count = new Day13(true).solve2("2020/day12.txt");
        System.out.println("Result: " + count);
        //assert count == 27016;

         */
        //*/
    }
}
