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
    private int dx = 10;
    private int dy = -1;
    private MoveType direction = MoveType.EAST;

    public static void main(String[] args) {
        int count;
        count = new Day12(true).solve("day12_test.txt");
        System.out.println("Result: " + count);
        assert count == 25;

        count = new Day12(true).solve("day12.txt");
        System.out.println("Result: " + count);
        assert count == 845;
        // not 1697 - bug turn n-times
        // 6:22

        count = new Day12(true).solve2("day12.txt");
        System.out.println("Result: " + count);
        assert count == 27016;
        // bug turn waypoint in other direction
        // 6:36
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
        for (Move move : moves) {
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
                    for (int i = 0; i < times; i++) {
                        direction = turnLeft(direction);
                    }
                    break;
                case RIGHT:
                    times = move.getSize() / 90;
                    for (int i = 0; i < times; i++) {
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

    public void turnWaypointLeft() {
        int newDx = dy;
        int newDy = -dx;
        dx = newDx;
        dy = newDy;
    }

    public void turnWaypointRight() {
        int newDx = -dy;
        int newDy = dx;
        dx = newDx;
        dy = newDy;
    }

    public int solve2(String file) {
        Move[] moves = new TypeCollector(file).process().toArray(new Move[1]);
        System.out.println(moves);
        for (Move move : moves) {
            int times;
            switch (move.getMoveType()) {
                case NORTH:
                    dy -= move.getSize();
                    break;
                case SOUTH:
                    dy += move.getSize();
                    break;
                case EAST:
                    dx += move.getSize();
                    break;
                case WEST:
                    dx -= move.getSize();
                    break;
                case LEFT:
                    times = move.getSize() / 90;
                    for (int i = 0; i < times; i++) {
                        turnWaypointLeft();
                    }
                    break;
                case RIGHT:
                    times = move.getSize() / 90;
                    for (int i = 0; i < times; i++) {
                        turnWaypointRight();
                    }
                    break;
                case FORWARD:
                    x += move.getSize() * dx;
                    y += move.getSize() * dy;
                    break;
            }
        }
        return Math.abs(x) + Math.abs(y);
    }

    enum MoveType {
        NORTH("N"),
        SOUTH("S"),
        EAST("E"),
        WEST("W"),
        LEFT("L"),
        RIGHT("R"),
        FORWARD("F");

        private static final Map<String, MoveType> values;

        static {
            values = new HashMap<>();
            Arrays.stream(values()).forEach(p -> values.put(p.value, p));
        }

        private final String value;

        MoveType(String place) {
            this.value = place;
        }

        public static MoveType get(String value) {
            return values.get(value);
        }

        public String toString() {
            return value;
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
}