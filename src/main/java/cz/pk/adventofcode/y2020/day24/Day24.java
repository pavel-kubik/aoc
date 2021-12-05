package cz.pk.adventofcode.y2020.day24;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.HexagonUtil;
import cz.pk.adventofcode.util.HexagonUtil.Direction;
import cz.pk.adventofcode.util.Matrix;
import cz.pk.adventofcode.util.Pair;
import lombok.Data;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static cz.pk.adventofcode.util.HexagonUtil.moveOnHexagon;
import static cz.pk.adventofcode.util.HexagonUtil.walkOnHexagon;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Data
public class Day24 {

    private final boolean debug;

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

    public long solve(String file) {
        List<List<Direction>> directions = new DirectionCollector(file).process();
        System.out.println(Matrix.instance(directions));

        System.out.println("test: " + parseLine("nwwswee"));
        System.out.println("Position: " + walkOnHexagon(parseLine("nwwswee")));

        List<Pair<Integer>> coordinates = directions.stream()
                .map(line -> walkOnHexagon(line))
                .collect(toList());
        System.out.println("Positions: " + coordinates);

        int minX = coordinates.stream().min((a, b) -> a.first.compareTo(b.first)).get().first;
        int maxX = coordinates.stream().max((a, b) -> a.first.compareTo(b.first)).get().first;
        int minY = coordinates.stream().min((a, b) -> a.second.compareTo(b.second)).get().second;
        int maxY = coordinates.stream().max((a, b) -> a.second.compareTo(b.second)).get().second;

        int[][] field = new int[maxX-minX+1][maxY-minY+1];
        for (Pair<Integer> coordinate : coordinates) {
            switchTile(field, -minX + coordinate.first, -minY + coordinate.second);
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
        List<List<Direction>> directions = new DirectionCollector(file).process();

        List<Pair<Integer>> coordinates = directions.stream()
                .map(line -> walkOnHexagon(line))
                .collect(toList());
        System.out.println("Positions: " + coordinates);

        int minX = coordinates.stream().min((a, b) -> a.first.compareTo(b.first)).get().first;
        int maxX = coordinates.stream().max((a, b) -> a.first.compareTo(b.first)).get().first;
        int minY = coordinates.stream().min((a, b) -> a.second.compareTo(b.second)).get().second;
        int maxY = coordinates.stream().max((a, b) -> a.second.compareTo(b.second)).get().second;

        int sizeX = maxX-minX+1;
        int sizeY = maxY-minY+1;
        int[][] field = new int[sizeX+5][sizeY+5];
        for (Pair<Integer> coordinate : coordinates) {
            switchTile(field, 3 - minX + coordinate.first, 3 - minY + coordinate.second);
        }
        // TODO read https://www.redblobgames.com/grids/hexagons/

        int count = 0;
        Matrix<Integer> fieldMatrix = Matrix.instance(Stream.of(field)
                .map(array -> IntStream.of(array).boxed().toArray(Integer[]::new))
                .toArray(Integer[][]::new));
        for (int day = 0; day < 2; day++) {
            System.out.println("Field:\n" + fieldMatrix);
            Matrix<Integer> surroundEven = fieldMatrix.convolution(
                    Matrix.instance(new Integer[][]{
                            {1, 1, 0},
                            {1, 0, 1},
                            {1, 1, 0}}),
                    (f, c) -> f * c,
                    0,
                    (a, b) -> a + b);
            Matrix<Integer> surroundOdd = fieldMatrix.convolution(
                    Matrix.instance(new Integer[][]{
                            {0, 1, 1},
                            {1, 0, 1},
                            {0, 1, 1}}),
                    (f, c) -> f * c,
                    0,
                    (a, b) -> a + b);
            Matrix<Integer> surround = surroundEven
                    .merge(surroundOdd, (i, j) -> i % 2 == 0);
            System.out.println("Surround:\n" + surround);
            fieldMatrix = fieldMatrix.applyMatrix(surround, (f, s) -> {
                if (f == 1) {
                    // black
                    if (s == 0 || s > 2) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else {
                    // white
                    if (s == 2) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            count = 0;
            for (int i = 0; i < fieldMatrix.getHeight(); i++) {
                for (int j = 0; j < fieldMatrix.getWidth(); j++) {
                    count += fieldMatrix.get(i, j);
                }
            }
            System.out.println(format("Day %d: %d", day+1, count));
        }

        return count;
    }

    public static void main(String[] args) {
        System.out.println(Day24.class);
        long count;
        //*
        count = new Day24(true).solve("2020/day24_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 10;

        count = new Day24(true).solve("2020/day24.txt");
        System.out.println("Result: " + count);
        assert count == 317;    //235

        //*/

        count = new Day24(true).solve2("2020/day24_test.txt");
        System.out.println("Result: " + count);
        assert count == 2208;

        count = new Day24(true).solve2("2020/day24.txt");
        System.out.println("Result: " + count);
        assert count == 444;    //17
        //*/
    }
}
