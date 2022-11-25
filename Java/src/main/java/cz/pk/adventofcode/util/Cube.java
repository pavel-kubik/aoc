package cz.pk.adventofcode.util;

import cz.pk.adventofcode.util.HexagonUtil.HexagonCoordinate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public class Cube<TYPE> {
    private List<List<List<TYPE>>> data;
    private int dimensionX;
    private int dimensionY;
    private int dimensionZ;
    private TYPE initialValue = null;

    public static <TYPE> Cube<TYPE> instance(TYPE[][][] array3d) {
        List<List<List<TYPE>>> d1 = new ArrayList<>();
        for (int i = 0; i < array3d.length; i++) {
            List<List<TYPE>> d2 = new ArrayList<>();
            d1.add(d2);
            for (int j = 0; j < array3d[0].length; j++) {
                d2.add(Arrays.asList(array3d[i][j]));
            }
        }
        return Cube.instance(d1);
    }

    public static <TYPE> Cube<TYPE> instance(List<List<List<TYPE>>> list3d) {
        return new Cube(list3d);
    }

    public static <TYPE> Cube<TYPE> instance(int dimension, TYPE initialValue) {
        return Cube.instance(dimension, dimension, dimension, initialValue);
    }

    public static <TYPE> Cube<TYPE> instance(int dimensionX, int dimensionY, int dimensionZ, TYPE initialValue) {
        List<List<List<TYPE>>> d1 = new ArrayList<>();
        for (int i = 0; i < dimensionX; i++) {
            List<List<TYPE>> d2 = new ArrayList<>();
            d1.add(d2);
            for (int j = 0; j < dimensionY; j++) {
                List<TYPE> d3 = new ArrayList<>();
                d2.add(d3);
                for (int k = 0; k < dimensionZ; k++) {
                    d3.add(initialValue);
                }
            }
        }
        return Cube.instance(d1).setInitialValue(initialValue);
    }

    private Cube(List<List<List<TYPE>>> data) {
        this.dimensionX = data.size();
        this.dimensionY = data.get(0).size();
        this.dimensionZ = data.get(0).get(0).size();
        this.data = data;
    }

    public TYPE get(int x, int y, int z) {
        if (x >= 0 && x < dimensionX &&
            y >= 0 && y < dimensionY &&
            z >= 0 && z < dimensionZ) {
            return data.get(x).get(y).get(z);
        } else {
            return initialValue;
        }
    }

    public void set(int x, int y, int z, TYPE value) {
        data.get(x).get(y).set(z, value);
    }

    // TODO convert HexagonCoordinate to general Vec3
    public TYPE get(HexagonCoordinate coordinate) {
        return get(coordinate.getQ(), coordinate.getR(), coordinate.getS());
    }

    public void set(HexagonCoordinate coordinate, TYPE value) {
        set(coordinate.getQ(), coordinate.getR(), coordinate.getS(), value);
    }

    public TYPE map(TYPE initialValue, BiFunction<TYPE, TYPE, TYPE> mapFunction) {
        TYPE out = initialValue;
        for (int i = 0; i < dimensionX; i++) {
            for (int j = 0; j < dimensionY; j++) {
                for (int k = 0; k < dimensionZ; k++) {
                    out = mapFunction.apply(out, get(i, j, k));
                }
            }
        }
        return out;
    }

    public <TYPE_CONV, TYPE_OUT>
    Cube<TYPE_OUT> convolution(Cube<TYPE_CONV> convolutionCube,
                               BiFunction<TYPE, TYPE_CONV, TYPE_OUT> op,
                               TYPE_OUT initialValue,
                               BinaryOperator<TYPE_OUT> collector) {
        List<List<List<TYPE_OUT>>> out = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            List<List<TYPE_OUT>> d2 = new ArrayList<>();
            for (int j = 0; j < data.get(i).size(); j++) {
                List<TYPE_OUT> d3 = new ArrayList<>();
                d2.add(d3);
                for (int k = 0; k < data.get(i).get(j).size(); k++) {
                    d3.add(convolutionFrame(i, j, k, convolutionCube, op, initialValue, collector));
                }
            }
            out.add(d2);
        }
        return Cube.instance(out);
    }

    private <TYPE_CONV, TYPE_OUT>
    TYPE_OUT convolutionFrame(int x, int y, int z, Cube<TYPE_CONV> convolutionCube, BiFunction<TYPE, TYPE_CONV, TYPE_OUT> op, TYPE_OUT initialValue, BinaryOperator<TYPE_OUT> collector) {
        int cmDimX = convolutionCube.dimensionX;
        int cmDimY = convolutionCube.dimensionY;
        int cmDimZ = convolutionCube.dimensionZ;
        TYPE_OUT out = initialValue;
        for (int i = 0; i < cmDimX; i++) {
            for (int j = 0; j < cmDimY; j++) {
                for (int k = 0; k < cmDimZ; k++) {
                    int dx = i - cmDimX / 2;
                    int dy = j - cmDimY / 2;
                    int dz = k - cmDimZ / 2;
                    if (x + dx >= 0 && x + dx < data.size() &&
                        y + dy >= 0 && y + dy < data.get(x + dx).size() &&
                        z + dz >= 0 && z + dz < data.get(x + dx).get(y + dy).size()
                    ) {
                        TYPE_OUT item = op.apply(data.get(x + dx).get(y + dy).get(z + dz), convolutionCube.get(i, j, k));
                        out = collector.apply(out, item);
                    }
                }
            }
        }
        return out;
    }

    public <TYPE_B, TYPE_O> Cube<TYPE_O>
    applyCube(Cube<TYPE_B> b,
                BiFunction<TYPE, TYPE_B, TYPE_O> op) {
        assert dimensionX == b.dimensionX;
        assert dimensionY == b.dimensionY;
        assert dimensionZ == b.dimensionZ;
        List<List<List<TYPE_O>>> out = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            List<List<TYPE_O>> d2 = new ArrayList<>();
            for (int j = 0; j < data.get(i).size(); j++) {
                List<TYPE_O> d3 = new ArrayList<>();
                d2.add(d3);
                for (int k = 0; k < data.get(i).get(j).size(); k++) {
                    d3.add(op.apply(this.get(i, j, k), b.get(i, j, k)));
                }
            }
            out.add(d2);
        }
        return Cube.instance(out);
    }

    public Cube<TYPE>
    applyOperation(BiFunction<Cube<TYPE>, HexagonCoordinate, TYPE> op) {
        List<List<List<TYPE>>> out = new ArrayList<>();
        for (int i = 0; i < dimensionX; i++) {
            List<List<TYPE>> d2 = new ArrayList<>();
            for (int j = 0; j < dimensionY; j++) {
                List<TYPE> d3 = new ArrayList<>();
                for (int k = 0; k < dimensionZ; k++) {
                    d3.add(op.apply(this, new HexagonCoordinate(i, j, k)));
                }
                d2.add(d3);
            }
            out.add(d2);
        }
        return Cube.instance(out).setInitialValue(initialValue);
    }

    public Cube setInitialValue(TYPE initialValue) {
        this.initialValue = initialValue;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < dimensionX; x++) {
            for (int y = 0; y < dimensionY; y++) {
                for (int z = 0; z < dimensionZ; z++) {
                    sb.append(this.get(x, y, z)).append(" ");
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
