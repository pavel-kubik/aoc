package cz.pk.adventofcode.current;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.StringCollector;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day14 {

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

    private void applyRules(List<Character> template, Map<String, String> rules) {
        StringBuilder key = new StringBuilder();
        for (int i = template.size() - 2; i >= 0; i--) {
            key.append(template.get(i)).append(template.get(i+1));
            Character newChar = rules.get(key.toString()).charAt(0);
            template.add(i+1, newChar);
            key.delete(0, 2);
        }
    }

    public long solve(String file) {
        // string lines
        List<String> data = new StringCollector(file).process();
        List<Character> template = data.get(0).chars().mapToObj(c -> (char)c).collect(toList());
        Map<String, String> rules = new HashMap<>();
        for (int i = 2; i < data.size(); i++) {
            String[] ruleParts = data.get(i).split("->");
            rules.put(ruleParts[0].trim(), ruleParts[1].trim());
        }

        for (int i = 0; i < 10; i++) {
            applyRules(template, rules);
            System.out.println(template);
        }

        Map<Character, Long> histogram = new HashMap<>();
        for (int i = 0; i < template.size(); i++) {
            histogram.compute(template.get(i), (key, count) -> count != null ? count + 1 : 1);
        }

        long min = Long.MAX_VALUE;
        long max = 0;
        for (Map.Entry<Character, Long> entry : histogram.entrySet()) {
            if (entry.getValue() > max) max = entry.getValue();
            if (entry.getValue() < min) min = entry.getValue();
        }
        System.out.println("Max: " + max);
        System.out.println("Min: " + min);
        return max-min;
    }

    public long solve2(String file) {
        List<String> data = new StringCollector(file).process();
        List<Character> template = data.get(0).chars().mapToObj(c -> (char)c).collect(toList());
        Map<String, String> rules = new HashMap<>();
        for (int i = 2; i < data.size(); i++) {
            String[] ruleParts = data.get(i).split("->");
            rules.put(ruleParts[0].trim(), ruleParts[1].trim());
        }

        for (int i = 0; i < 40; i++) {
            applyRules(template, rules);
            System.out.println("Step " + (i+1) + "/40");
            //System.out.println(template);
        }

        Map<Character, Long> histogram = new HashMap<>();
        for (int i = 0; i < template.size(); i++) {
            histogram.compute(template.get(i), (key, count) -> count != null ? count + 1 : 1);
        }

        long min = Long.MAX_VALUE;
        long max = 0;
        for (Map.Entry<Character, Long> entry : histogram.entrySet()) {
            if (entry.getValue() > max) max = entry.getValue();
            if (entry.getValue() < min) min = entry.getValue();
        }
        System.out.println("Max: " + max);
        System.out.println("Min: " + min);
        return max-min;
    }

    public static void main(String[] args) {
        System.out.println(Day14.class);
        long count;
        //*
        count = new Day14(true).solve("day14_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 1588;

        count = new Day14(true).solve("day14.txt");
        System.out.println("Result: " + count);
        assert count == 3587;

        //*/

        count = new Day14(false).solve2("day14_test.txt");
        System.out.println("Result: " + count);
        assert count == 2188189693529l;

        count = new Day14(false).solve2("day14.txt");
        System.out.println("Result: " + count);
        assert count == 44;
        //*/

        assert "VM options -ea set. You can delete this.".isEmpty();
        throw new RuntimeException("Don't forgot to set vm option -ea");
    }
}
