package cz.pk.adventofcode.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static java.lang.String.format;

public class Matrix<TYPE> {
    private List<List<TYPE>> rows;
    private int width;
    private int height;
    private TYPE initialValue;

    public Matrix<TYPE> setRows(List<List<TYPE>> rows) {
        this.rows = rows;
        this.height = rows.size();
        this.width = rows.get(0).size();
        return this;
    }

    private Matrix() {
        rows = new ArrayList<>();
    }

    public static <T> Matrix<T> instance(Class<T> clazz) {
        return new Matrix<T>();
    }

    public static <T> Matrix<T> instance(T[][] data) {
        List<List<T>> rows = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            rows.add(Arrays.asList(data[i]));
        }
        return Matrix.instance(rows);
    }

    public static <T> Matrix<T> instance(int width, int height, T initialValue) {
        List<List<T>> rows = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            List<T> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(initialValue);
            }
            rows.add(row);
        }
        return Matrix.instance(rows).setInitialValue(initialValue);
    }

    public static <T> Matrix<T> instance(List<List<T>> rows) {
        return new Matrix<T>().setRows(rows);
    }

    public TYPE get(int row, int col) {
        if (row >= 0 && row < rows.size() &&
            col >= 0 && col < rows.get(row).size()) {
            return rows.get(row).get(col);
        } else {
            return null;
        }
    }

    public TYPE get(Vector2<Integer> vector) {
        return get(vector.y, vector.x);
    }

    public void set(int row, int col, TYPE value) {
        rows.get(row).set(col, value);
    }

    public void set(Vector2<Integer> vector, TYPE value) {
        set(vector.y, vector.x, value); // TODO x should go to columns
    }

    public Matrix<TYPE> submatrix(int startRow, int startCol, int height, int width) {
        List<List<TYPE>> rows = new ArrayList<>();
        assert this.width >= width;
        assert this.height >= height;
        for (int i = startRow; i < height; i++) {
            List<TYPE> row = new ArrayList<>();
            for (int j = startCol; j < width; j++) {
                row.add(this.get(i, j));
            }
            rows.add(row);
        }
        return instance(rows).setInitialValue(this.initialValue);
    }

    public <T> boolean equals(Matrix<TYPE> matrix) {
        for (int i = 0; i < rows.size(); i++) {
            List<TYPE> row = rows.get(i);
            for (int j = 0; j < row.size(); j++) {
                if (!row.get(j).equals(matrix.get(i, j))) {
                    return false;
                }
            }
        }
        return true;
    }
    public Matrix<TYPE>
    applyOperation(BiFunction<Matrix<TYPE>, Vector2<Integer>, TYPE> op) {
        List<List<TYPE>> out = new ArrayList<>();
        for (int row = 0; row < rows.size(); row++) {
            List<TYPE> d2 = new ArrayList<>();
            for (int col = 0; col < rows.get(row).size(); col++) {
                d2.add(op.apply(this, new Vector2<>(col, row)));
            }
            out.add(d2);
        }
        return Matrix.instance(out);
    }

    public <TYPE_CM, TYPE_O>
    Matrix<TYPE_O> convolution(Matrix<TYPE_CM> convolutionMatrix,
                               BiFunction<TYPE, TYPE_CM, TYPE_O> op,
                               TYPE_O initialValue,
                               BinaryOperator<TYPE_O> collector) {
        List<List<TYPE_O>> out = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            List<TYPE_O> row = new ArrayList<>();
            for (int j = 0; j < rows.get(i).size(); j++) {
                row.add(convolutionFrame(i, j, convolutionMatrix, op, initialValue, collector));
            }
            out.add(row);
        }
        return Matrix.instance(out);
    }

    private <TYPE_CM, TYPE_O>
    TYPE_O convolutionFrame(int x, int y, Matrix<TYPE_CM> convolutionMatrix, BiFunction<TYPE, TYPE_CM, TYPE_O> op, TYPE_O initialValue, BinaryOperator<TYPE_O> collector) {
        //System.out.println(format("Item[%d,%d]", x, y));
        int cmRows = convolutionMatrix.rows.size();
        TYPE_O out = initialValue;
        for (int i = 0; i < cmRows; i++) {
            int cmCols = convolutionMatrix.rows.get(i).size();
            for (int j = 0; j < cmCols; j++) {
                int dx = i - cmRows / 2;
                int dy = j - cmCols / 2;
                //System.out.println(format("DX: %d, DY: %d", dx, dy));
                if (x + dx >= 0 &&
                        x + dx < rows.size() &&
                        y + dy >= 0 &&
                        y + dy < rows.get(x + dx).size()) {
                    TYPE_O item = op.apply(rows.get(x + dx).get(y + dy), convolutionMatrix.get(i, j));
                    //System.out.println(format("M[%d,%d]*c[%d,%d] = %d", x + dx, y + dy, i, j, item));
                    out = collector.apply(out, item);
                }
            }
        }
        //System.out.println(format(" ...sum=%s", out));
        return out;
    }

    // TODO
    public <TYPE_A, TYPE_B, TYPE_O> TYPE_O applyAB2Scalar(TYPE_A[][] a,
                                                          TYPE_B[][] b,
                                                          TYPE_O initialValue,
                                                          BiFunction<TYPE_A, TYPE_B, TYPE_O> op,
                                                          BinaryOperator<TYPE_O> collector) {
        TYPE_O out = initialValue;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                out = collector.apply(out, op.apply(a[i][j], b[i][j]));
            }
        }
        return out;
    }

    /**
     * [A] op [B] -> [a_i_j op b_i_j]
     */
    public <TYPE_B, TYPE_O> Matrix<TYPE_O>
    applyMatrix(Matrix<TYPE_B> b,
                BiFunction<TYPE, TYPE_B, TYPE_O> op) {
        assert width == b.width;
        assert height == b.height;
        List<List<TYPE_O>> outRows = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            List<TYPE_O> outRow = new ArrayList<>();
            for (int j = 0; j < rows.get(i).size(); j++) {
                outRow.add(op.apply(this.get(i, j), b.get(i, j)));
            }
            outRows.add(outRow);
        }
        return Matrix.instance(outRows);
    }

    /**
     * [A] op [B] -> [a_i_j op b_i_j]
     */
    public Matrix<TYPE>
    merge(Matrix<TYPE> b,
                BiFunction<Integer, Integer, Boolean> op) {
        List<List<TYPE>> outRows = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            List<TYPE> outRow = new ArrayList<>();
            for (int j = 0; j < rows.get(i).size(); j++) {
                if (op.apply(i, j)) {
                    outRow.add(this.get(i, j));
                } else {
                    outRow.add(b.get(i, j));
                }
            }
            outRows.add(outRow);
        }
        return Matrix.instance(outRows);
    }

    public Integer apply(Function<Object, Integer> op) {
        Integer out = 0;
        for (int i = 0; i < rows.size(); i++) {
            for (int j = 0; j < rows.get(i).size(); j++) {
                out += op.apply(this.get(i, j));
            }
        }
        return out;
    }

    public <T> T[][] copyOf(T[][] input) {
        T[][] out = (T[][]) Array.newInstance(input.getClass().getComponentType(), input.length);
        for (int i = 0; i < input.length; i++) {
            out[i] = (T[]) Array.newInstance(input[i].getClass().getComponentType(), input[i].length);
            for (int j = 0; j < input[i].length; j++) {
                out[i][j] = input[i][j];
            }
        }
        return out;
    }

    public <TYPE_CM, TYPE_O>
    Matrix<TYPE_O> convolutionWalker() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public <TYPE_O> TYPE_O map(TYPE_O initialValue, BiFunction<TYPE_O, TYPE, TYPE_O> mapFunction) {
        TYPE_O out = initialValue;
        for (int i = 0; i < rows.size(); i++) {
            for (int j = 0; j < rows.get(i).size(); j++) {
                out = mapFunction.apply(out, get(i, j));
            }
        }
        return out;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.rows.size(); i++) {
            for (int j = 0; j < this.rows.get(i).size(); j++) {
                sb.append(this.get(i, j)).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Print without field separator. Save space when field is just one digit.
     */
    public String toShortString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.rows.size(); i++) {
            for (int j = 0; j < this.rows.get(i).size(); j++) {
                sb.append(this.get(i, j));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TYPE getInitialValue() {
        return initialValue;
    }

    public Matrix<TYPE> setInitialValue(TYPE initialValue) {
        this.initialValue = initialValue;
        return this;
    }

    public void reverseColumns() {
        Collections.reverse(rows);
    }

    public void reverseRows() {
        for (List<TYPE> row : rows) {
            Collections.reverse(row);
        }
    }

    public Vector2<Integer> getDimension() {
        return new Vector2<>(getHeight(), getWidth());
    }
}