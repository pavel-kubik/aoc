package cz.pk.adventofcode.current;

import cz.pk.adventofcode.util.Cube;
import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.StringCollector;
import cz.pk.adventofcode.util.Vector3;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cz.pk.adventofcode.current.Day22.State.OFF;
import static cz.pk.adventofcode.current.Day22.State.ON;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day22 {

    private final boolean debug;

    @Data
    @AllArgsConstructor
    class ReactorCube {
        private State state;
        private Vector3<Integer> from;
        private Vector3<Integer> to;

        @Override
        public String toString() {
            return "\nReactorCube{" +
                    "state=" + state +
                    ", from=" + from +
                    ", to=" + to +
                    '}';
        }
    }

    enum State {
        ON("on"),
        OFF("off"),
        ;

        private static final Map<String, State> values;

        static {
            values = new HashMap<>();
            Arrays.stream(values()).forEach(p -> values.put(p.value, p));
        }

        private final String value;

        State(String place) {
            this.value = place;
        }

        public static State get(String value) {
            return values.get(value);
        }

        public String toString() {
            return value;
        }
    }

    class CubeCollector extends DataCollector<ReactorCube> {

        public CubeCollector(String file) {
            super(file);
        }

        @Override
        protected ReactorCube processLine(String line) {
            //on x=-20..26,y=-36..17,z=-47..7
            String[] parts = line.split(" ");
            State state = State.get(parts[0]);
            String[] coordinates = parts[1].split(",");

            String[] x = coordinates[0].substring(2).split("\\.\\.");
            String[] y = coordinates[1].substring(2).split("\\.\\.");
            String[] z = coordinates[2].substring(2).split("\\.\\.");

            Vector3<Integer> from = new Vector3<>(
                    Integer.parseInt(x[0]),
                    Integer.parseInt(y[0]),
                    Integer.parseInt(z[0]));
            Vector3<Integer> to = new Vector3<>(
                    Integer.parseInt(x[1]),
                    Integer.parseInt(y[1]),
                    Integer.parseInt(z[1]));
            return new ReactorCube(state, from, to);
        }
    }

    private void fillCube(ReactorCube reactorCube, Cube<Integer> reactor, int OFFSET) {
        for (int i = reactorCube.getFrom().getX(); i <= reactorCube.getTo().getX(); i++) {
            for (int j = reactorCube.getFrom().getY(); j <= reactorCube.getTo().getY(); j++) {
                for (int k = reactorCube.getFrom().getZ(); k <= reactorCube.getTo().getZ(); k++) {
                    try {
                        reactor.set(i + OFFSET, j + OFFSET, k + OFFSET, reactorCube.getState().equals(ON) ? 1 : 0);
                    } catch (IndexOutOfBoundsException e) {
                        return;
                    }
                }
            }
        }
    }

    public long solve(String file) {
        List<ReactorCube> cubes = new CubeCollector(file).process();
        System.out.println(cubes);

        int OFFSET = 50;
        Cube<Integer> reactor = Cube.instance(110, 110, 110, 0);
        for (ReactorCube reactorCube : cubes) {
            fillCube(reactorCube, reactor, OFFSET);
        }
        return reactor.map(0, (s, v) -> s + v);
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
        count = new Day22(true).solve("day22_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 590784;

        count = new Day22(true).solve("day22.txt");
        System.out.println("Result: " + count);
        assert count == 22;

        //*/

        count = new Day22(true).solve2("day22_test.txt");
        System.out.println("Result: " + count);
        assert count == 33;

        count = new Day22(true).solve2("day22.txt");
        System.out.println("Result: " + count);
        assert count == 44;
        //*/

        assert "VM options -ea set. You can delete this.".isEmpty();
        throw new RuntimeException("Don't forgot to set vm option -ea");
    }
}
