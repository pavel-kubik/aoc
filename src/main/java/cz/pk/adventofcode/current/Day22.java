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
public class Day22 {

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

    public long solve(String file) {
        // general data structure
        //Subject[] data = new TypeCollector(file).process().toArray(new Subject[1]);

        // string lines
        //List<String> data = new StringCollector(file).process();

        // 1 line separated by ,
        List<Integer> data = stream(new StringCollector(file)
                .process().get(0).split(",")).map(Integer::valueOf).collect(toList());

        // matrix
        //List<List<Long>> data = collectData(
        //        file,
        //        (line) -> stream(line.split(" ")).map(Long::parseLong).collect(toList()));
        //Matrix<Long> m = Matrix.instance(data);
        //m.map(0l, (a, b) -> a + b);

        System.out.println(data);
        return data.get(0);
    }

    public long solve2(String file) {
        List<Integer> data = stream(new StringCollector(file)
                .process().get(0).split(",")).map(Integer::valueOf).collect(toList());
        System.out.println(data);
        return data.get(0)+22;
    }

    public static void main(String[] args) {
        System.out.println(Day22.class);
        long count;
        //*
        count = new Day22(true).solve("day_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 11;

        count = new Day22(true).solve("day.txt");
        System.out.println("Result: " + count);
        assert count == 22;

        //*/

        count = new Day22(true).solve2("day_test.txt");
        System.out.println("Result: " + count);
        assert count == 33;

        count = new Day22(true).solve2("day.txt");
        System.out.println("Result: " + count);
        assert count == 44;
        //*/

        assert "VM options -ea set. You can delete this.".isEmpty();
        throw new RuntimeException("Don't forgot to set vm option -ea");
    }
}
