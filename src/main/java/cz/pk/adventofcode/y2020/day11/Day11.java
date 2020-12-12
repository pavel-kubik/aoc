package cz.pk.adventofcode.y2020.day11;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.Matrix;
import cz.pk.adventofcode.util.MatrixUtil;
import cz.pk.adventofcode.util.PuzzleSolver2D;
import lombok.Data;

import static java.util.stream.Collectors.toList;

@Data
public class Day11 {
    private final boolean debug;

    MatrixUtil<Place> matrixUtil = new MatrixUtil<>(Place.class);
    private Place[][] places;

    private static Place switchPlace(Place place) {
        switch (place) {
            case OCCUPIED_SEAT:
                return Place.FREE_SEAT;
            case FREE_SEAT:
                return Place.OCCUPIED_SEAT;
            default:
                return place;
        }
    }

    public static void main(String[] args) throws IOException {
        //TODO fix matrixes
//        int result = new Day11(true).solvePart1WithMatrix("2020/day11_test.txt");
//        System.out.println("Result " + result);
//        assert result == 37;

        int count = new Day11(true).countFreePlaces("2020/day11_test.txt");
        System.out.println("Result: " + count);
        assert count == 37;

        count = new Day11(false).countFreePlaces("2020/day11.txt");
        System.out.println("Result: " + count);
        assert count == 2296;
        // not 2662
        // not 2163

        count = new Day11(true).countFreePlacesLongDistance("2020/day11_test.txt");
        System.out.println("Result: " + count);
        assert count == 26;

        count = new Day11(false).countFreePlacesLongDistance("2020/day11.txt");
        System.out.println("Result: " + count);
        assert count == 2089;
    }

    private Place[][] step(Place[][] places) {
        Place[][] out = new Place[places.length][];
        for (int i = 0; i < places.length; i++) {
            out[i] = new Place[places[i].length];
            for (int j = 0; j < places[i].length; j++) {
                switch (places[i][j]) {
                    case FREE_SEAT:
                        if (occupiedArround(i, j, places) == 0) {
                            out[i][j] = Place.OCCUPIED_SEAT;
                        } else {
                            out[i][j] = Place.FREE_SEAT;
                        }
                        break;
                    case OCCUPIED_SEAT:
                        if (occupiedArround(i, j, places) >= 4) {
                            out[i][j] = Place.FREE_SEAT;
                        } else {
                            out[i][j] = Place.OCCUPIED_SEAT;
                        }
                        break;
                    case EMPTY:
                        out[i][j] = Place.EMPTY;
                        break;
                    default:
                        assert false;
                }
            }
        }
        return out;
    }

    private int occupiedArround(int x, int y, Place[][] places) {
        int out = 0;
        out += isOccupied(x - 1, y - 1, places) ? 1 : 0;
        out += isOccupied(x, y - 1, places) ? 1 : 0;
        out += isOccupied(x + 1, y - 1, places) ? 1 : 0;
        out += isOccupied(x + 1, y + 1, places) ? 1 : 0;
        out += isOccupied(x, y + 1, places) ? 1 : 0;
        out += isOccupied(x - 1, y + 1, places) ? 1 : 0;
        out += isOccupied(x - 1, y, places) ? 1 : 0;
        out += isOccupied(x + 1, y, places) ? 1 : 0;
        return out;
    }

    private boolean isOccupied(int x, int y, Place[][] places) {
        if (x >= 0 && y >= 0 && x < places.length && y < places[0].length) {
            return places[x][y] == Place.OCCUPIED_SEAT;
        } else {
            return false;
        }
    }

    public int countFreePlaces(String file) throws IOException {
        places = new PlaceCollector(file).process().toArray(new Place[1][1]);
        if (isDebug()) {
            System.out.println(printPlaces(places));
            System.out.println();
        }

        Place[][] newPlaces = places;
        do {
            places = newPlaces;
            newPlaces = step(places);
            if (isDebug()) {
                System.out.println();
                System.out.println(printPlaces(newPlaces));
            }
        } while (!isEquals(places, newPlaces));

        return count(newPlaces, Place.OCCUPIED_SEAT);
    }

    private String printPlaces(Place[][] places) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < places.length; i++) {
            for (int j = 0; j < places[i].length; j++) {
                sb.append(places[i][j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private boolean isEquals(Place[][] places1, Place[][] places2) {
        for (int i = 0; i < places1.length; i++) {
            for (int j = 0; j < places1[i].length; j++) {
                if (places1[i][j] != places2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private int count(Place[][] place1, Place place) {
        int out = 0;
        for (int i = 0; i < place1.length; i++) {
            for (int j = 0; j < place1[i].length; j++) {
                if (place1[i][j] == place) {
                    out++;
                }
            }
        }
        return out;
    }

    private boolean isOccupiedLongDistance(int x, int y, int dx, int dy, Place[][] places) {
        if (x + dx >= 0 && y + dy >= 0 && x + dx < places.length && y + dy < places[0].length) {
            if (places[x + dx][y + dy] == Place.EMPTY) {
                // see more far
                return isOccupiedLongDistance(x + dx, y + dy, dx, dy, places);
            } else {
                return places[x + dx][y + dy] == Place.OCCUPIED_SEAT;
            }
        } else {
            return false;
        }
    }

    private int occupiedLongDistance(int x, int y, Place[][] places) {
        int out = 0;
        out += isOccupiedLongDistance(x, y, -1, -1, places) ? 1 : 0;
        out += isOccupiedLongDistance(x, y, 0, -1, places) ? 1 : 0;
        out += isOccupiedLongDistance(x, y, +1, -1, places) ? 1 : 0;
        out += isOccupiedLongDistance(x, y, +1, +1, places) ? 1 : 0;
        out += isOccupiedLongDistance(x, y, 0, +1, places) ? 1 : 0;
        out += isOccupiedLongDistance(x, y, -1, +1, places) ? 1 : 0;
        out += isOccupiedLongDistance(x, y, -1, 0, places) ? 1 : 0;
        out += isOccupiedLongDistance(x, y, +1, 0, places) ? 1 : 0;
        return out;
    }

    private Place[][] stepLongDistance(Place[][] places) {
        Place[][] out = new Place[places.length][];
        for (int i = 0; i < places.length; i++) {
            out[i] = new Place[places[i].length];
            for (int j = 0; j < places[i].length; j++) {
                switch (places[i][j]) {
                    case FREE_SEAT:
                        if (occupiedLongDistance(i, j, places) == 0) {
                            out[i][j] = Place.OCCUPIED_SEAT;
                        } else {
                            out[i][j] = Place.FREE_SEAT;
                        }
                        break;
                    case OCCUPIED_SEAT:
                        if (occupiedLongDistance(i, j, places) >= 5) {
                            out[i][j] = Place.FREE_SEAT;
                        } else {
                            out[i][j] = Place.OCCUPIED_SEAT;
                        }
                        break;
                    case EMPTY:
                        out[i][j] = Place.EMPTY;
                        break;
                    default:
                        assert false;
                }
            }
        }
        return out;
    }

    public int countFreePlacesLongDistance(String file) throws IOException {
        places = new PlaceCollector(file).process().toArray(new Place[1][1]);
        if (isDebug()) {
            System.out.println(printPlaces(places));
            System.out.println();
        }

        Place[][] newPlaces = places;
        do {
            places = newPlaces;
            newPlaces = stepLongDistance(places);
            if (isDebug()) {
                System.out.println();
                System.out.println(printPlaces(newPlaces));
            }
        } while (!isEquals(places, newPlaces));

        return count(newPlaces, Place.OCCUPIED_SEAT);
    }

    enum Place {
        EMPTY("."),
        FREE_SEAT("L"),
        OCCUPIED_SEAT("#");

        private static final Map<String, Place> values;

        static {
            values = new HashMap<>();
            Arrays.stream(values()).forEach(p -> values.put(p.place, p));
        }

        private final String place;

        Place(String place) {
            this.place = place;
        }

        public static Place get(String value) {
            return values.get(value);
        }

        public String toString() {
            return place;
        }
    }

    //*
    public int solvePart1WithMatrix(String file) {
        Place[] type = new Place[1];
        Matrix<Place> places = new PuzzleSolver2D<Place>(Place.class, file)
                .load(line -> {
                    // TODO mapper from String (line) to Enum[] to speed it up
                    return line.chars()
                            .mapToObj(c -> (char) c)
                            .map(c -> Place.get(c.toString()))
                            .collect(toList());
                });
        Matrix<Place> newPlaces = places;
        do {
            places = newPlaces;
            Matrix<Integer> surround = places.convolution(
                    Matrix.instance(new Integer[][]{
                            {1, 1, 1},
                            {1, 0, 1},
                            {1, 1, 1}}),
                    (p, c) -> c.equals(1) && p == Place.OCCUPIED_SEAT ? 1 : 0,
                    0, (a, b) -> a + b);
            newPlaces = places.applyMatrix(
                    surround,
                    (p, s) -> {
                        if (s >= 4) {
                            return switchPlace(p);
                        } else {
                            return Place.FREE_SEAT;
                        }});
        } while (!places.equals(newPlaces));
        return places.apply(v -> v.equals(Place.OCCUPIED_SEAT) ? 1 : 0);
    }

    class PlaceCollector extends DataCollector<Place[]> {
        public PlaceCollector(String file) {
            super(file);
        }

        @Override
        protected Place[] processLine(String line) {
            Place[] newLine = new Place[line.length()];
            for (int i = 0; i < line.length(); i++) {
                //TODO automatically convert value to ENUM and move it to library
                switch (line.charAt(i)) {
                    case '.':
                        newLine[i] = Place.EMPTY;
                        break;
                    case 'L':
                        newLine[i] = Place.FREE_SEAT;
                        break;
                    case '#':
                        newLine[i] = Place.OCCUPIED_SEAT;
                        break;
                }
            }
            return newLine;
        }
    }
}
