package cz.pk.adventofcode.y2021.day3;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.StringCollector;
import lombok.AllArgsConstructor;
import lombok.Data;

import static java.lang.String.format;
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

        //System.out.println(data);
        System.out.println("gamma: " + gamma);
        System.out.println("epsilon: " + epsilon);
        return gamma*epsilon;
    }

    public long solve2(String file) {
        List<String> data = new StringCollector(file).process();
        String oxygen = "";
        String co2 = "";
        for (int i=0;i<data.get(0).length();i++) {
            data = reduce(data, i);
            if (data.size() == 1) {
                oxygen = data.get(0);
                break;
            }
        }

        data = new StringCollector(file).process();
        for (int i=0;i<data.get(0).length();i++) {
            data = reduceCO2(data, i);
            if (data.size() == 1) {
                co2 = data.get(0);
                break;
            }
        }

        System.out.println("oxygen: " + oxygen);
        System.out.println("co2: " + co2);
        // convert bin string to number
        return new BigInteger(oxygen, 2).longValue()*new BigInteger(co2, 2).longValue();
    }

    private List<String> reduce(List<String> input, int index) {
        List<String> out = new ArrayList<>();
        int count0 = 0;
        int count1 = 0;
        for (int j=0;j<input.size();j++) {
            if (input.get(j).getBytes()[index] == '0') {
                count0++;
            } else {
                count1++;
            }
        }
        for (int j=0;j<input.size();j++) {
            if (input.get(j).getBytes()[index] == (count0 > count1 ? '0' : '1')) {
                out.add(input.get(j));
            }
        }
        //System.out.println(format("Index %s: %s", index, out));
        return out;
    }

    private List<String> reduceCO2(List<String> input, int index) {
        List<String> out = new ArrayList<>();
        int count0 = 0;
        int count1 = 0;
        for (int j=0;j<input.size();j++) {
            if (input.get(j).getBytes()[index] == '0') {
                count0++;
            } else {
                count1++;
            }
        }
        for (int j=0;j<input.size();j++) {
            if (input.get(j).getBytes()[index] == (count0 <= count1 ? '0' : '1')) {
                out.add(input.get(j));
            }
        }
        //System.out.println(format("Index %s: %s", index, out));
        return out;
    }

    public static void main(String[] args) {
        System.out.println(Day3.class);
        long count;
        //*
        count = new Day3(true).solve("2021/day3_test.txt");
        System.out.println("Result: " + count);
        assert count == 198;

        count = new Day3(true).solve("2021/day3.txt");
        System.out.println("Result: " + count);
        assert count == 2261546;

        //*/

        count = new Day3(true).solve2("2021/day3_test.txt");
        System.out.println("Result: " + count);
        //assert count == 0;

        count = new Day3(true).solve2("2021/day3.txt");
        System.out.println("Result: " + count);
        //assert count == 2261546;    // not 8177765, 14361714, 6773824
        //*/
    }
}
