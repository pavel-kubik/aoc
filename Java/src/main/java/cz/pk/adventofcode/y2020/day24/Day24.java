package cz.pk.adventofcode.y2020.day24;

import cz.pk.adventofcode.util.*;
import cz.pk.adventofcode.util.HexagonUtil.Direction;
import cz.pk.adventofcode.util.HexagonUtil.HexagonCoordinate;
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

    private void switchTile(Cube<Integer> field, HexagonCoordinate coordinate, int offset) {
        if (field.get(coordinate.getQ() + offset, coordinate.getR() + offset, coordinate.getS() + offset).equals(0)) {
            field.set(coordinate.getQ() + offset, coordinate.getR() + offset, coordinate.getS() + offset, 1);
        } else {
            field.set(coordinate.getQ() + offset, coordinate.getR() + offset, coordinate.getS() + offset, 0);
        }
    }

    private int findMaxDimension(List<HexagonCoordinate> coordinates) {
        int minX = 0, maxX = 0, minY = 0, maxY = 0, minZ = 0, maxZ = 0;
        for (HexagonCoordinate coordinate : coordinates) {
            if (coordinate.getQ() < minX) minX = coordinate.getQ();
            if (coordinate.getQ() > maxX) maxX = coordinate.getQ();
            if (coordinate.getR() < minY) minY = coordinate.getR();
            if (coordinate.getR() > maxY) maxY = coordinate.getR();
            if (coordinate.getS() < minZ) minZ = coordinate.getS();
            if (coordinate.getS() > maxZ) maxZ = coordinate.getS();
        }
        int dimX = maxX - minX + 1;
        int dimY = maxY - minY + 1;
        int dimZ = maxZ - minZ + 1;
        return  Math.max(dimX, Math.max(dimY, dimZ));
    }

    public long solve(String file) {
        List<List<Direction>> directions = new DirectionCollector(file).process();
        //System.out.println(Matrix.instance(directions));

        //System.out.println("test: " + parseLine("nwwswee"));
        //System.out.println("Position: " + walkOnHexagon(parseLine("nwwswee")));

        List<HexagonCoordinate> coordinates = directions.stream()
                .map(line -> walkOnHexagon(line))
                .collect(toList());
        //System.out.println("Positions: " + coordinates);

        int dim = findMaxDimension(coordinates);

        Cube<Integer> field = Cube.instance(dim, 0);
        for (HexagonCoordinate coordinate : coordinates) {
            switchTile(field, coordinate, dim / 2);
        }

        int count = field.map(0, (a, b) -> a + b);

        return count;
    }

    public long solve2(String file) {
        List<List<Direction>> directions = new DirectionCollector(file).process();

        List<HexagonCoordinate> coordinates = directions.stream()
                .map(line -> walkOnHexagon(line))
                .collect(toList());
        //System.out.println("Positions: " + coordinates);

        int dim = findMaxDimension(coordinates) + 202;

        Cube<Integer> field = Cube.instance(dim, 0);
        for (HexagonCoordinate coordinate : coordinates) {
            switchTile(field, coordinate, dim / 2);
        }

        int count = field.map(0, (a, b) -> a + b);

        //System.out.println("Field:\n" + field);
        for (int day = 0; day < 100; day++) {
            field = field.applyOperation((Cube<Integer> cube, HexagonCoordinate coordinate) -> {
                int s = 0;  // number of black in surround
                for (Direction direction : Direction.values()) {
                    HexagonCoordinate delta = new HexagonCoordinate(direction.getQ(), direction.getR(), direction.getS());
                    s += cube.get(coordinate.plus(delta));
                }
                if (cube.get(coordinate) == 1) {
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
            //System.out.println("Field:\n" + field);

            count = field.map(0, (a, b) -> a + b);
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
        assert count == 3804;    //17
        //*/
    }
}
