package cz.pk.adventofcode.y2022.day12;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.Matrix;
import cz.pk.adventofcode.util.Vector2;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

@Data
public class Day12 {

    private final boolean debug;

    private final Vector2<Integer>[] topology = new Vector2[]{
            new Vector2<>(1, 0),
            new Vector2<>(0, 1),
            new Vector2<>(-1, 0),
            new Vector2<>(0, -1)};

    class MapCollector extends DataCollector<List<Integer>> {

        public MapCollector(String file) {
            super(file);
        }

        @Override
        protected List<Integer> processLine(String line) {
            // numbers in line without separator - Subject: List<Integer>
            return new ArrayList<>(line.chars().mapToObj(c -> c).toList());
        }
    }

    @Data
    @AllArgsConstructor
    private static class Step implements Comparable<Step> {
        private Vector2<Integer> position;
        private Integer distance;

        @Override
        public int compareTo(Step o) {
            return distance.compareTo(o.getDistance());
        }
    }

    private long explorePath(Vector2<Integer> startPosition,
                             Integer shortestWay,
                             Matrix<Integer> map,
                             Matrix<Integer> mapDistance,
                             Integer bestWay) {
        long stepsCount = 0;
        PriorityQueue<Step> steps = new PriorityQueue<>();
        steps.add(new Step(startPosition, shortestWay));
        while (!steps.isEmpty()) {
            Step currentStep = steps.poll();
            steps.remove(currentStep);
            stepsCount++;

            Vector2<Integer> position = currentStep.getPosition();
            Integer distance = currentStep.getDistance();
            Integer current = map.get(position);
            for (Vector2<Integer> direction : topology) {
                Vector2<Integer> newPosition = position.plus(direction);
                if (map.get(newPosition) == null) {
                    continue;
                }
                if (mapDistance.get(newPosition) != null || map.get(newPosition).intValue() - current.intValue() > 1) {
                    continue;
                }
                Integer newAlt = map.get(newPosition);
                if (newAlt != null) {
                    mapDistance.set(newPosition, distance + 1);
                    steps.add(new Step(newPosition, distance + 1));
                    //System.out.println("New position " + newPosition);
                }
            }
        }
        return stepsCount;
    }

    public long solve(String file) {
        List<List<Integer>> data = new MapCollector(file).process();
        Matrix<Integer> map = Matrix.instance(data);
        Vector2<Integer> target = null;
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                if (map.get(i, j).intValue() == 'E') {
                    target = new Vector2<>(j, i);
                    map.set(i, j, (int)'z');
                    break;
                }
            }
            if (target != null) {
                break;
            }
        }
        System.out.println("Target " + target);

        Matrix<Integer> mapDistance = Matrix.instance(map.getWidth(), map.getHeight(), null);
        mapDistance.set(0, 0, 0);

        explorePath(new Vector2<>(0, 0), 0, map, mapDistance, Integer.MAX_VALUE);

        if (debug) System.out.println(mapDistance);

        return mapDistance.get(target);
    }

    public long solve2(String file) {
        List<List<Integer>> data = new MapCollector(file).process();
        List<Vector2<Integer>> starts = new ArrayList<>();

        Matrix<Integer> map = Matrix.instance(data);
        Vector2<Integer> target = null;
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                if (map.get(i, j).intValue() == 'a') {
                    starts.add(new Vector2<>(j, i));
                }
                if (map.get(i, j).intValue() == 'E') {
                    target = new Vector2<>(j, i);
                    map.set(i, j, (int)'z');
                }
            }
        }
        System.out.println("Target " + target);

        int min = Integer.MAX_VALUE;
        for (Vector2<Integer> start : starts) {
            map = Matrix.instance(data);
            System.out.println("Start " + start);

            Matrix<Integer> mapDistance = Matrix.instance(map.getWidth(), map.getHeight(), null);
            mapDistance.set(start.y, start.x, 0);

            explorePath(start, 0, map, mapDistance, Integer.MAX_VALUE);

            if (debug) System.out.println(mapDistance);

            Integer localMin = mapDistance.get(target);
            if (localMin != null && localMin < min) {
                System.out.println("New min " + localMin);
                System.out.println(mapDistance.toString());
                min = localMin;
            }
        }

        return min;
    }

    public static void main(String[] args) {
        System.out.println(Day12.class);
        long count;
        /*
        count = new Day12(true).solve("2022/day12_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 31;

        count = new Day12(false).solve("2022/day12.txt");
        System.out.println("Result: " + count);
        assert count == 423;

        /*/

        count = new Day12(false).solve2("2022/day12_test.txt");
        System.out.println("Result: " + count);
        assert count == 29;

        count = new Day12(false).solve2("2022/day12.txt");
        System.out.println("Result: " + count);
        assert count == 416;
        //*/
    }
}
