package cz.pk.adventofcode.y2021.day19;

import java.util.*;

import cz.pk.adventofcode.util.GroupCollector;
import cz.pk.adventofcode.util.Vector2;
import cz.pk.adventofcode.util.Vector3;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * https://www.baeldung.com/java-matrix-multiplication
 *
 * https://demonstrations.wolfram.com/VectorRotationsIn3D/
 */
@Data
public class Day19 {

    public static final int SEP = 100;
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

    private int[] remapAxes(int[] a, int[] remapAxes, int[] axesSignum) {
        int[] out = new int[3];
        out[remapAxes[0]] = axesSignum[0]*a[0];
        out[remapAxes[1]] = axesSignum[1]*a[1];
        out[remapAxes[2]] = axesSignum[2]*a[2];
        return out;
    }

    private int[] translate(int[] a, int[] translation) {
        return new int[] {
                a[0] + translation[0],
                a[1] + translation[1],
                a[2] + translation[2]};
    }

    private List<int[]> rotate(List<int[]> vectors, int[] map, int[] signum) {
        return vectors.stream().map(v -> remapAxes(v, map, signum)).collect(toList());
    }

    private int[] compareScansWithRotations(int scanA, int scanB, List<int[]> a, List<int[]> b) {
        List<Vector2<int[]>> remaps = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if (i == 0 || j == 0 || k == 0) {
                        continue;
                    }
                    remaps.add(new Vector2<>(new int[] {0, 1, 2}, new int[] {i, j, k}));
                    remaps.add(new Vector2<>(new int[] {0, 2, 1}, new int[] {i, j, k}));
                    remaps.add(new Vector2<>(new int[] {1, 0, 2}, new int[] {i, j, k}));
                    remaps.add(new Vector2<>(new int[] {1, 2, 0}, new int[] {i, j, k}));
                    remaps.add(new Vector2<>(new int[] {2, 0, 1}, new int[] {i, j, k}));
                    remaps.add(new Vector2<>(new int[] {2, 1, 0}, new int[] {i, j, k}));
                }
            }
        }

        for (Vector2<int[]> remap : remaps) {
            int[] diff = compareScans(a, rotate(b, remap.x, remap.y));    //remap = axes, signum
            if (diff != null) {
                probesRelations.put(
                        SEP * scanB + scanA,
                        new Transformation(null, remap.x, remap.y).add(
                            new Transformation(diff, null, null)));
                return diff;
            }
        }
        return null;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    private class Transformation {
        private final int[] translation;
        private final int[] remap;
        private final int[] signum;

        List<Transformation> chain;

        public int[] transform(int [] a) {
            if (chain != null) {
                int[] out = a;
                int i = 0;
                for (Transformation transformation : chain) {
                    out = transformation.transform(out);
                    //System.out.println("Chain " + i++ + ": " + Arrays.toString(out));
                }
                return out;
            } else {
                if (translation != null) {
                    return translate(a, translation);
                } else {
                    return remapAxes(a, remap, signum);
                }
            }
        }

        public Transformation inverse() {
            if (chain != null) {
                List<Transformation> inverseChain = new ArrayList<>();
                for (int i = 0; i < chain.size(); i++) {
                    inverseChain.add(chain.get(chain.size() - i - 1).inverse());
                }
                return new Transformation(null, null, null, inverseChain);
            } else {
                int[] newRemap = new int[3];
                int[] newSignum = new int[3];
                if (remap != null) {
                    newRemap[remap[0]] = 0;
                    newRemap[remap[1]] = 1;
                    newRemap[remap[2]] = 2;

                    newSignum[remap[0]] = signum[0];
                    newSignum[remap[1]] = signum[1];
                    newSignum[remap[2]] = signum[2];
                }
                return new Transformation(
                        translation != null ? new int[]{-translation[0], -translation[1], -translation[2]} : null,
                        remap != null ? newRemap : null,
                        remap != null ? newSignum : null,
                        null);
            }
        }

        public Transformation copy() {
            return new Transformation(translation, remap, signum, chain);
        }

        public Transformation add(Transformation that) {
            List<Transformation> newChain = new ArrayList<>();
            if (chain == null) {
                newChain.add(this.copy());
            } else {
                newChain.addAll(this.chain);
            }
            if (that.chain == null) {
                newChain.add(that.copy());
            } else {
                newChain.addAll(that.chain);
            }
            return new Transformation(null, null, null, newChain);
        }
    }

    private Map<Integer, Transformation> probesRelations = new HashMap<>();

    public long solve(String file) {
//        int[] a = {1, 2, 3};
//        Transformation t = new Transformation(new int[] {-10, 20, 30}, new int[] {2, 1, 0}, new int[] {-1, -1, 1});
//        System.out.println(Arrays.toString(a));
//        System.out.println(t);
//        int[] b = t.transform(a);
//        System.out.println(Arrays.toString(b));
//        System.out.println(t.inverse());
//        int[] c = t.inverse().transform(b);
//        System.out.println(Arrays.toString(c));
//        assert Arrays.equals(a, c);
//
//        Transformation t2 = new Transformation(new int[] {20, -10, 50}, new int[] {1, 0, 2}, new int[] {-1, 1, -1});
//        Transformation t3 = new Transformation(new int[] {-100, -200, -300}, new int[] {0, 1, 2}, new int[] {-1, 1, 1});
//        Transformation ch = t.add(t2).add(t3.inverse());
//        b = ch.transform(a);
//        c = ch.inverse().transform(b);
//        assert Arrays.equals(a, c);
//
//        Transformation t12 = t.add(t2);
//        Transformation t23 = t2.add(t3);
//        Transformation t123 = t12.add(t23);
//
//        b = t123.transform(a);
//        c = t123.inverse().transform(b);
//        assert Arrays.equals(a, c);

        //int[] v = new int[] { 649, 640, 665 };
        int[] v = new int[] { 1, 2, 3 };
//        Transformation trError = new Transformation(new int[] {-68, 1246, 43}, null, null)
//                .add(new Transformation(null, new int[] {0, 1, 2}, new int[] {-1, 1, -1}))
//                .add(new Transformation(new int[] {-88, -113, 1104}, null, null))
//                .add(new Transformation(null, new int[] {1, 2, 0}, new int[] {-1, -1, 1}))
//                .add(new Transformation(null, new int[] {1, 0, 2}, new int[] {1, 1, -1}))
//                .add(new Transformation(new int[] {1125, -168, 72}, null, null))
//                ;
        Transformation trError = new Transformation(null, new int[] {1, 2, 0}, new int[] {-1, -1, 1})
                .add(new Transformation(null, new int[] {1, 0, 2}, new int[] {1, 1, -1}))
                ;
        System.out.println(Arrays.toString(v));
        int[] vT = trError.transform(v);
        System.out.println(Arrays.toString(vT));
        int[] vTT = trError.inverse().transform(vT);
        System.out.println(Arrays.toString(vTT));
        assert Arrays.equals(v, vTT);

        List<List<int[]>> scannersData = new ScanCollector(file).processGroups();
        // find subset with offset and changed orientation
        for (int i = 0; i < scannersData.size(); i++) {
            for (int j = i + 1; j < scannersData.size(); j++) {
                List<int[]> scanI = scannersData.get(i);
                List<int[]> scanJ = scannersData.get(j);

                int[] diff = compareScansWithRotations(i, j, scanI, scanJ);
                if (diff != null) {
                    System.out.printf("Scan %d match %d with translation %s\n", i, j, probesRelations.get(SEP*j + i));
                }
            }
        }

        System.out.println(probesRelations);

        // expand probe relations
        for (int z = 0; z < 3; z++) {
            for (int i = 0; i < scannersData.size(); i++) {
                for (int j = 0; j < scannersData.size(); j++) {
                    if (i == j) {
                        continue;
                    }
                    if (probesRelations.containsKey(SEP * i + j)) {
                        Transformation transformationIJ = probesRelations.get(SEP * i + j);
                        if (debug) System.out.println("Extend mapping " + i + " to " + j);
                        Map<Integer, Transformation> newGen = new HashMap<>();
                        for (Map.Entry<Integer, Transformation> entry : probesRelations.entrySet()) {
                            int fromProbe = entry.getKey() / SEP;
                            int toProbe = entry.getKey() % SEP;
                            if (fromProbe == j && toProbe != i) {
                                int k = toProbe;    // j -> k
                                // i -> j extend to i -> j -> k
                                if (!probesRelations.containsKey(SEP * i + k)) {
                                    if (debug) System.out.println("Add transformation from " + i + " to " + k);
                                    newGen.put(SEP * i + k, transformationIJ.add(entry.getValue()));
                                }
                                // i -> j extend to k ->' j ->' i
                                if (!probesRelations.containsKey(SEP * k + i)) {
                                    if (debug) System.out.println("Add transformation from " + k + " to " + i);
                                    newGen.put(SEP * k + i, entry.getValue().inverse().add(transformationIJ.inverse()));
                                }
                            }
                            if (fromProbe == i && toProbe != j) {
                                int k = toProbe;    // i -> k
                                // i -> j extend to j ->' i -> k
                                // 447 when commented out
                                if (!probesRelations.containsKey(SEP * j + k)) {
                                    if (debug) System.out.println("Add transformation from " + j + " to " + k);
                                    newGen.put(SEP * j + k, transformationIJ.inverse().add(entry.getValue()));
                                }
                                // i -> j extend to k ->' i -> j
                                if (!probesRelations.containsKey(SEP * k + j)) {
                                    if (debug) System.out.println("Add transformation from " + k + " to " + j);
                                    newGen.put(SEP * k + j, entry.getValue().inverse().add(transformationIJ));
                                }
                            }
                        }
                        probesRelations.putAll(newGen);
                    }
                }
            }
        }

        Set<Vector3<Integer>> beacons = new HashSet<>();
        for (int measure = 0; measure < scannersData.get(0).size(); measure++) {
            beacons.add(new Vector3<>(Arrays.stream(scannersData.get(0).get(measure)).boxed().toArray(Integer[]::new)));
        }
        for (int scan = 1; scan < scannersData.size(); scan++) {
            Transformation transformation = probesRelations.get(SEP*scan);
            if (transformation == null) {
                System.out.println("Missing transformation " + scan + " to 0");
            }
            for (int measure = 0; measure < scannersData.get(scan).size(); measure++) {
                beacons.add(new Vector3<>(
                        Arrays.stream(transformation.transform(scannersData.get(scan).get(measure)))
                        .boxed().toArray(Integer[]::new)));
            }
        }

        return beacons.size();
    }

    public long solve2(String file) {
        List<List<int[]>> scannersData = new ScanCollector(file).processGroups();
        // find subset with offset and changed orientation
        for (int i = 0; i < scannersData.size(); i++) {
            for (int j = i + 1; j < scannersData.size(); j++) {
                List<int[]> scanI = scannersData.get(i);
                List<int[]> scanJ = scannersData.get(j);

                int[] diff = compareScansWithRotations(i, j, scanI, scanJ);
                if (diff != null) {
                    System.out.printf("Scan %d match %d with translation %s\n", i, j, probesRelations.get(SEP*j + i));
                }
            }
        }

        System.out.println(probesRelations);

        // expand probe relations
        for (int z = 0; z < 3; z++) {
            for (int i = 0; i < scannersData.size(); i++) {
                for (int j = 0; j < scannersData.size(); j++) {
                    if (i == j) {
                        continue;
                    }
                    if (probesRelations.containsKey(SEP * i + j)) {
                        Transformation transformationIJ = probesRelations.get(SEP * i + j);
                        Map<Integer, Transformation> newGen = new HashMap<>();
                        for (Map.Entry<Integer, Transformation> entry : probesRelations.entrySet()) {
                            int fromProbe = entry.getKey() / SEP;
                            int toProbe = entry.getKey() % SEP;
                            if (fromProbe == j && toProbe != i) {
                                int k = toProbe;    // j -> k
                                // i -> j extend to i -> j -> k
                                if (!probesRelations.containsKey(SEP * i + k)) {
                                    newGen.put(SEP * i + k, transformationIJ.add(entry.getValue()));
                                }
                                // i -> j extend to k ->' j ->' i
                                if (!probesRelations.containsKey(SEP * k + i)) {
                                    newGen.put(SEP * k + i, entry.getValue().inverse().add(transformationIJ.inverse()));
                                }
                            }
                            if (fromProbe == i && toProbe != j) {
                                int k = toProbe;    // i -> k
                                // i -> j extend to j ->' i -> k
                                // 447 when commented out
                                if (!probesRelations.containsKey(SEP * j + k)) {
                                    newGen.put(SEP * j + k, transformationIJ.inverse().add(entry.getValue()));
                                }
                                // i -> j extend to k ->' i -> j
                                if (!probesRelations.containsKey(SEP * k + j)) {
                                    newGen.put(SEP * k + j, entry.getValue().inverse().add(transformationIJ));
                                }
                            }
                        }
                        probesRelations.putAll(newGen);
                    }
                }
            }
        }

        // count Manhattan distance
        Set<Vector3<Integer>> scanners = new HashSet<>();
        scanners.add(new Vector3<>(0, 0, 0));
        for (int scan = 1; scan < scannersData.size(); scan++) {
            Transformation transformation = probesRelations.get(SEP*scan);
            if (transformation == null) {
                System.out.println("Missing transformation " + scan + " to 0");
            }
            scanners.add(new Vector3<>(Arrays.stream(
                    transformation.transform(new int[] {0, 0, 0}))
                    .boxed()
                    .toArray(Integer[]::new)));

        }
        long maxDistance = 0;
        for (Vector3<Integer> scannerA: scanners) {
            for (Vector3<Integer> scannerB : scanners) {
                long currentDistance = manhattanDistance(scannerA, scannerB);
                if (maxDistance < currentDistance) {
                    maxDistance = currentDistance;
                }
            }
        }

        return maxDistance;
    }

    private long manhattanDistance(Vector3<Integer> a, Vector3<Integer> b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY()) + Math.abs(a.getZ() - b.getZ());
    }

    public static void main(String[] args) {
        System.out.println(Day19.class);
        long count;
        //*
        count = new Day19(true).solve("2021/day19_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 79;

        count = new Day19(true).solve("2021/day19.txt");
        System.out.println("Result: " + count);
        assert count == 342;

        //*/

        count = new Day19(true).solve2("2021/day19_test.txt");
        System.out.println("Result: " + count);
        assert count == 3621;

        count = new Day19(true).solve2("2021/day19.txt");
        System.out.println("Result: " + count);
        assert count == 9668; // > 9561
        //*/
    }
}
