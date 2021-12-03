package cz.pk.adventofcode.current;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.StringCollector;
import lombok.AllArgsConstructor;
import lombok.Data;

import static java.util.Arrays.stream;

@Data
public class Day3 {

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
            stream(values()).forEach(p -> values.put(p.value, p));
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
        List<String> data = new StringCollector(file).process();
        int gamma = 0;
        int epsilon = 0;
        int rad = data.get(0).length();
        for (int i=0;i<data.get(0).length();i++) {
            int count0 = 0;
            int count1 = 0;
            for (int j=0;j<data.size();j++) {
                if (data.get(j).getBytes()[i] == '0') {
                    count0++;
                } else {
                    count1++;
                }
            }
            if (count0 > count1) {
                epsilon += Math.pow(2, rad - 1);
            } else {
                gamma += Math.pow(2, rad - 1);
            }
            rad--;
        }

        System.out.println(data);
        System.out.println("gamma: " + gamma);
        System.out.println("epsilon: " + epsilon);
        return gamma*epsilon;
    }

    public long solve2(String file) {
        Subject[] data = new TypeCollector(file).process().toArray(new Subject[1]);
        System.out.println(data);
        return 0;
    }

    public static void main(String[] args) {
        long count;
        //*
        count = new Day3(true).solve("day3_test.txt");
        System.out.println("Result: " + count);
        assert count == 198;

        count = new Day3(true).solve("day3.txt");
        System.out.println("Result: " + count);
        assert count == 2261546;

        /*/

        count = new Day3(true).solve2("day3_test.txt");
        System.out.println("Result: " + count);
        assert count == 3;

        count = new Day3(true).solve2("day3.txt");
        System.out.println("Result: " + count);
        assert count == 20213;
        //*/
    }
}
