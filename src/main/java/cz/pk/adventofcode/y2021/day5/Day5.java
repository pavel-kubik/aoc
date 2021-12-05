package cz.pk.adventofcode.current;

import java.util.List;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.Matrix;
import lombok.AllArgsConstructor;
import lombok.Data;

import static cz.pk.adventofcode.util.DataCollectorFactory.collectData;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day5 {

    private final boolean debug;

    @Data
    @AllArgsConstructor
    class Line {
        int x1;
        int y1;
        int x2;
        int y2;

        public boolean isLine() {
            return x1 == x2 || y1 == y2;
        }

        public boolean isHorizontal() {
            return y1 == y2;
        }

        public boolean isVertical() {
            return x1 == x2;
        }

        public boolean isDiagonal() {
            return Math.abs(x1 - x2) == Math.abs(y1 - y2);
        }

    }

    class TypeCollector extends DataCollector<Line> {

        public TypeCollector(String file) {
            super(file);
        }

        @Override
        protected Line processLine(String line) {
            String[] points = line.split("->");
            String[] p1 = points[0].trim().split(",");
            String[] p2 = points[1].trim().split(",");
            return new Line(
                    Integer.valueOf(String.valueOf(p1[0])),
                    Integer.valueOf(String.valueOf(p1[1])),
                    Integer.valueOf(String.valueOf(p2[0])),
                    Integer.valueOf(String.valueOf(p2[1])));
        }
    }

    public long solve(String file) {
        Line[] data = new TypeCollector(file).process().toArray(new Line[1]);

        List<Line> lines = stream(data).filter(Line::isLine).collect(toList());

        int[][] field = new int[1000][1000];
        lines.stream().forEach(line -> {
            System.out.println(line);
            if (line.isHorizontal()) {
                if (line.getX2() > line.getX1()) {
                    for (int i = line.getX1(); i <= line.getX2(); i++) {
                        field[i][line.getY1()]++;
                    }
                } else {
                    for (int i = line.getX2(); i <= line.getX1(); i++) {
                        field[i][line.getY1()]++;
                    }
                }
            } else {
                if (line.getY2() > line.getY1()) {
                    for (int i = line.getY1(); i <= line.getY2(); i++) {
                        field[line.getX1()][i]++;
                    }
                } else {
                    for (int i = line.getY2(); i <= line.getY1(); i++) {
                        field[line.getX1()][i]++;
                    }
                }
            }
        });

        // count
        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                sum += field[i][j] >= 2 ? 1 : 0;
            }
        }

        System.out.println(data);
        return sum;
    }

    public long solve2(String file) {
        int size = 1000;
        Line[] data = new TypeCollector(file).process().toArray(new Line[1]);

        List<Line> lines = stream(data).collect(toList());

        int[][] field = new int[size][size];
        lines.stream().forEach(line -> {
            System.out.println(line);
            if (line.isHorizontal()) {
                if (line.getX2() > line.getX1()) {
                    for (int i = line.getX1(); i <= line.getX2(); i++) {
                        field[i][line.getY1()]++;
                    }
                } else {
                    for (int i = line.getX2(); i <= line.getX1(); i++) {
                        field[i][line.getY1()]++;
                    }
                }
            } else if (line.isVertical()) {
                if (line.getY2() > line.getY1()) {
                    for (int i = line.getY1(); i <= line.getY2(); i++) {
                        field[line.getX1()][i]++;
                    }
                } else {
                    for (int i = line.getY2(); i <= line.getY1(); i++) {
                        field[line.getX1()][i]++;
                    }
                }
            } else if (line.isDiagonal()) {
                int dx = line.getX2() - line.getX1() > 0 ? 1 : -1;
                int dy = line.getY2() - line.getY1() > 0 ? 1 : -1;
                int iter = 0;
                if (dx < 0) {
                    for (int i = line.getX1(); i >= line.getX2(); i += dx) {
                        field[i][line.getY1() + dy * iter]++;
                        iter++;
                    }
                } else {
                    for (int i = line.getX1(); i <= line.getX2(); i += dx) {
                        field[i][line.getY1() + dy * iter]++;
                        iter++;
                    }
                }
            } else {
                System.out.println("Invalid line " + line);
            }

        });

        // count
        int sum = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sum += field[i][j] >= 2 ? 1 : 0;
            }
        }

        System.out.println(data);
        return sum;

    }

    public static void main(String[] args) {
        System.out.println(Day5.class);
        long count;
        //*
        count = new Day5(true).solve("day5_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 5;

        count = new Day5(true).solve("day5.txt");
        System.out.println("Result: " + count);
        assert count == 6687;

        //*/

        count = new Day5(true).solve2("day5_test.txt");
        System.out.println("Result: " + count);
        assert count == 12;

        count = new Day5(true).solve2("day5.txt");
        System.out.println("Result: " + count);
        assert count == 19851; //18326;
        //*/
    }
}
