package cz.pk.adventofcode.y2020.day15;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.y2020.day1.Day1;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Day15 {

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
            Type type = Type.get(String.valueOf(line.charAt(0)));
            Integer size = Integer.valueOf(line.substring(1));
            return new Subject(type, size);
        }
    }

    public int solve(String file, int ith) {
        List<Integer> start = Arrays.stream(file.split(","))
                .map(n -> Integer.valueOf(n))
                .collect(Collectors.toList());

        Map<Integer, Integer> lastIndex = new HashMap<>();
        for (int i = 0; i < start.size() - 1; i++) {
            lastIndex.put(start.get(i), i);
        }
        int lastNumber = start.get(start.size() - 1);
        for (int i = start.size(); i < ith; i++) {
            int previousLastNumber = lastNumber;
            //int lastIdx = start.subList(0, i-1).lastIndexOf(lastNumber);
            if (!lastIndex.containsKey(lastNumber)) {
                lastNumber = 0;
            } else {
                lastNumber = i - lastIndex.get(lastNumber) - 1;
            }
            start.add(lastNumber);
            lastIndex.put(previousLastNumber, i-1);
        }
        System.out.println(start);
        return lastNumber;
    }

    public int solve2(String file) {
        Subject[] data = new TypeCollector(file).process().toArray(new Subject[1]);
        System.out.println(data);
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(Day15.class);
        int count;
        //*
        count = new Day15(true).solve("0,3,6", 2020);
        System.out.println("Result: " + count);
        assert count == 436;

        count = new Day15(true).solve("2,1,3", 2020);
        System.out.println("Result: " + count);
        assert count == 10;

        count = new Day15(true).solve("15,5,1,4,7,0", 2020);
        System.out.println("Result: " + count);
        assert count == 1259;

        count = new Day15(true).solve("15,5,1,4,7,0", 30000000);
        System.out.println("Result: " + count);
        //assert count == 27016;

        //*/
    }
}
