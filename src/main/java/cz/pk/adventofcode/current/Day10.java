package cz.pk.adventofcode.current;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.StringCollector;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Data
public class Day10 {

    private final boolean debug;

    @Data
    @AllArgsConstructor
    class Subject {
        Type type;
        int size;
    }

    enum Type {
        PLACEHOLDER("p"),
        PLACEHOLDER2("q"),
        ;

        private static final Map<String, Type> values;

        static {
            values = new HashMap<>();
            Arrays.stream(values()).forEach(p -> values.put(p.value, p));
        }

        private final String value;

        Type(String place) {
            this.value = place;
        }

        public static Type get(String value) {
            return values.get(value);
        }

        public String toString() {
            return value;
        }
    }

    class TypeCollector extends DataCollector<Subject> {

        public TypeCollector(String file) {
            super(file);
        }

        @Override
        protected Subject processLine(String line) {
            // numbers in line without separator - Subject: List<Integer>
            //return line.chars().mapToObj(c -> (char)c).map(c -> Integer.parseInt(c.toString())).collect(toList());

            // numbers in line with separator - Subject: List<Integer>
            // decimal - can be any base - change 2nd argument in parseInt
            //return stream(line.split(" ")).map(n -> Integer.parseInt(n, 10)).collect(toList());

            Type type = Type.get(String.valueOf(line.charAt(0)));
            Integer size = Integer.valueOf(line.substring(1));
            return new Subject(type, size);
        }
    }

    private boolean isOpen(String character) {
        return List.of("(", "[", "{", "<").contains(character);
    }

    private boolean isClosed(String character) {
        return List.of(")", "]", "}", ">").contains(character);
    }

    private boolean match(String openChar, String closeChar) {
        switch (openChar) {
            case "(":
                return closeChar.equals(")");
            case "[":
                return closeChar.equals("]");
            case "{":
                return closeChar.equals("}");
            case "<":
                return closeChar.equals(">");
            default:
                throw new RuntimeException("Unknown open character " + openChar);
        }
    }

    private String getClosingCharacter(String openChar) {
        switch (openChar) {
            case "(":
                return ")";
            case "[":
                return "]";
            case "{":
                return "}";
            case "<":
                return ">";
            default:
                throw new RuntimeException("Unknown open character " + openChar);
        }
    }

    private long rankIllegalCharacter(String character) {
        switch (character) {
            case ")":
                return 3;
            case "]":
                return 57;
            case "}":
                return 1197;
            case ">":
                return 25137;
            default:
                throw new RuntimeException("Unknown open character " + character);
        }
    }

    private long rankMissingCharacter(String character) {
        switch (character) {
            case ")":
                return 1;
            case "]":
                return 2;
            case "}":
                return 3;
            case ">":
                return 4;
            default:
                throw new RuntimeException("Unknown open character " + character);
        }
    }

    private long parseLine(String line) {
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < line.length(); i++) {
            String character = line.substring(i, i+1);
            if (isOpen(character)) {
                stack.push(character);
            } else {
                if (stack.isEmpty()) {
                    break;
                }
                String openChar = stack.pop();
                if (!match(openChar, character)) {
                    return rankIllegalCharacter(character);
                }
            }
        }
        return 0;
    }

    private Stack<String> parseLine2(String line) {
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < line.length(); i++) {
            String character = line.substring(i, i+1);
            if (isOpen(character)) {
                stack.push(character);
            } else {
                if (stack.isEmpty()) {
                    break;
                }
                String openChar = stack.pop();
                if (!match(openChar, character)) {
                    return null;
                }
            }
        }
        return stack;
    }

    public long solve(String file) {
        List<String> data = new StringCollector(file).process();
        long count = 0;
        for (int i = 0; i < data.size(); i++) {
            count += parseLine(data.get(i));
        }

        return count;
    }

    public long solve2(String file) {
        List<String> data = new StringCollector(file).process();
        long count = 0;
        List<Long> scores = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Stack<String> missing = parseLine2(data.get(i));
            if (missing == null) {
                continue;
            }
            List<String> completeChars = new ArrayList<>();
            while (!missing.isEmpty()) {
                completeChars.add(getClosingCharacter(missing.pop()));
            }
            List<Long> missingRanks = completeChars.stream()
                    .map(ch -> rankMissingCharacter(ch))
                    .toList();
            long lineCount = 0;
            for (Long rank : missingRanks) {
                lineCount = rank + 5*lineCount;
            }
            scores.add(lineCount);
        }

        Collections.sort(scores);
        System.out.println(scores);
        return scores.get(scores.size()/2);
    }

    public static void main(String[] args) {
        System.out.println(Day10.class);
        long count;
        //*
        count = new Day10(true).solve("day10_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 26397;

        count = new Day10(true).solve("day10.txt");
        System.out.println("Result: " + count);
        assert count == 369105;

        //*/

        count = new Day10(true).solve2("day10_test.txt");
        System.out.println("Result: " + count);
        assert count == 288957;

        count = new Day10(true).solve2("day10.txt");
        System.out.println("Result: " + count);
        assert count == 3999363569l;
        //*/
    }
}
