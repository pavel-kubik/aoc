package cz.pk.adventofcode.util;

import java.lang.reflect.Array;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class MatrixUtil<T> {

    private final Class<? extends T> clazz;

    public MatrixUtil(Class<? extends T> clazz) {
        this.clazz = clazz;
    }

    public <T> boolean same(Pair<T[][]> pair) {
        T[][] a = pair.first;
        T[][] b = pair.second;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (!a[i][j].equals(b[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    public <TYPE_A, TYPE_CM, TYPE_O>
    TYPE_O[][] convolution(Class<? extends TYPE_O> outClazz, TYPE_A[][] a, TYPE_CM[][] convolutionMatrix, BiFunction<TYPE_A, TYPE_CM, TYPE_O> op, TYPE_O initialValue, BinaryOperator<TYPE_O> collector) {
        TYPE_O[][] out = (TYPE_O[][]) Array.newInstance(outClazz, a.length);
        for (int i = 0; i < a.length; i++) {
            out[i] = (TYPE_O[]) Array.newInstance(outClazz, a[i].length);
            for (int j = 0; j < a[i].length; j++) {
                out[i][j] = applyWindow(a, i, j, convolutionMatrix, op, initialValue, collector);
            }
        }
        return out;
    }

    private <TYPE_A, TYPE_CM, TYPE_O>
    TYPE_O applyWindow(TYPE_A[][] a, int x, int y, TYPE_CM[][] convolutionMatrix, BiFunction<TYPE_A, TYPE_CM, TYPE_O> op, TYPE_O initialValue, BinaryOperator<TYPE_O> collector) {
        int cmRows = convolutionMatrix.length;
        TYPE_O out = initialValue;
        for (int i = 0; i < cmRows; i++) {
            int cmCols = convolutionMatrix[i].length;
            for (int j = 0; j < cmCols; j++) {
                int dx = x - cmRows / 2 + i;
                int dy = y - cmCols / 2 + i;
                if (x + dx >= 0 &&
                        x + dx < a.length &&
                        y + dy >= 0 &&
                        y + dy < a[x + dx].length) {
                    // TODO make it for any type and add Function for convolution collector
                    out = collector.apply(out, op.apply(a[x + dx][y + dy], convolutionMatrix[i][j]));
                }
            }
        }
        return out;
    }

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

}
