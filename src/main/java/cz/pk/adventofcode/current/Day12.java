package cz.pk.adventofcode.current;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cz.pk.adventofcode.util.DataCollector;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Day12 {

    private final boolean debug;

    private int x = 0;
    private int y = 0;
    private MoveType direction = MoveType.EAST;

    enum MoveType {
        NORTH("N"),
        SOUTH("S"),
        EAST("E"),
        WEST("W"),
        LEFT("L"),
        RIGHT("R"),
        FORWARD("F");

        private String value;

        private static Map<String, MoveType> values;

        static {
            values = new HashMap<>();
            Arrays.stream(values()).forEach(p -> values.put(p.value, p));
        }

        MoveType(String place) {
            this.value = place;
        }

        public String toString() {
            return value;
        }

        public static MoveType get(String value) {
            return values.get(value);
        }
    }

    @Data
    @AllArgsConstructor
    class Move {
        MoveType moveType;
        int size;
    }

    class TypeCollector extends DataCollector<Move> {

        public TypeCollector(String file) {
            super(file);
        }

        @Override
        protected Move processLine(String line) {
            MoveType moveType = MoveType.get(String.valueOf(line.charAt(0)));
            Integer size = Integer.valueOf(line.substring(1));
            return new Move(moveType, size);
        }
    }

    public MoveType turnLeft(MoveType direction) {
        switch (direction) {
            case NORTH:
                return MoveType.WEST;
            case SOUTH:
                return MoveType.EAST;
            case EAST:
                return MoveType.NORTH;
            case WEST:
                return MoveType.SOUTH;
            default:
                throw new RuntimeException("x");
        }
    }

    public MoveType turnRight(MoveType direction) {
        switch (direction) {
            case NORTH:
                return MoveType.EAST;
            case SOUTH:
                return MoveType.WEST;
            case EAST:
                return MoveType.SOUTH;
            case WEST:
                return MoveType.NORTH;
            default:
                throw new RuntimeException("x");
        }
    }

    public int solve(String file) {
        Move[] moves = new TypeCollector(file).process().toArray(new Move[1]);
        System.out.println(moves);
        for(Move move : moves) {
            int times;
            switch (move.getMoveType()) {
                case NORTH:
                    y -= move.getSize();
                    break;
                case SOUTH:
                    y += move.getSize();
                    break;
                case EAST:
                    x += move.getSize();
                    break;
                case WEST:
                    x -= move.getSize();
                    break;
                case LEFT:
                    times = move.getSize() / 90;
                    for (int i=0;i<times;i++) {
                        direction = turnLeft(direction);
                    }
                    break;
                case RIGHT:
                    times = move.getSize() / 90;
                    for (int i=0;i<times;i++) {
                        direction = turnRight(direction);
                    }
                    break;
                case FORWARD:
                    switch (direction) {
                        case NORTH:
                            y -= move.getSize();
                            break;
                        case SOUTH:
                            y += move.getSize();
                            break;
                        case EAST:
                            x += move.getSize();
                            break;
                        case WEST:
                            x -= move.getSize();
                            break;
                    }
                    break;
            }
        }
        return Math.abs(x) + Math.abs(y);
    }

    public static void main(String[] args) {
//        int count = new Day12(true).solve("day12_test.txt");
//        System.out.println("Result: " + count);
//        assert count == 25;

        int count = new Day12(true).solve("day12.txt");
        System.out.println("Result: " + count);
        assert count == 845;
        // not 1697
        // 6:22

    }

}
