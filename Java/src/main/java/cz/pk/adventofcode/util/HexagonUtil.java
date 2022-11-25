package cz.pk.adventofcode.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * Map hexagon to cube
 *
 * https://www.redblobgames.com/grids/hexagons/
 */
public class HexagonUtil {

    public enum Direction {
        EAST("e", 1, 0, -1),
        SOUTHEAST("se", 0, 1, -1),
        SOUTHWEST("sw", -1, 1, 0),
        WEST("w", -1, 0, 1),
        NORTHEAST("ne", 1, -1, 0),
        NORTHWEST("nw", 0, -1, 1),
        ;

        private static final Map<String, Direction> values;

        static {
            values = new HashMap<>();
            Arrays.stream(values()).forEach(p -> values.put(p.value, p));
        }

        private final String value;
        private final int q;
        private final int r;
        private final int s;

        Direction(String place, int q, int r, int s) {
            this.value = place;
            this.q = q;
            this.r = r;
            this.s = s;
        }

        public static Direction get(String value) {
            return values.get(value);
        }

        @Override
        public String toString() {
            return value;
        }

        public int getQ() {
            return q;
        }

        public int getR() {
            return r;
        }

        public int getS() {
            return s;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HexagonCoordinate {
        int q;
        int r;
        int s;

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("[").append(q).append(",").append(r).append(",").append(s).append("]").toString();
        }

        public HexagonCoordinate plus(HexagonCoordinate that) {
            return new HexagonCoordinate(q + that.getQ(), r + that.getR(), s + that.getS());
        }
    }

    public static HexagonCoordinate moveOnHexagon(Direction direction, HexagonCoordinate coordinate) {
        return new HexagonCoordinate(
                coordinate.q + direction.getQ(),
                coordinate.r + direction.getR(),
                coordinate.s + direction.getS());
    }

    public static HexagonCoordinate walkOnHexagon(List<Direction> directions) {
        HexagonCoordinate currentPoint = new HexagonCoordinate();
        for (Direction direction : directions) {
            currentPoint = moveOnHexagon(direction, currentPoint);
        }
        return currentPoint;
    }

}
