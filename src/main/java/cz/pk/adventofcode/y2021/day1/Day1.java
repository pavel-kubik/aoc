package cz.pk.adventofcode.y2021.day1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.LongCollector;
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
        List<Long> data = new LongCollector(file).process();
        long lastValue = data.get(0);
        int count = 0;
        for (int i=1;i<data.size();i++) {
            if (lastValue < data.get(i)) {
                count++;
            }
            lastValue = data.get(i);
        }
        System.out.println(data);
        return count;
    }

    public long solve2(String file) {
        List<Long> data2 = new LongCollector(file).process();
        List<Long> data = new ArrayList<>();
        for (int i=0;i<data2.size() - 2;i++) {
            data.add(data2.get(i) + data2.get(i+1) + data2.get(i+2));
        }
        long lastValue = data.get(0);
        int count = 0;
        for (int i=1;i<data.size();i++) {
            if (lastValue < data.get(i)) {
                count++;
            }
            lastValue = data.get(i);
        }
        System.out.println(data);
        return count;
    }

    public static void main(String[] args) {
        long count;
        //*
        count = new Day1(true).solve("2021/day1_test.txt");
        System.out.println("Result: " + count);
        assert count == 7;

        count = new Day1(true).solve("2021/day1.txt");
        System.out.println("Result: " + count);
        assert count == 1681;
        //*/
        count = new Day1(true).solve2("2021/day1_test.txt");
        System.out.println("Result: " + count);
        assert count == 5;

        count = new Day1(true).solve2("2021/day1.txt");
        System.out.println("Result: " + count);
        assert count == 1704;
        //*/
    }
}
