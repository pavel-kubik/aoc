package cz.pk.adventofcode.current;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.Matrix;
import cz.pk.adventofcode.util.Vector2;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

import static java.util.Arrays.stream;

@Data
public class Day17 {

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
            // numbers in line without separator - Subject: List<Integer>
            //return line.chars().mapToObj(c -> (char)c).map(c -> Integer.parseInt(c.toString())).collect(toList());

            // numbers in line with separator - Subject: List<Integer>
            // decimal - can be any base - change 2nd argument in parseInt
            //return stream(line.split(" ")).map(n -> Integer.parseInt(n, 10)).collect(toList());

            Type type = Type.get(String.valueOf(line.charAt(0)));
            Integer size = Integer.valueOf(line.substring(1));
            return new Subject(type, size);
        }
    }

    private Vector2<Integer> moveProbe(Vector2<Integer> initialPosition, Vector2<Integer> diff) {
        Vector2<Integer> newPosition = initialPosition.plus(diff);
        return newPosition;
    }

    private Vector2<Integer> updateDiff(Vector2<Integer> diff) {
        return new Vector2<>(diff.getX() > 0 ? diff.getX() - 1 : diff.getX(), diff.getY() - 1);
    }

    private boolean inTarget(Vector2<Integer> position, int x1, int y1, int x2, int y2) {
        return position.getX() >= x1 && position.getX() <= x2 &&
                position.getY() >= y1 && position.getY() <= y2;
    }

    private boolean inOver(Vector2<Integer> position, Vector2<Integer> diff, int x1, int y1, int x2, int y2) {
        return position.getX() > x2 ||
                position.getY() < y1 && diff.getY() < 0;
    }

    private void markTarget(Matrix<String> field, int offset, int x1, int y1, int x2, int y2) {
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                field.set(offset - y, x, "T");
            }
        }
    }

    public long solve(int x1, int y1, int x2, int y2) {
        int maxY = Integer.MIN_VALUE;
        int guessDiffXMin = (int) Math.floor(Math.sqrt(2*x1));
        int guessDiffXMax = (int) Math.ceil(Math.sqrt(2*x2));
        int guessDiffYMin = 0;
        int guessDiffYMax = -y1;
        for (int x = guessDiffXMin; x <= guessDiffXMax; x++) {
            for (int y = guessDiffYMin; y <= guessDiffYMax; y++) {
//                Matrix<String> field = Matrix.instance(x2+1, -2*y1+70, ".");
//                int offset = field.getHeight()+y1-1;
//                field.set(offset, 0, "S");
//                markTarget(field, offset, x1, y1, x2, y2);
                Vector2<Integer> currentPosition = new Vector2<>(0, 0);
                Vector2<Integer> diff = new Vector2<>(x, y);
                System.out.printf("Check step (%d, %d)\n", x, y);
                int localMaxY2 = 0;
                while (!inOver(currentPosition, diff, x1, y1, x2, y2)) {
                    if (localMaxY2 < currentPosition.getY()) {
                        localMaxY2 = currentPosition.getY();
                    }
                    if (inTarget(currentPosition, x1, y1, x2, y2)) {
                        if (maxY < localMaxY2) {
                            maxY = localMaxY2;
                        }
                        System.out.printf("IT WORKS FOR step (%d, %d) - max y was %d\n", x, y, localMaxY2);
//                        System.out.println(field.toShortString());
                        break;
                    }
                    currentPosition = moveProbe(currentPosition, diff);
                    diff = updateDiff(diff);
//                    field.set(offset - currentPosition.getY(), currentPosition.getX(), "#");
                }
//                if (inOver(currentPosition, diff, x1, y1, x2, y2)) {
//                    System.out.println(field.toShortString());
//                }
            }
        }

        return maxY;
    }

    public long solve2(int x1, int y1, int x2, int y2) {
        int guessDiffXMin = (int) Math.floor(Math.sqrt(2*x1));
        int guessDiffXMax = x2;
        int guessDiffYMin = y1;
        int guessDiffYMax = -y1;
        long counts = 0;
        for (int x = guessDiffXMin; x <= guessDiffXMax; x++) {
            for (int y = guessDiffYMin; y <= guessDiffYMax; y++) {
                //Matrix<String> field = Matrix.instance(x2+1, -2*y1+70, ".");
                //int offset = field.getHeight()+y1-1;
                //field.set(offset, 0, "S");
                //markTarget(field, offset, x1, y1, x2, y2);
                Vector2<Integer> currentPosition = new Vector2<>(0, 0);
                Vector2<Integer> diff = new Vector2<>(x, y);
                //System.out.printf("Check step (%d, %d)\n", x, y);
                int localMaxY2 = 0;
                while (!inOver(currentPosition, diff, x1, y1, x2, y2)) {
                    if (localMaxY2 < currentPosition.getY()) {
                        localMaxY2 = currentPosition.getY();
                    }
                    if (inTarget(currentPosition, x1, y1, x2, y2)) {
                        System.out.printf("IT WORKS FOR step (%d, %d) - max y was %d\n", x, y, localMaxY2);
                        //System.out.println(field.toShortString());
                        counts++;
                        break;
                    }
                    currentPosition = moveProbe(currentPosition, diff);
                    diff = updateDiff(diff);
                    //field.set(offset - currentPosition.getY(), currentPosition.getX(), "#");
                }
//                if (inOver(currentPosition, diff, x1, y1, x2, y2)) {
//                    System.out.println(field.toShortString());
//                }
            }
        }

        return counts;
    }

    public static void main(String[] args) {
        System.out.println(Day17.class);
        long count;
        //*
        count = new Day17(true).solve(20, -10,30, -5);
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 45;

        count = new Day17(true).solve(135, -102,155, -78);
        System.out.println("Result: " + count);
        assert count == 5151;

        //*/

        count = new Day17(true).solve2(20, -10,30, -5);
        System.out.println("Result: " + count);
        assert count == 112;

        count = new Day17(true).solve2(135, -102,155, -78);
        System.out.println("Result: " + count);
        assert count == 968;
        //*/
    }
}
