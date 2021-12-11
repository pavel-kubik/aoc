package cz.pk.adventofcode.current;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.Matrix;
import cz.pk.adventofcode.util.StringCollector;
import cz.pk.adventofcode.util.Vector2;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day11 {

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

    class TypeCollector extends DataCollector<List<Integer>> {

        public TypeCollector(String file) {
            super(file);
        }

        @Override
        protected List<Integer> processLine(String line) {
            return line.chars().mapToObj(c -> (char)c).map(c -> Integer.parseInt(c.toString())).collect(toList());
        }
    }

    private final Vector2<Integer>[] topology = new Vector2[]{
            new Vector2<>(1, 0),
            new Vector2<>(-1, 0),
            new Vector2<>(0, 1),
            new Vector2<>(0, -1),
            new Vector2<>(1, 1),
            new Vector2<>(-1, -1),
            new Vector2<>(1, -1),
            new Vector2<>(-1, 1),
    };


    public long solve(String file) {
        // matrix
        List<List<Integer>> data = new TypeCollector(file).process();
        Matrix<Integer> octopusMatrix = Matrix.instance(data);
        System.out.println(octopusMatrix);

        long flashesSum = 0;

        for (int i = 0; i < 100; i++) {
            // increase all
            octopusMatrix.applyOperation((m, idx) -> {
                m.set(idx, m.get(idx) + 1);
                return null;
            });
            // flashes
            long flashCountAtStep = 0;
            do {
                Matrix<Integer> flashes = octopusMatrix.applyOperation((m, idx) -> {
                    int flash = 0;
                    if (m.get(idx) > 9) {
                        m.set(idx, 0);
                        flash = 1;
                        // flash -> increase neighbors
                        Arrays.stream(topology).forEach(
                                diff -> {
                                    Vector2<Integer> field = idx.plus(diff);
                                    if (m.get(field) != null && m.get(field) > 0) {
                                        m.set(field, m.get(field) + 1);
                                    }
                                }
                        );
                    }
                    return flash;
                });
                flashCountAtStep = flashes.map(0, (sum, m) -> sum + m);
                flashesSum += flashCountAtStep;
            } while (flashCountAtStep > 0);
            //System.out.println(octopusMatrix);
        }

        return flashesSum;
    }

    public long solve2(String file) {
        // matrix
        List<List<Integer>> data = new TypeCollector(file).process();
        Matrix<Integer> octopusMatrix = Matrix.instance(data);
        System.out.println(octopusMatrix);

        int step = 0;
        long flashesSum = 0;
        do {
            step++;
            // increase all
            octopusMatrix.applyOperation((m, idx) -> {
                m.set(idx, m.get(idx) + 1);
                return null;
            });
            // flashes
            long flashCountAtStep = 0;
            flashesSum = 0;
            do {
                Matrix<Integer> flashes = octopusMatrix.applyOperation((m, idx) -> {
                    int flash = 0;
                    if (m.get(idx) > 9) {
                        m.set(idx, 0);
                        flash = 1;
                        // flash -> increase neighbors
                        Arrays.stream(topology).forEach(
                                diff -> {
                                    Vector2<Integer> field = idx.plus(diff);
                                    if (m.get(field) != null && m.get(field) > 0) {
                                        m.set(field, m.get(field) + 1);
                                    }
                                }
                        );
                    }
                    return flash;
                });
                flashCountAtStep = flashes.map(0, (sum, m) -> sum + m);
                flashesSum += flashCountAtStep;
            } while (flashCountAtStep > 0);
            System.out.println("Step " + step + ": " + flashesSum);
        } while (flashesSum != octopusMatrix.getWidth()*octopusMatrix.getHeight());

        return step;
    }

    public static void main(String[] args) {
        System.out.println(Day11.class);
        long count;
        //*
        count = new Day11(true).solve("day11_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 1656;

        count = new Day11(true).solve("day11.txt");
        System.out.println("Result: " + count);
        assert count == 1667;

        //*/

        count = new Day11(true).solve2("day11_test.txt");
        System.out.println("Result: " + count);
        assert count == 195;

        count = new Day11(true).solve2("day11.txt");
        System.out.println("Result: " + count);
        assert count == 44;
        //*/
    }
}
