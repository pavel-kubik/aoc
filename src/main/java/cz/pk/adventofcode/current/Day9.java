package cz.pk.adventofcode.current;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.Matrix;
import cz.pk.adventofcode.util.StringCollector;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

import static java.util.Arrays.stream;
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

    private boolean isMin(List<Integer> vals, Integer value) {
        for (Integer v : vals) {
            if (value >= v) {
                return false;
            }
        }
        return true;
    }

    public long solve(String file) {
        // string lines
        List<String> in = new StringCollector(file)
                        .process();
        List<List<Integer>> mtx = new ArrayList<>();
        for (String line : in) {
            mtx.add(line.chars().mapToObj(c -> (char)c).map(c -> Integer.parseInt(c.toString())).collect(toList()));
        }
        Matrix<Integer> data = Matrix.instance(mtx);
        System.out.println(data);

        Matrix<Integer> surround = data.applyOperation(
                (m, c) -> {
                    int x = c.first;
                    int y = c.second;
                    List<Integer> sur = new ArrayList<>();
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            if (i==0 && j == 0) continue;
                            if (m.get(x + i, y + j) != null) {
                                sur.add(m.get(x + i, y + j));
                            }
                        }
                    }
                    if (isMin(sur, m.get(x, y))) {
                        return m.get(x, y);
                    } else {
                        return -1;
                    }
               });
        System.out.println(surround);

        int count = surround.map(0, (a, b) -> a + ((b != -1) ? b + 1 : 0));

        return count;
    }

    private void markBasis(Matrix<Integer> m, int x, int y, int base) {
        m.set(x, y, -base); // mark as counted
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                if (dx==0 && dy == 0) continue;
                if (m.get(x + dx, y + dy) != null && m.get(x + dx, y + dy) == base + 1) {
                    // new point
                    if (base + 1 < 9) {
                        markBasis(m, x + dx, y + dy, base + 1);
                    }
                }
            }
        }
        //System.out.printf("(%d,%d): %d\n", x, y, base);
    }

    private void unmarkBasis(Matrix<Integer> matrix) {
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
        // string lines
        List<String> in = new StringCollector(file)
                .process();
        List<List<Integer>> mtx = new ArrayList<>();
        for (String line : in) {
            mtx.add(line.chars().mapToObj(c -> (char)c).map(c -> Integer.parseInt(c.toString())).collect(toList()));
        }
        Matrix<Integer> data = Matrix.instance(mtx);
        System.out.println(data);

        Matrix<Integer> surround = data.applyOperation(
                (m, c) -> {
                    int x = c.first;
                    int y = c.second;
                    List<Integer> sur = new ArrayList<>();
                    for (int dx = -1; dx < 2; dx++) {
                        for (int dy = -1; dy < 2; dy++) {
                            if (dx==0 && dy == 0) continue;
                            if (m.get(x + dx, y + dy) != null) {
                                sur.add(m.get(x + dx, y + dy));
                            }
                        }
                    }
                    if (isMin(sur, m.get(x, y))) {
                        int base = m.get(x, y);
                        System.out.println("Count base " + x + "," + y);
                        markBasis(m, x, y, base);
                        //System.out.println(m);
                        int count = m.map(0, (a, b) -> a + (b < 0 ? 1 : 0));
                        if (m.get(x, y) == 0) count++;
                        //System.out.println("Size: " + count);
                        unmarkBasis(m);
                        //System.out.println(m);
                        return count;
                    } else {
                        return -1;
                    }
                });
        System.out.println(surround);

        List<Integer> basinsSize = new ArrayList<>();
        int count = surround.map(0, (a, b) -> {
            if (b > 0) {
                basinsSize.add(b);
            }
            return 0;
        });

        System.out.println(basinsSize);

        Collections.sort(basinsSize, Collections.reverseOrder());

        return basinsSize.get(0) * basinsSize.get(1) * basinsSize.get(2);
    }

    public static void main(String[] args) {
        System.out.println(Day9.class);
        long count;
        /*
        count = new Day9(true).solve("day9_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 15;

        count = new Day9(true).solve("day9.txt");
        System.out.println("Result: " + count);
        assert count == 545;

        //*/

        count = new Day9(true).solve2("day9_test.txt");
        System.out.println("Result: " + count);
        assert count == 1134;

        count = new Day9(true).solve2("day9.txt");
        System.out.println("Result: " + count);
        assert count == 44; //>931491
        //*/
    }
}
