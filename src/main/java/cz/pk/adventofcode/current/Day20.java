package cz.pk.adventofcode.current;

import java.io.IOException;
import java.util.ArrayList;
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
                    .append("\n");
            for (int i = 0; i < pic.length; i++) {
                for (int j = 0; j < pic.length; j++) {
                    out.append(pic[i][j] == 1 ? "#" : ".");
                }
                out.append("\n");
            }
            return out.toString();
        }
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

        long mul = 1;
        for (Map.Entry<Integer, List<Integer>> entry : matches.entrySet()) {
            if (entry.getValue().size() == 2) {
                System.out.println(entry.getKey());
                mul *= entry.getKey();
            }
        }

        if (part == 1) {
            return mul;
        } else {
            // part 2 - fuck, do match
            int waterTiles = 0;
            
            return waterTiles;
        }
    }

    public long solve2(String file) throws IOException {
        List<Image> images = new ImageCollector(file).processGroups();
        System.out.println(images.size());
        return 0;
    }

    public static void main(String[] args) throws IOException {
        long count;
        //*
        count = new Day20(true).solve("day20_test.txt", 1);    //9 tiles
        System.out.println("Result: " + count);
        assert count == 20899048083289l;

        count = new Day20(true).solve("day20.txt", 1); //144 tiles
        System.out.println("Result: " + count);
        assert count == 32287787075651l;

        count = new Day20(true).solve("day20_test.txt", 2);    //9 tiles
        System.out.println("Result: " + count);
        assert count == 273;

        count = new Day20(true).solve("day20.txt", 2); //144 tiles
        System.out.println("Result: " + count);
        //assert count == 273;
        //*/
    }
}
