package cz.pk.adventofcode.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class Matrix<TYPE> {
    private List<List<TYPE>> rows;
    private int width;
    private int height;

    public Matrix<TYPE> setRows(List<List<TYPE>> rows) {
        this.rows = rows;
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
        return new Matrix<T>();
    }

    public static <T> Matrix<T> instance(List<List<T>> rows) {
        return new Matrix<T>().setRows(rows);
    }

    public TYPE get(int row, int col) {
        return rows.get(row).get(col);
    }

    public void set(int row, int col, TYPE value) {
        rows.get(row).set(col, value);
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
        int cmRows = convolutionMatrix.rows.size();
        TYPE_O out = initialValue;
        for (int i = 0; i < cmRows; i++) {
            int cmCols = convolutionMatrix.rows.get(i).size();
            for (int j = 0; j < cmCols; j++) {
                int dx = x - cmRows / 2 + i;
                int dy = y - cmCols / 2 + i;
                if (x + dx >= 0 &&
                        x + dx < rows.size() &&
                        y + dy >= 0 &&
                        y + dy < rows.get(x + dx).size()) {
                    out = collector.apply(out, op.apply(rows.get(x + dx).get(y + dy), convolutionMatrix.get(i, j)));
                }
            }
        }
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

}