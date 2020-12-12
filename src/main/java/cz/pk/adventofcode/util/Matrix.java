package cz.pk.adventofcode.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import lombok.Data;

@Data
public class Matrix<TYPE> {
    private final List<List<TYPE>> data;
    private int width;
    private int height;

    private Matrix() {
        data = new ArrayList<>();
    }

    public static <T> Matrix<T> instance(Class<T> clazz) {
        return new Matrix<T>();
    }

    public TYPE get(int row, int col) {
        return data.get(row).get(col);
    }

    public void set(int row, int col, TYPE value) {
        data.get(row).set(col, value);
    }

    public <T> boolean equals(Matrix<TYPE> matrix) {
        for (int i = 0; i < data.size(); i++) {
            List<TYPE> row = data.get(i);
            for (int j = 0; j < row.size(); j++) {
                if (!row.get(j).equals(matrix.get(i, j))) {
                    return false;
                }
            }
        }
        return true;
    }

    public <TYPE_CM, TYPE_O>
    Matrix<TYPE_O> convolution(Matrix<TYPE_CM> convolutionMatrix, BiFunction<TYPE, TYPE_CM, TYPE_O> op, TYPE_O initialValue, BinaryOperator<TYPE_O> collector) {
        Matrix<TYPE_O> out = new Matrix<>();
        for (int i = 0; i < data.size(); i++) {
            List<TYPE> row = data.get(i);
            for (int j = 0; j < row.size(); j++) {
                out.set(i, j, convolutionFrame(i, j, convolutionMatrix, op, initialValue, collector));
            }
        }
        return out;
    }

    private <TYPE_CM, TYPE_O>
    TYPE_O convolutionFrame(int x, int y, Matrix<TYPE_CM> convolutionMatrix, BiFunction<TYPE, TYPE_CM, TYPE_O> op, TYPE_O initialValue, BinaryOperator<TYPE_O> collector) {
        int cmRows = convolutionMatrix.data.size();
        TYPE_O out = initialValue;
        for (int i = 0; i < cmRows; i++) {
            int cmCols = convolutionMatrix.data.get(i).size();
            for (int j = 0; j < cmCols; j++) {
                int dx = x - cmRows / 2 + i;
                int dy = y - cmCols / 2 + i;
                if (x + dx >= 0 &&
                        x + dx < data.size() &&
                        y + dy >= 0 &&
                        y + dy < data.get(x + dx).size()) {
                    out = collector.apply(out, op.apply(data.get(x + dx).get(y + dy), convolutionMatrix.get(i, j)));
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
    public <TYPE_A, TYPE_B, TYPE_O> TYPE_O[][] applyAB2Matrix(TYPE_A[][] a,
                                                              TYPE_B[][] b,
                                                              BiFunction<TYPE_A, TYPE_B, TYPE_O> op) {
        TYPE_O[][] out = (TYPE_O[][]) Array.newInstance(a.getClass().getComponentType(), a.length);
        for (int i = 0; i < a.length; i++) {
            out[i] = (TYPE_O[]) Array.newInstance(a[i].getClass().getComponentType(), a[i].length);
            for (int j = 0; j < a[i].length; j++) {
                out[i][j] = op.apply(a[i][j], b[i][j]);
            }
        }
        return out;
    }

    public Integer apply(Object[][] a, Function<Object, Integer> op) {
        Integer out = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                out += op.apply(a[i][j]);
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
}