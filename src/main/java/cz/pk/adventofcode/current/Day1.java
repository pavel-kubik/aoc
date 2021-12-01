package cz.pk.adventofcode.current;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cz.pk.adventofcode.util.DataCollector;
import lombok.Data;

@Data
public class Day1 {

    private final boolean debug;

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

    class TypeCollector extends DataCollector<Integer> {

        public TypeCollector(String file) {
            super(file);
        }

        @Override
        protected Integer processLine(String line) {
            return Integer.valueOf(line);
        }
    }

    public long solve(String file) {
        Integer[] data = new TypeCollector(file).process().toArray(new Integer[1]);
        int lastValue = data[0];
        int count = 0;
        for (int i=1;i<data.length;i++) {
            if (lastValue < data[i]) {
                count++;
            }
            lastValue = data[i];
        }
        System.out.println(data);
        return count;
    }

    public long solve2(String file) {
        Integer[] data = new TypeCollector(file).process().toArray(new Integer[1]);
        System.out.println(data);
        return 0;
    }

    public static void main(String[] args) {
        long count;
        //*
        count = new Day1(true).solve("day1_test.txt");
        System.out.println("Result: " + count);
        assert count == 1;

        count = new Day1(true).solve("day1.txt");
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
