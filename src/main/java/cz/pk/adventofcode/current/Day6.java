package cz.pk.adventofcode.current;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.StringCollector;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

import static cz.pk.adventofcode.util.DataCollectorFactory.collectData;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day6 {

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

    public long solve(String file) {
        List<String> data = new StringCollector(file).process();
        String[] agesStr = data.get(0).split(",");
        List<Integer> ages = new ArrayList<>();
        for (int i = 0; i < agesStr.length; i++) {
            ages.add(Integer.valueOf(agesStr[i]).intValue());
        }
        System.out.println(ages);

        for (int day = 0; day < 80; day++) {
            List<Integer> newGen = new ArrayList<>();
            for (int i = 0; i < ages.size(); i++) {
                ages.set(i, ages.get(i) - 1);
            }
            for (int i = 0; i < ages.size(); i++) {
                if (ages.get(i) == -1) {
                    newGen.add(8);
                    ages.set(i, 6);
                }
            }
            ages.addAll(newGen);
            System.out.println(format("Day %d (%d): %s", day, ages.size(), ages));
        }

        System.out.println(data);
        return ages.size();
    }

    public long solve2(String file) {
        List<String> data = new StringCollector(file).process();
        String[] agesStr = data.get(0).split(",");
        List<Integer> ages = new ArrayList<>();
        for (int i = 0; i < agesStr.length; i++) {
            ages.add(Integer.valueOf(agesStr[i]).intValue());
        }
        System.out.println(ages);

        for (int day = 0; day < 256; day++) {
            List<Integer> newGen = new ArrayList<>();
            for (int i = 0; i < ages.size(); i++) {
                ages.set(i, ages.get(i) - 1);
            }
            for (int i = 0; i < ages.size(); i++) {
                if (ages.get(i) == -1) {
                    newGen.add(8);
                    ages.set(i, 6);
                }
            }
            ages.addAll(newGen);
            //System.out.println(format("Day %d (%d): %s", day, ages.size(), ages));
        }

        System.out.println(data);
        return ages.size();
    }

    public static void main(String[] args) {
        System.out.println(Day6.class);
        long count;
        //*
        count = new Day6(true).solve("day6_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 5934;

        count = new Day6(true).solve("day6.txt");
        System.out.println("Result: " + count);
        assert count == 372300;

        //*/

        count = new Day6(true).solve2("day6_test.txt");
        System.out.println("Result: " + count);
        assert count == 26984457539l;

        count = new Day6(true).solve2("day6.txt");
        System.out.println("Result: " + count);
        assert count == 444;
        //*/
    }
}
