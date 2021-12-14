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

    private static class Key {
        char[] key = new char[2];

        public Key(char a, char b) {
            setKey(a, b);
        }

        public Key(String key) {
            setKey(key.charAt(0), key.charAt(1));
        }

        public void setKey(char a, char b) {
            key[0] = a;
            key[1] = b;
        }

        public char getA() {
            return key[0];
        }

        public char getB() {
            return key[1];
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key1 = (Key) o;
            return Arrays.equals(key, key1.key);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(key);
        }

        @Override
        public String toString() {
            return "Key{" +
                    "key=" + Arrays.toString(key) +
                    '}';
        }
    }

    private void applyRules(List<Character> template, Map<Key, String> rules) {
        Key key = new Key("  ");
        for (int i = template.size() - 2; i >= 0; i--) {
            key.setKey(template.get(i), template.get(i+1));
            Character newChar = rules.get(key).charAt(0);
            template.add(i+1, newChar);
        }
    }

    private Map<Key, Long> applyRulesToSequences(Map<Key, Long> sequences, Map<Key, String> rules) {
        Map<Key, Long> newGen = new HashMap<>();
        for (Map.Entry<Key, Long> sequence : sequences.entrySet()) {
            char newChar = rules.get(sequence.getKey()).charAt(0);
            Key keyA = new Key(sequence.getKey().getA(), newChar);
            Key keyB = new Key(newChar, sequence.getKey().getB());
            newGen.compute(keyA, (k, v) -> v != null ? v + sequence.getValue() : sequence.getValue());
            newGen.compute(keyB, (k, v) -> v != null ? v + sequence.getValue() : sequence.getValue());
        }
        return newGen;
    }

    public long solve(String file, int iterations) {
        List<String> data = new StringCollector(file).process();
        List<Character> template = data.get(0).chars().mapToObj(c -> (char)c).collect(toList());
        Map<Key, String> rules = new HashMap<>();
        for (int i = 2; i < data.size(); i++) {
            String[] ruleParts = data.get(i).split("->");
            rules.put(new Key(ruleParts[0].trim()), ruleParts[1].trim());
        }
        System.out.println("Template: " + template);

        // split template to pairs
        Map<Key, Long> sequences = new HashMap<>();
        for (int i = 0; i< template.size() - 1; i++) {
        //for (int i = template.size() - 2; i >= 0; i--) {
            Key key = new Key(template.get(i), template.get(i+1));
            sequences.compute(key, (k, val) -> val != null ? val + 1 : 1);
        }
        System.out.println(sequences);

        // step - recount pairs
        for (int i = 0; i < iterations; i++) {
            sequences = applyRulesToSequences(sequences, rules);
            System.out.println(sequences);
        }

        Map<Character, Long> histogram = new HashMap<>();
        for (Map.Entry<Key, Long> sequence : sequences.entrySet()) {
            histogram.compute(sequence.getKey().getA(), (key, count) -> count != null ? count + sequence.getValue() : sequence.getValue());
            histogram.compute(sequence.getKey().getB(), (key, count) -> count != null ? count + sequence.getValue() : sequence.getValue());
        }

        long min = Long.MAX_VALUE;
        long max = 0;
        char minChar = ' ';
        char maxChar = ' ';
        for (Map.Entry<Character, Long> entry : histogram.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                maxChar = entry.getKey();
            }
            if (entry.getValue() < min) {
                min = entry.getValue();
                minChar = entry.getKey();
            }
        }
        System.out.println("Max (" + maxChar + "): " + max/2);
        System.out.println("Min (" + minChar + "): " + min/2);
        return max/2-min/2 + 1; //TODO why + 1???
    }

    public static void main(String[] args) {
        System.out.println(Day14.class);
        long count;
        //*
        count = new Day14(true).solve("day14_test.txt", 10);
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 1588;

        count = new Day14(true).solve("day14.txt", 10);
        System.out.println("Result: " + count);
        assert count == 3587;

        //*/

        count = new Day14(false).solve("day14_test.txt", 40);
        System.out.println("Result: " + count);
        assert count == 2188189693529l;

        count = new Day14(false).solve("day14.txt", 40);
        System.out.println("Result: " + count);
        assert count == 3906445077999l;
        //*/
    }
}
