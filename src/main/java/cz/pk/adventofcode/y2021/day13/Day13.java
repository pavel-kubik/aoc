package cz.pk.adventofcode.y2021.day13;

import cz.pk.adventofcode.util.Matrix;
import cz.pk.adventofcode.util.StringCollector;
import cz.pk.adventofcode.util.Vector2;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day13 {

    private final boolean debug;

    @Data
    @AllArgsConstructor
    class FoldInstruction {
        private Axis axis;
        private int size;
    }

    enum Axis {
        HORIZONTAL("y"),
        VERTICAL("x"),
        ;

        private static final Map<String, Axis> values;

        static {
            values = new HashMap<>();
            Arrays.stream(values()).forEach(p -> values.put(p.value, p));
        }

        private final String value;

        Axis(String place) {
            this.value = place;
        }

        public static Axis get(String value) {
            return values.get(value);
        }

        public String toString() {
            return value;
        }
    }

    public long solve(String file, boolean stopAfterFirstFold) {
        // load data
        List<String> data = new StringCollector(file).process();
        List<Vector2<Integer>> dots = new ArrayList<>();
        List<FoldInstruction> foldInstructions = new ArrayList<>();
        boolean readDost = true;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isEmpty()) {
                readDost = false;
                continue;
            }
            if (readDost) {
                String[] dotCoordinate = data.get(i).split(",");
                dots.add(new Vector2<>(Integer.valueOf(dotCoordinate[0]), Integer.valueOf(dotCoordinate[1])));
            } else {
                String[] foldLine = data.get(i).split("=");
                String foldAxis = foldLine[0].split(" ")[2];
                Integer foldPosition = Integer.valueOf(foldLine[1]);
                foldInstructions.add(new FoldInstruction(Axis.get(foldAxis), foldPosition));
            }
        }
        System.out.println("Dots (" + dots.size() + ")");
        if (debug) System.out.println(dots);
        System.out.println("Folds (" + foldInstructions.size() + ")");
        if (debug) System.out.println(foldInstructions);

        // fold field
        Set<Vector2<Integer>> uniqueDots = new HashSet<>();
        for (FoldInstruction foldInstruction : foldInstructions) {
            for (Vector2<Integer> dot : dots) {
                if (foldInstruction.getAxis() == Axis.VERTICAL) {
                    if (dot.x > foldInstruction.getSize()) {
                        dot.x = 2*foldInstruction.getSize() - dot.x;
                    }
                } else {
                    if (dot.y > foldInstruction.getSize()) {
                        dot.y = 2*foldInstruction.getSize() - dot.y;
                    }
                }
            }
            if (debug) System.out.println(dots);
            uniqueDots.clear();
            uniqueDots.addAll(dots);
            dots = uniqueDots.stream().toList();
            if (stopAfterFirstFold) {
                break;
            }
        }

        int maxX = 0;
        int maxY = 0;
        for (Vector2<Integer> dot : dots) {
            if (maxX < dot.x) maxX = dot.x;
            if (maxY < dot.y) maxY = dot.y;
        }
        Matrix<String> out = Matrix.instance(maxX+1, maxY+1, ".");
        for (Vector2<Integer> dot : uniqueDots) {
            out.set(dot, "#");
        }
        System.out.println(out);

        return uniqueDots.size();
    }

    public static void main(String[] args) {
        System.out.println(Day13.class);
        long count;
        //*
        count = new Day13(true).solve("2021/day13_test.txt", true);
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 17;

        count = new Day13(false).solve("2021/day13.txt", true);
        System.out.println("Result: " + count);
        assert count == 687; //not 835

        //*/

        count = new Day13(true).solve("2021/day13_test.txt", false);
        System.out.println("Result: " + count);
        assert count == 16;

        count = new Day13(false).solve("2021/day13.txt", false);
        System.out.println("Result: " + count);
        assert count == 98;
        //*/
    }
}
