package cz.pk.adventofcode.current;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.Matrix;
import cz.pk.adventofcode.util.Pair;
import cz.pk.adventofcode.util.StringCollector;
import lombok.AllArgsConstructor;
import lombok.Data;

import static java.util.stream.Collectors.toList;

@Data
public class Day9 {

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

    class NumberCollector extends DataCollector<List<Integer>> {

        public NumberCollector(String file) {
            super(file);
        }

        @Override
        protected List<Integer> processLine(String line) {
            return line.chars().mapToObj(c -> (char)c).map(c -> Integer.parseInt(c.toString())).collect(toList());
        }
    }

    private boolean isMin(List<Integer> vals, Integer value) {
        for (Integer v : vals) {
            if (v != null) {
                if (value >= v) {
                    return false;
                }
            }
        }
        return true;
    }

    public long solve(String file) {
        List<List<Integer>> matrixData = new NumberCollector(file).process();
        Matrix<Integer> data = Matrix.instance(matrixData);
        if (debug) System.out.println(data);

        Matrix<Integer> surround = data.applyOperation(
                (m, c) -> {
                    int x = c.first;
                    int y = c.second;
                    List<Integer> sur = new ArrayList<>();
                    sur.add(m.get(x + 1, y));
                    sur.add(m.get(x - 1, y));
                    sur.add(m.get(x, y + 1));
                    sur.add(m.get(x, y - 1));
                    if (isMin(sur, m.get(x, y))) {
                        return m.get(x, y) + 1;
                    } else {
                        return -1;
                    }
               });
        System.out.println(surround);

        int count = surround.map(0, (a, b) -> a + ((b != -1) ? b : 0));

        return count;
    }

    private void markBasin(Matrix<Integer> m, int x, int y) {
        int base = m.get(x, y);
        m.set(x, y, -base); // mark as counted
        Pair<Integer>[] points = new Pair[]{
                new Pair<>(1, 0),
                new Pair<>(-1, 0),
                new Pair<>(0, 1),
                new Pair<>(0, -1),
        };
        for (Pair<Integer> point : points) {
            int dx = point.first;
            int dy = point.second;
            if (m.get(x + dx, y + dy) != null && m.get(x + dx, y + dy) > base) {
                // new point
                if (m.get(x + dx, y + dy) < 9) {
                    markBasin(m, x + dx, y + dy);
                }
            }
        }
        //System.out.printf("(%d,%d): %d\n", x, y, base);
    }

    private void unmarkBasin(Matrix<Integer> matrix) {
        matrix.applyOperation((m, c) -> {
            if (m.get(c.first, c.second) < 0) {
                m.set(c.first, c.second, -m.get(c.first, c.second));
            } else {
                m.set(c.first, c.second, m.get(c.first, c.second));
            }
            return 0;
        });
    }

    public long solve2(String file) {
        List<List<Integer>> matrixData = new NumberCollector(file).process();
        Matrix<Integer> data = Matrix.instance(matrixData);
        if (debug) System.out.println(data);

        Matrix<Integer> surround = data.applyOperation(
                (m, c) -> {
                    int x = c.first;
                    int y = c.second;
                    List<Integer> sur = new ArrayList<>();
                    sur.add(m.get(x + 1, y));
                    sur.add(m.get(x - 1, y));
                    sur.add(m.get(x, y + 1));
                    sur.add(m.get(x, y - 1));
                    if (isMin(sur, m.get(x, y))) {
                        if (debug) System.out.println("Count base " + x + "," + y);
                        markBasin(m, x, y);
                        if (debug) System.out.print(m);
                        int count = m.map(0, (a, b) -> a + (b < 0 ? 1 : 0));
                        if (m.get(x, y) == 0) count++;
                        if (debug) System.out.printf("Size: %d\n\n", count);
                        unmarkBasin(m);
                        if (debug) System.out.println(m);
                        return count;
                    } else {
                        return -1;
                    }
                });
        if (debug) System.out.println(surround);

        List<Integer> basinsSize = surround.map(new ArrayList(), (a, b) -> {
            if (b > 0) {
                a.add(b);
            }
            return a;
        });

        Collections.sort(basinsSize, Collections.reverseOrder());
        System.out.println(basinsSize);

        return basinsSize.get(0) * basinsSize.get(1) * basinsSize.get(2);
    }

    public static void main(String[] args) {
        System.out.println(Day9.class);
        long count;
        //*
        count = new Day9(true).solve("day9_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 15;

        count = new Day9(false).solve("day9.txt");
        System.out.println("Result: " + count);
        assert count == 545;

        //*/

        count = new Day9(true).solve2("day9_test.txt");
        System.out.println("Result: " + count);
        assert count == 1134;

        count = new Day9(false).solve2("day9.txt");
        System.out.println("Result: " + count);
        assert count == 950600; //>931491 (99, 97, 97), not 950796 (99, 98, 98)
        // not 941094 (99, 98, 97)
        // not 960400 (100, 98, 98)
        // not 912576
        // not 388360 ;-(
        // not 812912
        // not 839325
        // 950600
    }
}
