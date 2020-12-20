package cz.pk.adventofcode.current;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import cz.pk.adventofcode.util.GroupCollector;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Day20 {

    private final boolean debug;
    private Map<Integer, List<Integer>> matches;
    private static final String monster =
            "                  # \n" +
                    "#    ##    ##    ###\n" +
                    " #  #  #  #  #  #   ";

    @Data
    @AllArgsConstructor
    class Image {
        Integer id;
        Integer[][] pic;

        Set<Long> borders;

        @Override
        public String toString() {
            StringBuilder out = new StringBuilder()
                    .append("id: ")
                    .append(id)
                    .append("\n")
                    .append(toPic(pic));
            return out.toString();
        }
    }

    private String toPic(Integer[][] pic) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < pic.length; i++) {
            for (int j = 0; j < pic[i].length; j++) {
                out.append(Integer.valueOf(1).equals(pic[i][j]) ? "#" : ".");
            }
            out.append("\n");
        }
        return out.toString();
    }

    private long extractRow(int line, boolean reverse, Integer[][] pic) {
        assert line >= 0;
        assert line < pic.length;
        long out = 0;
        long base = 1;
        for (int i = 0; i < pic[line].length; i++) {
            if (reverse) {
                out += pic[line][pic[line].length - i - 1] * base;
            } else {
                out += pic[line][i] * base;
            }
            base *= 10;
        }
        return out;
    }

    private long extractCol(int col, boolean reverse, Integer[][] pic) {
        assert col >= 0;
        assert col < pic.length;
        long out = 0;
        long base = 1;
        for (int i = 0; i < pic.length; i++) {
            if (reverse) {
                out += pic[pic[col].length - i - 1][col] * base;
            } else {
                out += pic[i][col] * base;
            }
            base *= 10;
        }
        return out;
    }

    class ImageCollector extends GroupCollector<Image> {

        public ImageCollector(String file) throws IOException {
            super(file);
        }

        @Override
        protected Image processGroup(List<String> groupLines) {
            int id = Integer.valueOf(groupLines.get(0).split(" ")[1].split(":")[0]);
            Integer[][] pic = new Integer[10][10];
            for (int l = 1; l < groupLines.size(); l++) {
                pic[l - 1] = groupLines.get(l).chars()
                        .mapToObj(i -> (char) i)
                        .map(c -> c == '#' ? 1 : 0)
                        .collect(Collectors.toList())
                        .toArray(new Integer[1]);
            }
            Set<Long> borders = new HashSet<>();
            borders.add(extractRow(0, false, pic));
            borders.add(extractRow(0, true, pic));
            borders.add(extractRow(9, false, pic));
            borders.add(extractRow(9, true, pic));
            borders.add(extractCol(0, false, pic));
            borders.add(extractCol(0, true, pic));
            borders.add(extractCol(9, false, pic));
            borders.add(extractCol(9, true, pic));
            return new Image(id, pic, borders);
        }
    }

    private void addMatch(int id, int id2) {
        matches.computeIfAbsent(id, k -> new ArrayList<>()).add(id2);
    }

    private void unMatch(int id) {
        for (Map.Entry<Integer, List<Integer>> entry : matches.entrySet()) {
            entry.getValue().remove((Integer) id);
        }
    }

    private int commonMatch(Collection<Integer> aSet, Collection<Integer> bSet) {
        for (Integer a : aSet) {
            for (Integer b : bSet) {
                if (a.equals(b)) {
                    return a;
                }
            }
        }
        throw new InternalError("No match");
    }

    public long solve(String file, int part) throws IOException {
        List<Image> images = new ImageCollector(file).processGroups();
        System.out.println(images);

        matches = new HashMap<>();
        for (Image image : images) {
            for (Image imageOut : images) {
                if (image.id == imageOut.id) {
                    continue;
                }
                for (Long border : image.borders) {
                    if (imageOut.borders.contains(border)) {
                        addMatch(image.id, imageOut.id);
                        break;
                    }
                }
            }
        }
        System.out.println(matches);

        long mul = 1;
        List<Integer> corners = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : matches.entrySet()) {
            if (entry.getValue().size() == 2) {
                //System.out.println(entry.getKey());
                corners.add(entry.getKey());
                mul *= entry.getKey();
            }
        }
        List<Integer> sides = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : matches.entrySet()) {
            if (entry.getValue().size() == 3) {
                sides.add(entry.getKey());
            }
        }

        if (part == 1) {
            return mul;
        } else {
            // part 2 - fuck, do match
            int size = (int) Math.round(Math.sqrt(images.size()));
            int[][] order = new int[size][size];
            order[0][0] = corners.get(0);

            Set<Integer> sideMatches = new HashSet<>();
            sideMatches.addAll(corners);
            sideMatches.addAll(sides);
            sideMatches.remove(order[0][0]);

            unMatch(order[0][0]);

            // first line
            for (int i = 1; i < order[0].length; i++) {
                order[0][i] = commonMatch(matches.get(order[0][i - 1]), sideMatches);
                unMatch(order[0][i]);
                sideMatches.remove((Integer) order[0][i]);
            }
            // first col
            for (int i = 1; i < order[0].length; i++) {
                order[i][0] = commonMatch(matches.get(order[i - 1][0]), sideMatches);
                unMatch(order[i][0]);
                sideMatches.remove((Integer) order[0][i]);
            }
            // rest of image
            for (int i = 1; i < order.length; i++) {
                for (int j = 1; j < order[i].length; j++) {
                    int match = commonMatch(matches.get(order[i - 1][j]), matches.get(order[i][j - 1]));
                    order[i][j] = match;
                    unMatch(match);
                }
            }

            Integer[][] finalImage = new Integer[8 * size][8 * size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    final int imgId = order[i][j];
                    Integer[][] img = images.stream()
                            .filter(im -> im.id == imgId)
                            .findFirst()
                            .get()
                            .pic;

                    //TODO orientation - flip/rotate...

                    placeImage(finalImage, img, i * 8, j * 8);
                }
            }

            System.out.println(toPic(finalImage));

            int waves = 0;


            return waves;
        }
    }

    private void placeImage(Integer[][] out, Integer[][] pic, int x, int y) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                out[x + i - 1][y + j - 1] = pic[i][j];
            }
        }
    }

    public long solve2(String file) throws IOException {
        List<Image> images = new ImageCollector(file).processGroups();
        System.out.println(images.size());
        return 0;
    }

    public static void main(String[] args) throws IOException {
        long count;
        /*
        count = new Day20(true).solve("day20_test.txt", 1);    //9 tiles
        System.out.println("Result: " + count);
        assert count == 20899048083289l;

        count = new Day20(true).solve("day20.txt", 1); //144 tiles
        System.out.println("Result: " + count);
        assert count == 32287787075651l;
        /*/

        count = new Day20(true).solve("day20_test.txt", 2);    //9 tiles
        System.out.println("Result: " + count);
        assert count == 273;

        //        count = new Day20(true).solve("day20.txt", 2); //144 tiles
        //        System.out.println("Result: " + count);
        //assert count == 273;
        //*/
    }
}
