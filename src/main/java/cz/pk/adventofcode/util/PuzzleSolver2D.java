package cz.pk.adventofcode.util;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class PuzzleSolver2D<TYPE> {
    private final List<String> lines;
    private Class<TYPE> clazz;

    public PuzzleSolver2D(Class<TYPE> clazz, String file) {
        this.clazz = clazz;
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
     * <p>
     * Read line by line.
     */
    public Matrix<TYPE> load(Function<String, List<TYPE>> mapper) {
        List<List<TYPE>> rows = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            rows.add(mapper.apply(lines.get(i)));
        }
        return Matrix.instance(clazz).setRows(rows);
    }

    /**
     * Most of the puzzle has text inputs. This method is responsible
     * for read file (text multi-line) and transform it to TYPE.
     * <p>
     * Read by line groups.
     * <p>
     * public PuzzleSolver2D<TYPE> loadMultiLine(Function<List<String>, TYPE[]> mapper) {
     * data = new ArrayList<>();
     * List<String> groupLines = new ArrayList<>();
     * for (int i=0;i<lines.size();i++) {
     * String line = lines.get(i);
     * if (line.length() == 0) {
     * data.add(mapper.apply(groupLines));
     * groupLines = new ArrayList<>();
     * } else {
     * groupLines.add(line);
     * }
     * }
     * data.add(mapper.apply(groupLines));
     * return this;
     * }
     */
/*
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

 */
}