package cz.pk.adventofcode.current;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cz.pk.adventofcode.util.DataCollector;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Day2 {

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
        Subject[] data = new TypeCollector(file).process().toArray(new Subject[1]);
        System.out.println(data);
        return 0;
    }

    public long solve2(String file) {
        Subject[] data = new TypeCollector(file).process().toArray(new Subject[1]);
        System.out.println(data);
        return 0;
    }

    public static void main(String[] args) {
        long count;
        //*
        count = new Day2(true).solve("day2_test.txt");
        System.out.println("Result: " + count);
        //assert count == 1;

        count = new Day2(true).solve("day2.txt");
        System.out.println("Result: " + count);
        //assert count == 845;

        /*/

        count = new Day2(true).solve2("day2_test.txt");
        System.out.println("Result: " + count);
        //assert count == 25;

        count = new Day2(true).solve2("day2.txt");
        System.out.println("Result: " + count);
        //assert count == 27016;

         */
        //*/
    }
}
