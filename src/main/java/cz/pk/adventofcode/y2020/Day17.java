package cz.pk.adventofcode.y2020;

import java.util.List;
import java.util.stream.Collectors;

import cz.pk.adventofcode.util.DataCollector;
import lombok.Data;

@Data
public class Day17 {

    private final boolean debug;

    public static final int SIZE = 20;

    int[][][][] cube = new int[SIZE][SIZE][SIZE][SIZE];

    class TypeCollector extends DataCollector<List<Integer>> {

        public TypeCollector(String file) {
            super(file);
        }

        @Override
        protected List<Integer> processLine(String line) {
            return line.chars()
                    .mapToObj(i -> (char) i)
                    .map(c -> c == '#' ? 1 : 0)
                    .collect(Collectors.toList());
        }
    }

    private int[][][][] copyOf(int[][][][] source) {
        int[][][][] out = new int[SIZE][SIZE][SIZE][SIZE];
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                for (int z = 0; z < SIZE; z++) {
                    for (int w = 0; w < SIZE; w++) {
                        out[x][y][z][w] = source[x][y][z][w];
                    }
                }
            }
        }
        return out;
    }

    private int sumNeighbours(int x, int y, int z, int w, int[][][][] data) {
        int sum = 0;
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                for (int dz = -1; dz < 2; dz++) {
                    for (int dw = -1; dw < 2; dw++) {
                        if (dx == 0 && dy == 0 && dz == 0 && dw == 0) {
                            continue;
                        }
                        sum += get(x + dx, y + dy, z + dz, w + dw, data);
                    }
                }
            }
        }
        return sum;
    }

    private int get(int x, int y, int z, int w, int[][][][] data) {
        if (x >= 0 && x < data.length) {
            if (y >= 0 && y < data[x].length) {
                if (z >= 0 && z < data[x][y].length) {
                    if (w >= 0 && w < data[x][y][z].length) {
                        return data[x][y][z][w];
                    }
                }
            }
        }
        return 0;
    }

    private void printCube(int[][][][] data) {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < SIZE; x++) {
            sb.append("X=").append(x).append("\n");
            for (int y = 0; y < SIZE; y++) {
                for (int z = 0; z < SIZE; z++) {
                    for (int w = 0; w < SIZE; w++) {
                        sb.append(data[x][y][z][w] == 1 ? '#' : '.');
                    }
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    public long solve(String file) {
        List<List<Integer>> initialPlane = new TypeCollector(file).process();
        System.out.println(initialPlane);

        // put initial 2D plane into 3D cube
        int offsetX = SIZE / 2 - initialPlane.size() / 2;
        int offsetY = SIZE / 2 - initialPlane.get(0).size() / 2;
        for (int x = 0; x < initialPlane.size(); x++) {
            for (int y = 0; y < initialPlane.get(x).size(); y++) {
                cube[SIZE / 2][SIZE / 2][offsetX + x][offsetY + y] = initialPlane.get(x).get(y);
            }
        }

        // iterate 6 generations
        int[][][][] currentGen = cube;
        //printCube(currentGen);
        for (int i = 0; i < 6; i++) {
            int[][][][] nextGen = copyOf(currentGen);
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    for (int z = 0; z < SIZE; z++) {
                        for (int w = 0; w < SIZE; w++) {
                            int sum = sumNeighbours(x, y, z, w, currentGen);
                            if (currentGen[x][y][z][w] == 1) {
                                nextGen[x][y][z][w] = sum == 2 || sum == 3 ? 1 : 0;
                            } else {
                                nextGen[x][y][z][w] = sum == 3 ? 1 : 0;
                            }
                        }
                    }
                }
            }
            currentGen = nextGen;
            //printCube(currentGen);
        }

        // sum active fields
        long sum = 0;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                for (int z = 0; z < SIZE; z++) {
                    for (int w = 0; w < SIZE; w++) {
                        sum += currentGen[x][y][z][w];
                    }
                }
            }
        }

        return sum;
    }

    public long solve2(String file) {
        List<List<Integer>> initialPlane = new TypeCollector(file).process();
        System.out.println(initialPlane);
        return 0;
    }

    public static void main(String[] args) {
        long count;
        //*
        count = new Day17(true).solve("2020/day17_test.txt");
        System.out.println("Result: " + count);
        assert count == 848;

        count = new Day17(true).solve("2020/day17.txt");
        System.out.println("Result: " + count);
        //assert count == 209;
        // 206 low

        /*/

        count = new Day13(true).solve2("2020/day12_test.txt");
        System.out.println("Result: " + count);
        assert count == 25;

        count = new Day13(true).solve2("2020/day12.txt");
        System.out.println("Result: " + count);
        //assert count == 27016;

         */
        //*/
    }
}
