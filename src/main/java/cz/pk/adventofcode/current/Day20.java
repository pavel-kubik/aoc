package cz.pk.adventofcode.current;

import cz.pk.adventofcode.util.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day20 {

    private final boolean debug;

    class MatrixCollector extends GroupCollector<Matrix<Integer>> {

        public MatrixCollector(String file) {
            super(file);
        }

        @Override
        protected Matrix<Integer> processGroup(List<String> groupLines) {
            List<List<Integer>> data = new ArrayList<>();
            for (String line : groupLines) {
                data.add(line.chars().mapToObj(c -> (char)c).map(c -> c == '#' ? 1 : 0).collect(toList()));
            }
            return Matrix.instance(data);
        }
    }

    public long solve(String file) {
        List<Matrix<Integer>> matrixes = new MatrixCollector(file).processGroups();
        Matrix<Integer> alg = matrixes.get(0);
        Matrix<Integer> init = matrixes.get(1);

        System.out.println("Alg:\n" + alg);
        System.out.println("Init:\n" + init);

        Matrix<Integer> out = Matrix.instance(2*init.getWidth()+10, 2*init.getHeight()+10, 0);

        Vector2<Integer> offset = new Vector2<>(init.getWidth()/2+5, init.getHeight()/2+5);
        init.applyOperation((m, coord) -> {
            out.set(coord.plus(offset), m.get(coord));
            return null;
        });

        System.out.println("Out:\n" + out);

        Matrix<Integer> current = out;

        for (int i = 0; i < 2; i++) {
            current = current.convolution(Matrix.instance(new Integer[][]{
                            {100000000, 10000000, 1000000},
                            {   100000,    10000,    1000},
                            {      100,        10,      1}}),
                    (m, cm) -> m * cm,
                    0,
                    (a, b) -> a + b);
            System.out.println("Current:\n" + current);
            current = current.applyOperation((m, coord) -> Integer.parseInt(m.get(coord).toString(), 2));
            System.out.println("Current:\n" + current);
            current = current.applyOperation((m, coord) -> alg.get(0, m.get(coord)));
            System.out.println("Current:\n" + current);
        }
        current = current.submatrix(2, 2, current.getHeight(), current.getWidth());

        return current.map(0, (sum, v) -> sum + v);
    }

    public long solve2(String file) {
        List<Matrix<Integer>> matrixes = new MatrixCollector(file).processGroups();
        Matrix<Integer> alg = matrixes.get(0);
        Matrix<Integer> init = matrixes.get(1);

        System.out.println("Alg:\n" + alg);
        System.out.println("Init:\n" + init);

        Matrix<Integer> out = Matrix.instance(2*init.getWidth()+200, 2*init.getHeight()+200, 0);

        Vector2<Integer> offset = new Vector2<>(init.getWidth()/2+150, init.getHeight()/2+150);
        init.applyOperation((m, coord) -> {
            out.set(coord.plus(offset), m.get(coord));
            return null;
        });

        System.out.println("Out:\n" + out);

        Matrix<Integer> current = out;

        for (int i = 0; i < 50; i++) {
            current = current.convolution(Matrix.instance(new Integer[][]{
                            {100000000, 10000000, 1000000},
                            {   100000,    10000,    1000},
                            {      100,        10,      1}}),
                    (m, cm) -> m * cm,
                    0,
                    (a, b) -> a + b);
            System.out.println("Current:\n" + current);
            current = current.applyOperation((m, coord) -> Integer.parseInt(m.get(coord).toString(), 2));
            System.out.println("Current:\n" + current);
            current = current.applyOperation((m, coord) -> alg.get(0, m.get(coord)));
            System.out.println("Current:\n" + current);
            // cut evil corners
            current = current.submatrix(2, 2, current.getHeight(), current.getWidth());
        }

        return current.map(0, (sum, v) -> sum + v);
    }

    public static void main(String[] args) {
        System.out.println(Day20.class);
        long count;
        //*
        count = new Day20(true).solve("day20_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 35;

        count = new Day20(true).solve("day20.txt");
        System.out.println("Result: " + count);
        assert count == 5347;

        //*/

        count = new Day20(true).solve2("day20_test.txt");
        System.out.println("Result: " + count);
        assert count == 3351;

        count = new Day20(true).solve2("day20.txt");
        System.out.println("Result: " + count);
        assert count == 44; // 22652
        //*/

        assert "VM options -ea set. You can delete this.".isEmpty();
        throw new RuntimeException("Don't forgot to set vm option -ea");
    }
}
