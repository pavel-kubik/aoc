package cz.pk.adventofcode.y2020.day24;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.Pair;
import cz.pk.adventofcode.util.StringUtil;
import lombok.Data;

import java.util.*;

import static cz.pk.adventofcode.util.DataCollectorFactory.collectData;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day24 {

    private final boolean debug;

    enum Direction {
//        EAST("e", 1, 0),
//        SOUTHEAST("se", 1, 1),
//        SOUTHWEST("sw", -1, 1),
//        WEST("w", -1, 0),
//        NORTHEAST("ne", 1, -1),
//        NORTHWEST("nw", -1, -1),
        EAST("e", 1, 0),
        SOUTHEAST("se", 1, 1),
        SOUTHWEST("sw", 0, 1),
        WEST("w", -1, 0),
        NORTHEAST("ne", 0, -1),
        NORTHWEST("nw", -1, -1),
        ;

        private static final Map<String, Direction> values;

        static {
            values = new HashMap<>();
            Arrays.stream(values()).forEach(p -> values.put(p.value, p));
        }

        private final String value;
        private final int dx;   // for hexagon topology in 2D array
        private final int dy;

        Direction(String place, int dx, int dy) {
            this.value = place;
            this.dx = dx;
            this.dy = dy;
        }

        public static Direction get(String value) {
            return values.get(value);
        }

        public String toString() {
            return value;
        }
    }

    List<Direction> parseLine(String line) {
        List<Direction> directions = new ArrayList<>();
        while (!line.isEmpty()) {
            if (line.startsWith(Direction.EAST.toString())) {
                directions.add(Direction.EAST);
                line = line.substring(1);
            } else if (line.startsWith(Direction.WEST.toString())) {
                directions.add(Direction.WEST);
                line = line.substring(1);
            } else if (line.startsWith(Direction.NORTHEAST.toString())) {
                directions.add(Direction.NORTHEAST);
                line = line.substring(2);
            } else if (line.startsWith(Direction.NORTHWEST.toString())) {
                directions.add(Direction.NORTHWEST);
                line = line.substring(2);
            } else if (line.startsWith(Direction.SOUTHEAST.toString())) {
                directions.add(Direction.SOUTHEAST);
                line = line.substring(2);
            } else if (line.startsWith(Direction.SOUTHWEST.toString())) {
                directions.add(Direction.SOUTHWEST);
                line = line.substring(2);
            } else {
                throw new RuntimeException("Tile format error " + line);
            }
        }
        return directions;
    }

    class DirectionCollector extends DataCollector<List<Direction>> {

        public DirectionCollector(String file) {
            super(file);
        }

        @Override
        protected List<Direction> processLine(String line) {
            return parseLine(line);
        }
    }

    private void switchTile(int[][] field, int x, int y) {
        if (field[x][y] == 0) {
            field[x][y] = 1;
        } else {
            field[x][y] = 0;
        }
    }

    Pair<Integer> walkOnHexagon(List<Direction> directions) {
        int x = 0;
        int y = 0;
        for (Direction direction : directions) {
            x += direction.dx;
            y += direction.dy;
        }
        return new Pair<>(x, y);
    }

    public long solve(String file) {
        List<List<Direction>> directions = new DirectionCollector(file).process();
        System.out.println(directions);

        System.out.println("test: " + parseLine("nwwswee"));
        System.out.println("Position: " + walkOnHexagon(parseLine("nwwswee")));

        List<Pair<Integer>> coordinates = directions.stream()
                .map(line -> walkOnHexagon(line))
                .collect(toList());
        System.out.println("Positions: " + coordinates);

        int minX = coordinates.stream().min((a, b) -> a.a.compareTo(b.a)).get().a;
        int maxX = coordinates.stream().max((a, b) -> a.a.compareTo(b.a)).get().a;
        int minY = coordinates.stream().min((a, b) -> a.b.compareTo(b.b)).get().b;
        int maxY = coordinates.stream().max((a, b) -> a.b.compareTo(b.b)).get().b;

        int[][] field = new int[maxX-minX+1][maxY-minY+1];
        for (Pair<Integer> coordinate : coordinates) {
            switchTile(field, -minX + coordinate.a, -minY + coordinate.b);
        }

        int count = 0;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                count += field[i][j];
            }
        }

        return count;
    }

    public long solve2(String file) {
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(Day24.class);
        long count;
        /*
        count = new Day24(true).solve("2020/day24_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 10;

        count = new Day24(true).solve("2020/day24.txt");
        System.out.println("Result: " + count);
        assert count == 317;    //235

        /*/

        count = new Day24(true).solve2("day_test.txt");
        System.out.println("Result: " + count);
        assert count == 333;

        count = new Day24(true).solve2("day.txt");
        System.out.println("Result: " + count);
        assert count == 444;
        //*/
    }
}
