package cz.pk.adventofcode.y2021.day7;

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
public class Day7 {

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

    public List<Integer> loadData(String file) {
        return stream(new StringCollector(file)
                .process().get(0).split(",")).map(Integer::valueOf).collect(toList());
    }

    public long solve(String file) {
        List<Integer> horizontal = loadData(file);

        int minY = Integer.MAX_VALUE;
        int maxY = -Integer.MAX_VALUE;
        for (int i = 0; i < horizontal.size(); i++) {
            if (horizontal.get(i) < minY) minY = horizontal.get(i);
            if (horizontal.get(i) > maxY) maxY = horizontal.get(i);
        }

        int minYStart = -1;
        long minFuel = Long.MAX_VALUE;
        for (int i = minY; i < maxY; i++) {
            long fuel = 0;
            for (int y = 0; y < horizontal.size(); y++) {
                fuel += Math.abs(horizontal.get(y) - i);
            }
            if (fuel < minFuel) {
                minFuel = fuel;
                minYStart = i;
            }
        }

        return minFuel;
    }

    public long solve2(String file) {
        List<Integer> horizontal = loadData(file);

        int minY = Integer.MAX_VALUE;
        int maxY = -Integer.MAX_VALUE;
        for (int i = 0; i < horizontal.size(); i++) {
            if (horizontal.get(i) < minY) minY = horizontal.get(i);
            if (horizontal.get(i) > maxY) maxY = horizontal.get(i);
        }

        assert countFuelConsumption(Math.abs(16 - 5)) == 66;

        int minYStart = -1;
        long minFuel = Long.MAX_VALUE;
        for (int i = minY; i < maxY; i++) {
            long fuel = 0;
            for (int y = 0; y < horizontal.size(); y++) {
                fuel += countFuelConsumption(Math.abs(horizontal.get(y) - i));
            }
            if (fuel < minFuel) {
                minFuel = fuel;
                minYStart = i;
            }
        }

        return minFuel;
    }

    private long countFuelConsumption(Integer distance) {
        int sum = 0;
        int costOfStep = 1;
        for (int i = 0; i < distance; i++) {
            sum += costOfStep++;
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(Day7.class);
        long count;
        //*
        count = new Day7(true).solve("2021/day7_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 37;

        count = new Day7(true).solve("2021/day7.txt");
        System.out.println("Result: " + count);
        assert count == 349769;

        //*/

        count = new Day7(true).solve2("2021/day7_test.txt");
        System.out.println("Result: " + count);
        assert count == 168;

        count = new Day7(true).solve2("2021/day7.txt");
        System.out.println("Result: " + count);
        assert count == 99540554;
        //*/
    }
}
