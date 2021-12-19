package cz.pk.adventofcode.current;

import cz.pk.adventofcode.util.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * https://www.baeldung.com/java-matrix-multiplication
 *
 * https://demonstrations.wolfram.com/VectorRotationsIn3D/
 */
@Data
public class Day19 {

    private final boolean debug;

    class ScanCollector extends GroupCollector<List<int[]>> {

        public ScanCollector(String file) {
            super(file);
        }

        @Override
        protected List<int[]> processGroup(List<String> groupLines) {
            List<int[]> scans = new ArrayList<>();
            for (String line : groupLines) {
                if (line.startsWith("---")){
                    System.out.println(line);
                    continue;
                }
                List<Integer> scan = stream(line.split(","))
                        .map(n -> Integer.parseInt(n, 10))
                        .collect(toList());
                scans.add(new int[] {scan.get(0), scan.get(1), scan.get(2)});
            }
            //System.out.println(scans);
            return scans;
        }
    }

    private int[] diff(int[] a, int[] b) {
        return new int[] {a[0] - b[0], a[1] - b[1], a[2] - b[2]};
    }

    private int commonPoints(List<int[]> a, List<int[]> b, int[] diff) {
        int commonPoints = 0;
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if (a.get(i)[0] == b.get(j)[0] + diff[0] &&
                    a.get(i)[1] == b.get(j)[1] + diff[1] &&
                    a.get(i)[2] == b.get(j)[2] + diff[2]) {
                    commonPoints++;
                }
            }
        }
        return commonPoints;
    }

    private int[] compareScans(List<int[]> a, List<int[]> b) {
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                int[] diff = diff(a.get(i), b.get(j));
                int commonPoints = commonPoints(a, b, diff);
                //System.out.println("Translation " + Arrays.toString(diff) + " has " + commonPoints);
                if (commonPoints >=12) {
                    // match
                    return diff;
                }
            }
        }
        return null;
    }

    private List<int[]> rotate(List<int[]> vectors, Function<int[], int[]> rotationFunction) {
        return vectors.stream().map(v -> rotationFunction.apply(v)).collect(toList());
    }

    private int[] compareScansWithRotations(List<int[]> a, List<int[]> b) {
        int[] diff = null;

        INDArray byX90 = Nd4j.create(new int[][] {
                {1, 0, 0},
                {0, 0, -1},
                {0, 1, 0}
        });

        INDArray byX180 = Nd4j.create(new int[][] {
                {1, 0, 0},
                {0, -1, 0},
                {0, 0, -1}
        });

        INDArray byX270 = Nd4j.create(new int[][] {
                {1, 0, 0},
                {0, 0, 1},
                {0, -1, 0}
        });

        INDArray byY90 = Nd4j.create(new int[][] {
                {0, 0, 1},
                {0, 1, 0},
                {-1, 0, 0}
        });

        INDArray byZ90 = Nd4j.create(new int[][] {
                {0, -1, 0},
                {1, 0, 0},
                {0, 0, 1}
        });


        diff = compareScans(a, b);  // no rotation
        if (diff != null) {
            return diff;
        }
        List<int[]> bRot1 = rotate(b, (v) -> new int[] {-v[0], -v[1], -v[2]});
        diff = compareScans(a, bRot1);
        if (diff != null) {
            return diff;
        }
        List<int[]> bRot2 = rotate(b, (v) -> new int[] {v[1], v[2], v[0]});
        diff = compareScans(a, bRot1);
        if (diff != null) {
            return diff;
        }
        List<int[]> bRot4 = rotate(b, (v) -> new int[] {v[1], v[0], v[2]});
        diff = compareScans(a, bRot1);
        if (diff != null) {
            return diff;
        }
        List<int[]> bRot3 = rotate(b, (v) -> new int[] {v[2], v[0], v[1]});
        diff = compareScans(a, bRot1);
        if (diff != null) {
            return diff;
        }
        List<int[]> bRot5 = rotate(b, (v) -> new int[] {v[2], v[1], v[0]});
        diff = compareScans(a, bRot1);
        if (diff != null) {
            return diff;
        }

//        List<int[]> bRot2 = rotate(b, (v) ->  Nd4j.create(new int[][] {{v[0], v[1], v[2]}}).mmul(byY90).mmul(byX90).toIntVector());
//        diff = compareScans(a, bRot2);
//        if (diff != null) {
//            return diff;
//        }
//        List<int[]> bRot3 = rotate(b, (v) ->  Nd4j.create(new int[][] {{v[0], v[1], v[2]}}).mmul(byY90).mmul(byX180).toIntVector());
//        diff = compareScans(a, bRot3);
//        if (diff != null) {
//            return diff;
//        }
//        List<int[]> bRot4 = rotate(b, (v) ->  Nd4j.create(new int[][] {{v[0], v[1], v[2]}}).mmul(byY90).mmul(byX270).toIntVector());
//        diff = compareScans(a, bRot4);
//        if (diff != null) {
//            return diff;
//        }
//        List<int[]> bRot5 = rotate(b, (v) -> new int[] {-v[0], -v[2], v[1]});
//        diff = compareScans(a, bRot5);
//        if (diff != null) {
//            return diff;
//        }
        return null;
    }

    public long solve(String file) {
//        INDArray byX90 = Nd4j.create(new int[][] {
//                {1, 0, 0},
//                {0, 0, -1},
//                {0, 1, 0}
//        });
//
//        INDArray vec = Nd4j.create(new int[][] {{1,2, 3}});
//
//        System.out.println(vec);
//        System.out.println(vec.transpose());
//        System.out.println(vec.mmul(byX90));

        List<List<int[]>> scannersData = new ScanCollector(file).processGroups();
        // find subset with offset and changed orientation
        for (int i = 0; i < scannersData.size(); i++) {
            for (int j = i + 1; j < scannersData.size(); j++) {
                List<int[]> scanI = scannersData.get(i);
                // todo all rotations
                List<int[]> scanJ = scannersData.get(j);

                int[] diff = compareScansWithRotations(scanI, scanJ);
                if (diff != null) {
                    System.out.printf("Scan %d match %d with translation %s\n", i, j, Arrays.toString(diff));
                }
            }
        }

        return 0;
    }

    public long solve2(String file) {
        List<Integer> data = stream(new StringCollector(file)
                .process().get(0).split(",")).map(Integer::valueOf).collect(toList());
        System.out.println(data);
        return data.get(0)+22;
    }

    public static void main(String[] args) {
        System.out.println(Day19.class);
        long count;
        //*
        count = new Day19(true).solve("day19_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 11;

        count = new Day19(true).solve("day19.txt");
        System.out.println("Result: " + count);
        assert count == 22;

        //*/

        count = new Day19(true).solve2("day19_test.txt");
        System.out.println("Result: " + count);
        assert count == 33;

        count = new Day19(true).solve2("day19.txt");
        System.out.println("Result: " + count);
        assert count == 44;
        //*/
    }
}
