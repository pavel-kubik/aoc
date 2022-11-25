package cz.pk.adventofcode.y2021.day15;

import cz.pk.adventofcode.util.CodeTimer;
import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.Matrix;
import cz.pk.adventofcode.util.Vector2;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.util.*;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day15 {

    private final boolean debug;

    private final Vector2<Integer>[] topology = new Vector2[]{
            new Vector2<>(1, 0),
            new Vector2<>(0, 1),
            new Vector2<>(-1, 0),
            new Vector2<>(0, -1)};

    class CaveCollector extends DataCollector<List<Integer>> {

        public CaveCollector(String file) {
            super(file);
        }

        @Override
        protected List<Integer> processLine(String line) {
            // numbers in line without separator - Subject: List<Integer>
            return line.chars().mapToObj(c -> (char)c).map(c -> Integer.parseInt(c.toString())).collect(toList());
        }
    }

    @Data
    @AllArgsConstructor
    private static class Step implements Comparable<Step> {
        private Vector2<Integer> position;
        private Integer risk;

        @Override
        public int compareTo(Step o) {
            return risk.compareTo(o.getRisk());
        }
    }

    private long explorePath(Vector2<Integer> startPosition,
                             Integer startRisk,
                             Matrix<Integer> caveRisk,
                             Matrix<Integer> pathRisk,
                             Integer bestWay) {
        long stepsCount = 0;
        PriorityQueue<Step> steps = new PriorityQueue<>();
        steps.add(new Step(startPosition, startRisk));
        while (!steps.isEmpty()) {
            Step currentStep = steps.poll();
            steps.remove(currentStep);
            stepsCount++;

            Vector2<Integer> position = currentStep.getPosition();
            Integer risk = currentStep.getRisk();
            for (Vector2<Integer> direction : topology) {
                Vector2<Integer> newPosition = position.plus(direction);
                Integer riskAtNewPosition = caveRisk.get(position.plus(direction));
                if (riskAtNewPosition != null) {
                    if (risk + riskAtNewPosition < pathRisk.get(newPosition)) {
                        pathRisk.set(newPosition, risk + riskAtNewPosition);
                        if (risk + riskAtNewPosition < bestWay) {
                            steps.add(new Step(newPosition, risk + riskAtNewPosition));
                            //System.out.println("New position " + newPosition);
                        }
                    }
                }
            }
            if (pathRisk.get(pathRisk.getWidth() - 1, pathRisk.getHeight() - 1).compareTo(bestWay) < 0) {
                bestWay = pathRisk.get(pathRisk.getWidth() - 1, pathRisk.getHeight() - 1);
                System.out.println("Steps: " + steps.size());
                System.out.println("New best way is " + bestWay);
            }
        }
        return stepsCount;
    }

    public long solve(String file) {
        List<List<Integer>> data = new CaveCollector(file).process();
        Matrix<Integer> caveRisk = Matrix.instance(data);

        Matrix<Integer> pathRisk = Matrix.instance(caveRisk.getWidth(), caveRisk.getHeight(), Integer.MAX_VALUE);
        pathRisk.set(0, 0, 0);

        Integer[][] risks = new Integer[data.size()][data.get(0).size()];
        explorePath(new Vector2<Integer>(0, 0), 0, caveRisk, pathRisk, Integer.MAX_VALUE);

        if (debug) System.out.println(pathRisk);

        return pathRisk.get(pathRisk.getWidth() - 1, pathRisk.getHeight() - 1);
    }

    public long solve2(String file) {
        CodeTimer ct = new CodeTimer();
        ct.start("impl");
        List<List<Integer>> data = new CaveCollector(file).process();
        Matrix<Integer> caveTemplateRisk = Matrix.instance(data);
        Matrix<Integer> caveRisk = Matrix.instance(caveTemplateRisk.getWidth()*5, caveTemplateRisk.getHeight()*5, -1);
        caveTemplateRisk.applyOperation((m, pos) -> {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    caveRisk.set(
                            j*caveTemplateRisk.getHeight()+pos.getY(),
                            i*caveTemplateRisk.getWidth()+pos.getX(),
                            m.get(pos) + i + j > 9 ? m.get(pos) + i + j - 9 : m.get(pos) + i + j);
                }
            }
            return null;
        });

        if (debug) System.out.println(caveRisk);
        Matrix<Integer> pathRisk = Matrix.instance(caveRisk.getWidth(), caveRisk.getHeight(), 3000);
        pathRisk.set(0, 0, 0);  // don't go back to [0, 0]

        long steps = explorePath(new Vector2<Integer>(0, 0), 0, caveRisk, pathRisk, Integer.MAX_VALUE);
        System.out.println("Steps: " + steps);

        if (debug) System.out.println(pathRisk);
        ct.stop();
        ct.log();
        return pathRisk.get(pathRisk.getWidth() - 1, pathRisk.getHeight() - 1);
    }

    public static void main(String[] args) {
        System.out.println(Day15.class);
        long count;
        //*
        count = new Day15(true).solve("2021/day15_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 40;

        count = new Day15(false).solve("2021/day15.txt");
        System.out.println("Result: " + count);
        assert count == 403;
        // < 404 - two direction only

        //*/

        count = new Day15(false).solve2("2021/day15_test.txt");
        System.out.println("Result: " + count);
        assert count == 315;

        count = new Day15(false).solve2("2021/day15.txt");
        System.out.println("Result: " + count);
        assert count == 2840;
        //*/
    }
}
