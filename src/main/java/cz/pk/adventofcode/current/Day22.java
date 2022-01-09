package cz.pk.adventofcode.current;

import java.util.*;

import cz.pk.adventofcode.util.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static cz.pk.adventofcode.current.Day22.State.ON;

@Data
public class Day22 {

    private final boolean debug;

    @Data
    class ReactorCube {
        private State state;
        private Vector3<Integer> from;
        private Vector3<Integer> to;
        @EqualsAndHashCode.Exclude
        private int order;
        private Vector3<Double> center;
        private Vector3<Double> radius;

        public ReactorCube(State state, Vector3<Integer> from, Vector3<Integer> to, int order) {
            this.state = state;
            this.from = from;
            this.to = to;
            this.order = order;
            assert from.x <= to.x;
            assert from.y <= to.y;
            assert from.z <= to.z;
        }

        public boolean intersect(ReactorCube another) {
            double dcx = Math.abs(getCenter().x - another.getCenter().x);
            double dcy = Math.abs(getCenter().y - another.getCenter().y);
            double dcz = Math.abs(getCenter().z - another.getCenter().z);
            return dcx < getRadius().x + another.getRadius().x &&
                   dcy < getRadius().y + another.getRadius().y &&
                   dcz < getRadius().z + another.getRadius().z;
        }

        public ReactorCube cutBy(ReactorCube another) {
//            return this
//                    .copy()
//                    .setFrom(max(this.getFrom(), another.getFrom()))
//                    .setTo(min(this.getTo(), another.getTo().plus(new Vector3<Integer>(-1, -1, -1))));
            return new ReactorCube(
                    this.getState(),
                    max(this.getFrom(), another.getFrom()),
                    min(this.getTo(), another.getTo().plus(new Vector3<Integer>(-1, -1, -1))),
                    this.getOrder());
        }

        public Vector3<Double> getCenter() {
            if (center == null) {
                center = new Vector3<>(
                        (from.x + to.x) / 2.0,
                        (from.y + to.y) / 2.0,
                        (from.z + to.z) / 2.0);
            }
            return center;
        };

        public Vector3<Double> getRadius() {
            if (radius == null) {
                radius = new Vector3<>(
                        Math.abs(to.x - from.x) / 2.0,
                        Math.abs(to.y - from.y) / 2.0,
                        Math.abs(to.z - from.z) / 2.0);
            }
            return radius;
        };

        public List<ReactorCube> splitXHigh(List<Integer> xs) {
            List<ReactorCube> out = new ArrayList<>();
            Vector3<Integer> fromVector = this.from.copy();
            // split From - x1 - x2 - ... - To
            for (Integer x : xs) {
                out.add(new ReactorCube(this.getState(), fromVector, to.copy().setX(x), this.order));
                fromVector = fromVector.copy().setX(x+1);
            }
            out.add(new ReactorCube(this.getState(), fromVector, to.copy(), this.order));
            return out;
        }

        public List<ReactorCube> splitXLow(List<Integer> xs) {
            List<ReactorCube> out = new ArrayList<>();
            Vector3<Integer> fromVector = this.from.copy();
            // split From - x1 - x2 - ... - To
            for (Integer x : xs) {
                out.add(new ReactorCube(this.getState(), fromVector, to.copy().setX(x-1), this.order));
                fromVector = fromVector.copy().setX(x);
            }
            out.add(new ReactorCube(this.getState(), fromVector, to.copy(), this.order));
            return out;
        }

        public List<ReactorCube> splitYHigh(List<Integer> ys) {
            List<ReactorCube> out = new ArrayList<>();
            Vector3<Integer> fromVector = this.from.copy();
            for (Integer y : ys) {
                out.add(new ReactorCube(this.getState(), fromVector, to.copy().setY(y), this.order));
                fromVector = fromVector.copy().setY(y+1);
            }
            out.add(new ReactorCube(this.getState(), fromVector, to.copy(), this.order));
            return out;
        }

        public List<ReactorCube> splitYLow(List<Integer> ys) {
            List<ReactorCube> out = new ArrayList<>();
            Vector3<Integer> fromVector = this.from.copy();
            for (Integer y : ys) {
                out.add(new ReactorCube(this.getState(), fromVector, to.copy().setY(y-1), this.order));
                fromVector = fromVector.copy().setY(y);
            }
            out.add(new ReactorCube(this.getState(), fromVector, to.copy(), this.order));
            return out;
        }

        public List<ReactorCube> splitZHigh(List<Integer> zs) {
            List<ReactorCube> out = new ArrayList<>();
            Vector3<Integer> fromVector = this.from.copy();
            for (Integer z : zs) {
                out.add(new ReactorCube(this.getState(), fromVector, to.copy().setZ(z), this.order));
                fromVector = fromVector.copy().setZ(z+1);
            }
            out.add(new ReactorCube(this.getState(), fromVector, to.copy(), this.order));
            return out;
        }

        public List<ReactorCube> splitZLow(List<Integer> zs) {
            List<ReactorCube> out = new ArrayList<>();
            Vector3<Integer> fromVector = this.from.copy();
            for (Integer z : zs) {
                out.add(new ReactorCube(this.getState(), fromVector, to.copy().setZ(z-1), this.order));
                fromVector = fromVector.copy().setZ(z);
            }
            out.add(new ReactorCube(this.getState(), fromVector, to.copy(), this.order));
            return out;
        }

        public long getVolume() {
            long dx = Math.abs(to.x - from.x) + 1;
            long dy = Math.abs(to.y - from.y) + 1;
            long dz = Math.abs(to.z - from.z) + 1;
            return dx * dy * dz;
        }

        public ReactorCube copy() {
            return new ReactorCube(
                    state,
                    new Vector3<>(from.x, from.y, from.z),
                    new Vector3<>(to.x, to.y, to.z),
                    order);
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

        int order;

        public CubeCollector(String file) {
            super(file);
            order = 0;
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
            return new ReactorCube(state, from, to, order++);
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

    public long countQuadrant(Vector2<Vector3<Integer>> coordinates, List<ReactorCube> cubes) {
        // find intersections
        Set<Integer> intersectionsXLow = new TreeSet<>();
        Set<Integer> intersectionsXHigh = new TreeSet<>();
        Set<Integer> intersectionsYLow = new TreeSet<>();
        Set<Integer> intersectionsYHigh = new TreeSet<>();
        Set<Integer> intersectionsZLow = new TreeSet<>();
        Set<Integer> intersectionsZHigh = new TreeSet<>();
        for (ReactorCube reactorCube : cubes) {
            intersectionsXLow.add(reactorCube.getFrom().x);
            intersectionsXHigh.add(reactorCube.getTo().x);
            intersectionsYLow.add(reactorCube.getFrom().y);
            intersectionsYHigh.add(reactorCube.getTo().y);
            intersectionsZLow.add(reactorCube.getFrom().z);
            intersectionsZHigh.add(reactorCube.getTo().z);
        }

        // split cubes X
        if (debug) System.out.println("X high");
        List<ReactorCube> newCubes = new ArrayList<>();
        for (ReactorCube reactorCube : cubes) {
            List<Integer> dividingPoints = intersectionsXHigh.stream().filter(x -> x >= reactorCube.getFrom().x && x < reactorCube.getTo().x).toList();
            List<ReactorCube> split = reactorCube.splitXHigh(dividingPoints);
            if (debug) System.out.println("\nDividing High X: " + dividingPoints + reactorCube + " split to\n" + split);
            newCubes.addAll(split);
        }
        cubes = newCubes;

        if (debug) System.out.println("X low");
        newCubes = new ArrayList<>();
        for (ReactorCube reactorCube : cubes) {
            List<Integer> dividingPoints = intersectionsXLow.stream().filter(x -> x > reactorCube.getFrom().x && x <= reactorCube.getTo().x).toList();
            List<ReactorCube> split = reactorCube.splitXLow(dividingPoints);
            if (debug) System.out.println("\nDividing Low X: " + dividingPoints + reactorCube + " split to\n" + split);
            newCubes.addAll(split);
        }
        cubes = newCubes;


        // split cubes Y
        if (debug) System.out.println("Y high");
        newCubes = new ArrayList<>();
        for (ReactorCube reactorCube : cubes) {
            List<Integer> dividingPoints = intersectionsYHigh.stream().filter(y -> y >= reactorCube.getFrom().y && y < reactorCube.getTo().y).toList();
            List<ReactorCube> split = reactorCube.splitYHigh(dividingPoints);
            if (debug) System.out.println("\nDividing High Y: " + dividingPoints + reactorCube + " split to\n" + split);
            newCubes.addAll(split);
        }
        cubes = newCubes;

        if (debug) System.out.println("Y low");
        newCubes = new ArrayList<>();
        for (ReactorCube reactorCube : cubes) {
            List<Integer> dividingPoints = intersectionsYLow.stream().filter(y -> y > reactorCube.getFrom().y && y <= reactorCube.getTo().y).toList();
            List<ReactorCube> split = reactorCube.splitYLow(dividingPoints);
            if (debug) System.out.println("\nDividing Low Y: " + dividingPoints + reactorCube + " split to\n" + split);
            newCubes.addAll(split);
        }
        cubes = newCubes;


        // split cubes Z
        if (debug) System.out.println("Z high");
        newCubes = new ArrayList<>();
        int i = 0;
        for (ReactorCube reactorCube : cubes) {
            i++;
            List<Integer> dividingPoints = intersectionsZHigh.stream().filter(z -> z >= reactorCube.getFrom().z && z < reactorCube.getTo().z).toList();
            List<ReactorCube> split = reactorCube.splitZHigh(dividingPoints);
            if (debug) System.out.println("\nDividing High Z: " + dividingPoints + reactorCube + " split to\n" + split);
            newCubes.addAll(split);

        }
        cubes = newCubes;

        if (debug) System.out.println("Z low");
        newCubes = new ArrayList<>();
        for (ReactorCube reactorCube : cubes) {
            List<Integer> dividingPoints = intersectionsZLow.stream().filter(z -> z > reactorCube.getFrom().z && z <= reactorCube.getTo().z).toList();
            List<ReactorCube> split = reactorCube.splitZLow(dividingPoints);
            if (debug) System.out.println("\nDividing Low Z: " + dividingPoints + reactorCube + " split to\n" + split);
            newCubes.addAll(split);
        }
        cubes = newCubes;

        if (debug) System.out.println("Compute results");
        // reduce reactor cubes - if from-to match, last one win
        Map<Vector2<Vector3<Integer>>, List<ReactorCube>> cubesByFromTo = new HashMap<>();
        for (ReactorCube reactorCube : cubes) {
            cubesByFromTo.compute(new Vector2<>(reactorCube.getFrom(), reactorCube.getTo()), (k, v) -> {
                if (v == null) {
                    v = new ArrayList<>();
                }
                v.add(reactorCube);
                return v;
            });
        }

        long out = 0;
        for (List<ReactorCube> matchingCubes : cubesByFromTo.values()) {
            if (matchingCubes.get(matchingCubes.size()-1).getState() == ON) {
                out += matchingCubes.get(matchingCubes.size()-1).getVolume();
            }
        }
        return out;
    }

    public List<Vector2<Vector3<Integer>>> generateQuadrants(List<Integer> xs, List<Integer> ys, List<Integer> zs) {
        List<Vector2<Vector3<Integer>>> out = new ArrayList<>();
        for (int x = 1; x < xs.size(); x++) {
            for (int y = 1; y < ys.size(); y++) {
                for (int z = 1; z < zs.size(); z++) {
                    out.add(new Vector2<>(
                            new Vector3<>(xs.get(x-1), ys.get(y-1), zs.get(z-1)),
                            new Vector3<>(xs.get(x), ys.get(y), zs.get(z))));
                }
            }
        }
        return out;
    }

    public List<ReactorCube> extractQubesFromQuadrant(Vector2<Vector3<Integer>> quadrant, List<ReactorCube> cubes) {
        List<ReactorCube> out = new ArrayList<>();
        for (ReactorCube reactorCube : cubes) {
            ReactorCube quadrantCube = new ReactorCube(ON, quadrant.x, quadrant.y, -1);
            if (quadrantCube.intersect(reactorCube)) {
                out.add(reactorCube.cutBy(quadrantCube));
            }

        }
        return out;
    }

    public long solve2(String file) {
        CodeTimer ct = new CodeTimer();
        List<ReactorCube> cubes = new CubeCollector(file).process();
        System.out.println("Cubes count: " + cubes.size());

        TreeSet<Integer> coodrX = new TreeSet<>();
        TreeSet<Integer> coodrY = new TreeSet<>();
        TreeSet<Integer> coodrZ = new TreeSet<>();
        for (ReactorCube reactorCube : cubes) {
            coodrX.add(reactorCube.getFrom().x);
            coodrX.add(reactorCube.getTo().x);
            coodrY.add(reactorCube.getFrom().y);
            coodrY.add(reactorCube.getTo().y);
            coodrZ.add(reactorCube.getFrom().z);
            coodrZ.add(reactorCube.getTo().z);
        }
        System.out.println("X: " + coodrX.first() + " - " + coodrX.last());// + " " + coodrX);
        System.out.println("Y: " + coodrY.first() + " - " + coodrY.last());// + " " + coodrY);
        System.out.println("Z: " + coodrZ.first() + " - " + coodrZ.last());// + " " + coodrZ);

        // define quadrants
        List<Vector2<Vector3<Integer>>> quadrants = generateQuadrants(
                List.of(coodrX.first() - 1, -100, 100, 10000, coodrX.last() + 2),
                List.of(coodrY.first() - 1, -100, 100, coodrY.last() + 2),
                List.of(coodrZ.first() - 1, -100, 100, 10000, coodrZ.last() + 2));

        System.out.println("quadrants: " + quadrants);

        // divide cuboids to quadrants
        long out = 0;
        int q = 0;
        for (Vector2<Vector3<Integer>> quadrant : quadrants) {
            q++;
            ct.start("quadrant " + q);
            List<ReactorCube> localCubes = extractQubesFromQuadrant(quadrant, cubes);
            System.out.println("Count quadrant " + q + "/" + quadrants.size() + " " + quadrant + " with cubes " + localCubes.size());
            out += countQuadrant(quadrant, localCubes);
        }
        ct.stop();
        ct.log();
        return out;
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

//        count = new Day22(false).solve2("day22_test.txt");  // 20 cuboid
//        System.out.println("Result: " + count);
//        assert count == 590784;
//
//        count = new Day22(false).solve2("day22_test2.txt"); // 60 cuboid
//        System.out.println("Result: " + count);
//        assert count == 2758514936282235L;

        count = new Day22(false).solve2("day22.txt");       // 420 cuboid
        System.out.println("Result: " + count);
        assert count == 1177411289280259L;
        // low          1177411289280098L
        //              1177411289280259
        //*/

    }
}
