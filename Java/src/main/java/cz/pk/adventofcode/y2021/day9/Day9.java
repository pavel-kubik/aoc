package cz.pk.adventofcode.y2021.day9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.Matrix;
import cz.pk.adventofcode.util.Vector2;
import lombok.AllArgsConstructor;
import lombok.Data;

import static cz.pk.adventofcode.util.PKMath.isMin;
import static java.util.stream.Collectors.toList;

@Data
public class Day9 {

    private final boolean debug;

    private final Vector2<Integer>[] topology = new Vector2[]{
            new Vector2<>(1, 0),
            new Vector2<>(-1, 0),
            new Vector2<>(0, 1),
            new Vector2<>(0, -1)};

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

    public long solve(String file) {
        List<List<Integer>> matrixData = new NumberCollector(file).process();
        Matrix<Integer> data = Matrix.instance(matrixData);
        if (debug) System.out.println(data);

        Matrix<Integer> surround = data.applyOperation(
                (matrix, field) -> {
                    List<Integer> sur = Arrays.stream(topology)
                            .filter(neighbour -> matrix.get(field.plus(neighbour)) != null)
                            .map(neighbour -> matrix.get(field.plus(neighbour)))
                            .collect(toList());
                    if (isMin(matrix.get(field), sur)) {
                        return matrix.get(field) + 1;
                    } else {
                        return -1;
                    }
               });
        System.out.println(surround);

        int count = surround.map(0, (a, b) -> a + ((b != -1) ? b : 0));

        return count;
    }

    private void markBasin(Matrix<Integer> m, Vector2 position) {
        int base = m.get(position);
        m.set(position, -base); // mark as counted
        for (Vector2<Integer> neighbour : topology) {
            if (m.get(position.plus(neighbour)) != null && m.get(position.plus(neighbour)) > base) {
                // new point
                if (m.get(position.plus(neighbour)) < 9) {
                    markBasin(m, position.plus(neighbour));
                }
            }
        }
        //System.out.printf("(%d,%d): %d\n", x, y, base);
    }

    private void unmarkBasin(Matrix<Integer> matrix) {
        matrix.applyOperation((m, c) -> {
            if (m.get(c) < 0) {
                m.set(c, -m.get(c));
            } else {
                m.set(c, m.get(c));
            }
            return 0;
        });
    }

    public long solve2(String file) {
        List<List<Integer>> matrixData = new NumberCollector(file).process();
        Matrix<Integer> data = Matrix.instance(matrixData);
        if (debug) System.out.println(data);

        Matrix<Integer> surround = data.applyOperation(
                (matrix, field) -> {
                    List<Integer> sur = Arrays.stream(topology)
                            .filter(neighbour -> matrix.get(field.plus(neighbour)) != null)
                            .map(neighbour -> matrix.get(field.plus(neighbour)))
                            .collect(toList());
                    if (isMin(matrix.get(field), sur)) {
                        markBasin(matrix, field);
                        int count = matrix.map(0, (a, b) -> a + (b < 0 ? 1 : 0));
                        if (matrix.get(field) == 0) count++;
                        unmarkBasin(matrix);
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
        count = new Day9(true).solve("2021/day9_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 15;

        count = new Day9(false).solve("2021/day9.txt");
        System.out.println("Result: " + count);
        assert count == 545;

        //*/

        count = new Day9(true).solve2("2021/day9_test.txt");
        System.out.println("Result: " + count);
        assert count == 1134;

        count = new Day9(false).solve2("2021/day9.txt");
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
