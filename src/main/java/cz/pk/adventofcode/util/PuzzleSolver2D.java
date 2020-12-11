package cz.pk.adventofcode.util;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class PuzzleSolver2D<TYPE> {
    private List<String> lines;
    private TYPE[][] data;
    private TYPE[][] newData;
    private final Class<? extends TYPE> clazz;
    private MatrixUtil<TYPE> matrixUtil;

    public PuzzleSolver2D(Class<? extends TYPE> clazz, String file) {
        this.clazz = clazz;
        matrixUtil = new MatrixUtil<>(clazz);
        try {
            URL resource = getClass().getClassLoader().getResource(file);
            lines = Files.readAllLines(Path.of(resource.getPath()));
        } catch (IOException e) {
            throw new RuntimeException("Can't read file " + file, e);
        }
    }

    /**
     * Most of the puzzle has text inputs. This method is responsible
     * for read file (text multi-line) and transform it to TYPE.
     *
     * Read line by line.
     */
    public PuzzleSolver2D<TYPE> load(Class<? extends TYPE[]> clazz, Function<String, TYPE[]> mapper) {
        data = (TYPE[][]) Array.newInstance(clazz, lines.size());
        for (int i=0;i<data.length;i++) {
            data[i] = mapper.apply(lines.get(i));
        }
        return this;
    }

    /**
     * Most of the puzzle has text inputs. This method is responsible
     * for read file (text multi-line) and transform it to TYPE.
     *
     * Read by line groups.
     *
    public PuzzleSolver2D<TYPE> loadMultiLine(Function<List<String>, TYPE[]> mapper) {
        data = new ArrayList<>();
        List<String> groupLines = new ArrayList<>();
        for (int i=0;i<lines.size();i++) {
            String line = lines.get(i);
            if (line.length() == 0) {
                data.add(mapper.apply(groupLines));
                groupLines = new ArrayList<>();
            } else {
                groupLines.add(line);
            }
        }
        data.add(mapper.apply(groupLines));
        return this;
    }
    */

    public PuzzleSolver2D<TYPE> iterate(Function<TYPE[][], TYPE[][]> op, Predicate<Pair<TYPE[][]>> condition) {
        newData = data;
        do {
            data = newData;
            newData = op.apply(matrixUtil.copyOf(data));
        } while (condition.test(new Pair(data, newData)));
        data = newData;
        return this;
    }

    public Integer aggregate(Function<Object, Integer> op) {
        return matrixUtil.apply(data, op);
    }
}