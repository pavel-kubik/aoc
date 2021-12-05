package cz.pk.adventofcode.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class HexagonUtil {

    public enum Direction {
        EAST("e"),
        SOUTHEAST("se"),
        SOUTHWEST("sw"),
        WEST("w"),
        NORTHEAST("ne"),
        NORTHWEST("nw"),
        ;

        private static final Map<String, Direction> values;

        static {
            values = new HashMap<>();
            Arrays.stream(values()).forEach(p -> values.put(p.value, p));
        }

        private final String value;

        Direction(String place) {
            this.value = place;
        }

        public static Direction get(String value) {
            return values.get(value);
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static Pair<Integer> moveOnHexagon(Direction direction, Pair<Integer> coordinate) {
        int dx = 0;
        int dy = 0;
        switch (direction) {
            case EAST:
                dx = 1;
                break;
            case WEST:
                dx = -1;
                break;
            case NORTHEAST:
                dy = -1;
                if (coordinate.second % 2 != 0) {
                    dx = 1;
                }
                break;
            case NORTHWEST:
                dy = -1;
                if (coordinate.second % 2 == 0) {
                    dx = -1;
                }
                break;
            case SOUTHEAST:
                dy = 1;
                if (coordinate.second % 2 != 0) {
                    dx = 1;
                }
                break;
            case SOUTHWEST:
                dy = 1;
                if (coordinate.second % 2 == 0) {
                    dx = -1;
                }
                break;
            default:
                throw new RuntimeException(format("Unknown direction %s", direction));
        }
        return new Pair<>(coordinate.first + dx, coordinate.second + dy);
    }

    public static Pair<Integer> walkOnHexagon(List<Direction> directions) {
        Pair<Integer> currentPoint = new Pair<>(0, 0);
        for (Direction direction : directions) {
            currentPoint = moveOnHexagon(direction, currentPoint);
        }
        return currentPoint;
    }

}
