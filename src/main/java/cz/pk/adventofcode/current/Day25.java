package cz.pk.adventofcode.current;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.Matrix;
import cz.pk.adventofcode.util.StringCollector;
import cz.pk.adventofcode.util.Vector2;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static cz.pk.adventofcode.current.Day25.Cucumber.*;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day25 {

    private final boolean debug;

    enum Cucumber {
        EAST('>'),
        SOUTH('v'),
        EMPTY('.'),
        ;

        private static final Map<Character, Cucumber> values;

        static {
            values = new HashMap<>();
            Arrays.stream(values()).forEach(p -> values.put(p.value, p));
        }

        private final char value;

        Cucumber(char place) {
            this.value = place;
        }

        public static Cucumber get(char value) {
            return values.get(value);
        }

        public String toString() {
            return String.valueOf(value);
        }
    }

    class CucumberCollector extends DataCollector<Cucumber[]> {

        public CucumberCollector(String file) {
            super(file);
        }

        @Override
        protected Cucumber[] processLine(String line) {
            return line.chars().mapToObj(c -> (char)c).map(Cucumber::get).toList().toArray(Cucumber[]::new);
        }
    }

    private void print(Cucumber[][] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                sb.append(data[i][j]);
            }
            sb.append('\n');
        }
        System.out.println(sb.toString());
    }

    public long solve(String file) {
        Cucumber[][] data = new CucumberCollector(file).process().toArray(Cucumber[][]::new);
        Matrix<Cucumber> oldGen = Matrix.instance(data);
        if (debug) System.out.println(oldGen);

        int steps = 0;
        AtomicBoolean changed = new AtomicBoolean(true);
        while (changed.get()) {
            changed.set(false);
            // step
            Matrix<Cucumber> newGenEast = Matrix.instance(oldGen.getWidth(), oldGen.getHeight(), EMPTY);
            Matrix<Cucumber> newGenWest = Matrix.instance(oldGen.getWidth(), oldGen.getHeight(), EMPTY);
            oldGen.applyOperation((m, v) -> {
                Vector2<Integer> newPlace;
                switch (m.get(v)) {
                    case EAST:
                        newPlace = new Vector2<>((v.getX() + 1) % m.getWidth(), v.getY());
                        if (m.get(newPlace) == EMPTY) {
                            newGenEast.set(newPlace, EAST);
                            changed.set(true);
                        } else {
                            newGenEast.set(v, EAST);
                        }
                        break;
                    case SOUTH:
                        newGenEast.set(v, SOUTH);
                        break;
                }
                return null;
            });
            newGenEast.applyOperation((m, v) -> {
                Vector2<Integer> newPlace;
                switch (m.get(v)) {
                    case EAST:
                        newGenWest.set(v, EAST);
                        break;
                    case SOUTH:
                        newPlace = new Vector2<>(v.getX(), (v.getY() + 1) % m.getHeight());
                        if (m.get(newPlace) == EMPTY) {
                            newGenWest.set(newPlace, SOUTH);
                        } else {
                            newGenWest.set(v, SOUTH);
                        }
                        break;
                }
                return null;
            });
            oldGen = newGenWest;
            if (debug) System.out.println(newGenWest);
            steps++;
            if (!changed.get()) {
                break;
            }
        }

        return steps;
    }

    public long solve2(String file) {
        List<Integer> data = stream(new StringCollector(file)
                .process().get(0).split(",")).map(Integer::valueOf).collect(toList());
        System.out.println(data);
        return data.get(0)+22;
    }

    public static void main(String[] args) {
        System.out.println(Day25.class);
        long count;
        //*
        count = new Day25(true).solve("day25_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 58;

        count = new Day25(false).solve("day25.txt");
        System.out.println("Result: " + count);
        assert count == 406;

        //*/

//        count = new Day25(true).solve2("day25_test2.txt");
//        System.out.println("Result: " + count);
//        assert count == 33;
//
//        count = new Day25(true).solve2("day25.txt");
//        System.out.println("Result: " + count);
//        assert count == 44;
        //*/
    }
}
