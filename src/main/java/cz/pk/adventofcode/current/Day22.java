package cz.pk.adventofcode.current;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.pk.adventofcode.util.Cube;
import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.Vector3;
import lombok.AllArgsConstructor;
import lombok.Data;

import static cz.pk.adventofcode.current.Day22.State.ON;

@Data
public class Day22 {

    private final boolean debug;

    @Data
    @AllArgsConstructor
    class ReactorCube {
        private State state;
        private Vector3<Integer> from;
        private Vector3<Integer> to;

        public boolean intersect(ReactorCube another) {
            if (from.x <= another.from.x && another.from.x <= to.x &&
                from.y <= another.from.y && another.from.y <= to.y &&
                from.z <= another.from.z && another.from.z <= to.z ||
                from.x <= another.to.x && another.to.x <= to.x &&
                from.y <= another.to.y && another.to.y <= to.y &&
                from.z <= another.to.z && another.to.z <= to.z
            ) {
                return true;
            }
            return false;
        }

        public ReactorCube copy() {
            return new ReactorCube(
                    state,
                    new Vector3<>(from.x, from.y, from.z),
                    new Vector3<>(to.x, to.y, to.z));
        }

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
        //System.out.println(cubes);

        int OFFSET = 50;
        Cube<Integer> reactor = Cube.instance(110, 110, 110, 0);
        for (ReactorCube reactorCube : cubes) {
            fillCube(reactorCube, reactor, OFFSET);
        }
        return reactor.map(0, (s, v) -> s + v);
    }

    private Vector3<Integer> min(Vector3<Integer> v1, Vector3<Integer> v2) {
        return new Vector3<>(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y), Math.min(v1.z, v2.z));
    }

    private Vector3<Integer> max(Vector3<Integer> v1, Vector3<Integer> v2) {
        return new Vector3<>(Math.max(v1.x, v2.x), Math.max(v1.y, v2.y), Math.max(v1.z, v2.z));
    }

    private long size(Vector3<Integer> v1, Vector3<Integer> v2) {
        return Math.abs(v1.x - v2.x) * Math.abs(v1.y - v2.y) * Math.abs(v1.z - v2.z);
    }

    public long solve2(String file) {
        List<ReactorCube> cubes = new CubeCollector(file).process();
        //System.out.println(cubes);

        int OFFSET = 50;
        Cube<Integer> reactor = Cube.instance(110, 110, 110, 0);
        Map<ReactorCube, List<ReactorCube>> rcGroups = new HashMap<>();
        for (ReactorCube reactorCube : cubes) {
            // find matching group
            boolean found = false;
            for (ReactorCube groupKey : rcGroups.keySet()) {
                if (reactorCube.intersect(groupKey)) {
                    List<ReactorCube> rcGroup = rcGroups.get(groupKey);
                    rcGroups.remove(groupKey);
                    rcGroup.add(reactorCube);
                    groupKey.from = min(groupKey.from, reactorCube.from);
                    groupKey.to = max(groupKey.to, reactorCube.to);
                    rcGroups.put(groupKey, rcGroup);
                    found = true;
                    break;
                }
            }
            if (!found) {
                rcGroups.put(reactorCube.copy(), new ArrayList<>(List.of(reactorCube)));
            }
        }
        System.out.println(rcGroups);
        //
        long size = 0;
        for (ReactorCube groupKey : rcGroups.keySet()) {
            long groupSize = size(groupKey.from, groupKey.to);
            System.out.println("Group " + groupKey + " size " + groupSize);
            size += groupSize;
        }
        System.out.println(size);

        return reactor.map(0, (s, v) -> s + v);
    }

    public static void main(String[] args) {
        System.out.println(Day22.class);
        long count;
        /*
        count = new Day22(true).solve("day22_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 590784;

        count = new Day22(true).solve("day22.txt");
        System.out.println("Result: " + count);
        assert count == 568000;

        //*/

//        count = new Day22(true).solve2("day22_test2.txt");
//        System.out.println("Result: " + count);
//        assert count == 33;

        count = new Day22(true).solve2("day22.txt");
        System.out.println("Result: " + count);
        assert count == 44;
        //*/

        assert "VM options -ea set. You can delete this.".isEmpty();
        throw new RuntimeException("Don't forgot to set vm option -ea");
    }
}
