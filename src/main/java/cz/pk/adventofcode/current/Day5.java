package cz.pk.adventofcode.current;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.pk.adventofcode.util.DataCollector;
import lombok.AllArgsConstructor;
import lombok.Data;

import static cz.pk.adventofcode.util.DataCollectorFactory.collectData;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day5 {

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
        // general data structure
        //Subject[] data = new TypeCollector(file).process().toArray(new Subject[1]);
        // string lines
        //List<String> data = new StringCollector(file).process();
        // matrix
        List<List<Long>> data = collectData(
                file,
                (line) -> stream(line.split(" ")).map(Long::parseLong).collect(toList()));

        System.out.println(data);
        return 0;
    }

    public long solve2(String file) {
        Subject[] data = new TypeCollector(file).process().toArray(new Subject[1]);
        System.out.println(data);
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(Day5.class);
        long count;
        //*
        count = new Day5(true).solve("day5_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 111;

        count = new Day5(true).solve("day5.txt");
        System.out.println("Result: " + count);
        assert count == 222;

        /*/

        count = new Day5(true).solve2("day5_test.txt");
        System.out.println("Result: " + count);
        assert count == 333;

        count = new Day5(true).solve2("day5.txt");
        System.out.println("Result: " + count);
        assert count == 444;
        //*/
    }
}
