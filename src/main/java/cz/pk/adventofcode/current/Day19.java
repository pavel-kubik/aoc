package cz.pk.adventofcode.current;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import cz.pk.adventofcode.util.GroupCollector;
import cz.pk.adventofcode.util.StringCollector;
import cz.pk.adventofcode.util.Vector3;
import lombok.AllArgsConstructor;
import lombok.Data;

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
                if (commonPoints >= 12) {
                    // match
                    return diff;
                }
            }
        }
        return null;
    }

    private List<int[]> rotate(List<int[]> vectors, int[] sign, BiFunction<int[], int[], int[]> rotationFunction) {
        return vectors.stream().map(v -> rotationFunction.apply(sign, v)).collect(toList());
    }

    private int[] compareScansWithRotations(int scanA, int scanB, List<int[]> a, List<int[]> b) {
        int[] diff = null;

        List<BiFunction<int[], int[], int[]>> rotations = new ArrayList<>();
        rotations.add((sign, v) -> new int[] {v[0]*sign[0], v[1]*sign[1], v[2]*sign[2]});
        rotations.add((sign, v) -> new int[] {v[0]*sign[0], v[2]*sign[1], v[1]*sign[2]});
        rotations.add((sign, v) -> new int[] {v[1]*sign[0], v[0]*sign[1], v[2]*sign[2]});
        rotations.add((sign, v) -> new int[] {v[1]*sign[0], v[2]*sign[1], v[0]*sign[2]});
        rotations.add((sign, v) -> new int[] {v[2]*sign[0], v[0]*sign[1], v[1]*sign[2]});
        rotations.add((sign, v) -> new int[] {v[2]*sign[0], v[1]*sign[1], v[0]*sign[2]});

        for (BiFunction<int[], int[], int[]> rotation : rotations) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    for (int k = -1; k < 2; k++) {
                        if (i == 0 || j == 0 || k == 0) {
                            continue;
                        }
                        int[] sign = new int[] {i, j, k};
                        diff = compareScans(a, rotate(b, sign, rotation));
                        if (diff != null) {
                            probesRelations.put(100 * scanA + scanB, new ProbeRelation(diff, sign, rotation));
                            return diff;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Data
    @AllArgsConstructor
    private class ProbeRelation {
        private final int[] translation;
        private final int[] sign;
        private final BiFunction<int[], int[], int[]> rotationFunction;

        public ProbeRelation add(ProbeRelation that) {
            return new ProbeRelation(
                    new int[] {
                            this.translation[0] + that.translation[0],
                            this.translation[1] + that.translation[1],
                            this.translation[2] + that.translation[2]},
                    new int[] {
                            this.sign[0] * that.sign[0],
                            this.sign[1] * that.sign[1],
                            this.sign[2] * that.sign[2]},
                    this.rotationFunction.andThen(that.rotationFunction));
        }
    }

    private Map<Integer, ProbeRelation> probesRelations = new HashMap<>();

    public long solve(String file) {
        List<List<int[]>> scannersData = new ScanCollector(file).processGroups();
        // find subset with offset and changed orientation
        for (int i = 0; i < scannersData.size(); i++) {
            for (int j = i + 1; j < scannersData.size(); j++) {
                List<int[]> scanI = scannersData.get(i);
                List<int[]> scanJ = scannersData.get(j);

                int[] diff = compareScansWithRotations(i, j, scanI, scanJ);
                if (diff != null) {
                    System.out.printf("Scan %d match %d with translation %s\n", i, j, Arrays.toString(diff));
                }
            }
        }

        // expand probe relations
        probesRelations = new HashMap<>();
        for (int j = 0; j < scannersData.size(); j++) {
            if (probesRelations.containsKey(j)) {
                // expand mapping `0 -> j` to `0 -> j -> i`
                for (Map.Entry<Integer, ProbeRelation> entry : probesRelations.entrySet()) {
                    int fromProbe = entry.getKey() / 100;
                    int toProbe = entry.getKey() % 100;
                    if (fromProbe == j) {
                        probesRelations.put(toProbe, probesRelations.get(j).add(entry.getValue()));
                    }
                }
            }
        }

        Set<Vector3<Integer>> beacons = new HashSet<>();
        for (int scan = 0; scan < scannersData.size(); scan++) {
            ProbeRelation transformation = probesRelations.get(scan);
            for (int measure = 0; measure < scannersData.get(scan).size(); measure++) {
                beacons.add(new Vector3<>(
                        Arrays.stream(
                        transformation.getRotationFunction().apply(transformation.getSign(), new int[] {
                                scannersData.get(scan).get(measure)[0] + transformation.getTranslation()[0],
                                scannersData.get(scan).get(measure)[1] + transformation.getTranslation()[1],
                                scannersData.get(scan).get(measure)[2] + transformation.getTranslation()[2]}
                                )).boxed().toArray(Integer[]::new)));
            }
        }

        System.out.println(probesRelations);

        return beacons.size();
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
        assert count == 79;

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
