package cz.pk.adventofcode.y2020.day20;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.sun.jdi.InternalException;
import cz.pk.adventofcode.util.GroupCollector;
import cz.pk.adventofcode.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Day20 {

    private final boolean debug;
    private Map<Integer, Image> imagesById;
    private Map<Integer, List<Integer>> matches;
    private static final String monsterPattern =
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

        public Image rotate() {
            System.out.println("Rotate");
            int sizeW = pic.length;
            int sizeH = pic[0].length;
            Integer[][] out = new Integer[sizeH][sizeW];
            for (int i = 0; i < sizeW; i++) {
                for (int j = 0; j < sizeH; j++) {
                    out[j][sizeW - i - 1] = pic[i][j];
                }
            }
            pic = out;
            return this;
        }

        /**
         * Flip by vertical axis
         */
        public Image flipVertical() {
            System.out.println("Flip by vertical axis");
            int sizeW = pic.length;
            int sizeH = pic[0].length;
            Integer[][] out = new Integer[sizeW][sizeH];
            for (int i = 0; i < sizeW; i++) {
                for (int j = 0; j < sizeH; j++) {
                    out[i][j] = pic[i][sizeH - j - 1];
                }
            }
            pic = out;
            return this;
        }

        /**
         * Flip by horizontal axis
         */
        public Image flipHorizontal() {
            System.out.println("Flip by horizontal axis");
            int sizeW = pic.length;
            int sizeH = pic[0].length;
            Integer[][] out = new Integer[sizeW][sizeH];
            for (int i = 0; i < sizeW; i++) {
                for (int j = 0; j < sizeH; j++) {
                    out[i][j] = pic[sizeW - i - 1][j];
                }
            }
            pic = out;
            return this;
        }

        public Image flipAndRotate(int to, int move) {
            if (move >= 2 && move < 6) {
                //flip(move);
            }
            if (move >= 4) {
                move -= 4;
            }
            if (move < 4) {
                // just rotate until it is right
                while (move % 4 != to) {
                    this.rotate();
                    move++;
                }
            }
            return this;
        }

        public Image flip(Integer move) {
            if (move % 2 == 0) {
                this.flipVertical();
            } else {
                this.flipHorizontal();
            }
            return this;
        }
    }

    private String toPic(Integer[][] pic) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < pic.length; i++) {
            for (int j = 0; j < pic[i].length; j++) {
                if (pic[i][j] == null) {
                    out.append(" ");
                } else if (Integer.valueOf(2).equals(pic[i][j])) {
                    out.append("O");
                } else {
                    out.append(Integer.valueOf(1).equals(pic[i][j]) ? "#" : ".");
                }
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
                pic[l - 1] = loadLine(groupLines.get(l));
            }
            return new Image(id, pic, new HashSet<>(enumerateBorder(pic)));
        }
    }

    private static Integer[] loadLine(String line) {
        return line.chars()
                .mapToObj(i -> (char) i)
                .map(c -> c == '#' ? 1 : 0)
                .collect(Collectors.toList())
                .toArray(new Integer[1]);
    }

    private List<Long> enumerateBorder(Integer[][] pic) {
        List<Long> borders = new ArrayList<>();
        borders.add(extractRow(0, false, pic)); // TOP
        borders.add(extractCol(9, false, pic)); // RIGHT
        borders.add(extractRow(9, true, pic));  // BOTTOM rev
        borders.add(extractCol(0, true, pic));  // LEFT rev
        borders.add(extractRow(0, true, pic));  // TOP rev
        borders.add(extractCol(9, true, pic));  // RIGHT rev
        borders.add(extractRow(9, false, pic)); // BOTTOM
        borders.add(extractCol(0, false, pic)); // LEFT
        return borders;
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
        imagesById = new HashMap<>();
        images.stream()
                .forEach(i -> imagesById.put(i.id, i));

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
            order[0][0] = corners.get(3);

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
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < order.length; i++) {
                for (int j = 0; j < order[i].length; j++) {
                    sb.append(order[i][j]).append(" ");
                }
                sb.append("\n");
            }
            System.out.println(sb.toString());


            //*
            // rotation [0,0]
            Image base = imagesById.get(order[0][0]);
            Image right = imagesById.get(order[0][1]);

            List<Long> baseBorders = enumerateBorder(base.pic);
            List<Long> rightBorders = enumerateBorder(right.pic);

            Pair<Integer> rightIdxs = matchIndexes(baseBorders, rightBorders);

            System.out.println("Move: " + rightIdxs);
            base.flipAndRotate(1, rightIdxs.first);//.flipHorizontal();

            // rotate rest of first row
            for (int y = 1; y < order.length; y++) {
                Image imageA = imagesById.get(order[0][y - 1]);
                Image imageB = imagesById.get(order[0][y]);
                Pair<Integer> move = matchIndexes(enumerateBorder(imageA.pic), enumerateBorder(imageB.pic));
                System.out.println("Move: " + move);
                imagesById.get(order[0][y]).flipAndRotate(3, move.second);
                //                if (move.b >= 2 && move.b < 6) {
                //                    imagesById.get(order[0][y]).flip(move.b);
                //                }
                if (move.second < 4) {
                    imagesById.get(order[0][y]).flipHorizontal();
                }
            }
            // rotate others row
            for (int x = 1; x < order.length; x++) {
                for (int y = 0; y < order.length; y++) {
                    Image imageA = imagesById.get(order[x - 1][y]);
                    Image imageB = imagesById.get(order[x][y]);
                    Pair<Integer> move = matchIndexes(enumerateBorder(imageA.pic), enumerateBorder(imageB.pic));
                    System.out.println("Move: " + move);
                    imagesById.get(order[x][y]).flipAndRotate(0, move.second);
                    if (move.second < 4) {
                        imagesById.get(order[x][y]).flipVertical();
                    }
                }
            }
            //*/

            // reconstruct image
            int BOX_SIZE = 8;
//            int BOX_SIZE = 11;
            Integer[][] finalImage = new Integer[BOX_SIZE * size][BOX_SIZE * size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    final int imgId = order[i][j];
                    Integer[][] img = images.stream()
                            .filter(im -> im.id == imgId)
                            .findFirst()
                            .get()
                            .pic;

                    placeImage(finalImage, img, i * BOX_SIZE, j * BOX_SIZE, 1, 9);
//                    placeImage(finalImage, img, i * BOX_SIZE, j * BOX_SIZE, 0, 10);
                }
            }

            System.out.println(toPic(finalImage));

            String[] monsterLines = monsterPattern.split("\n");
            Integer[][] monsterPic = new Integer[monsterLines.length][];
            for (int i = 0; i < monsterLines.length; i++) {
                String line = monsterLines[i];
                monsterPic[i] = loadLine(line);
            }

            Image monsterImage = new Image(0, monsterPic, null);

            Integer[][] finalImageWithoutMonsters = new Integer[BOX_SIZE * size][BOX_SIZE * size];
            for (int i = 0; i < finalImage.length; i++) {
                for (int j = 0; j < finalImage[i].length; j++) {
                    finalImageWithoutMonsters[i][j] = finalImage[i][j];
                }
            }

            for (int r = 0; r < 8; r++) {
                // remove monster

                Integer[][] monster = monsterImage.pic;
                //                System.out.println("Monster " + r);
                //                System.out.println(monsterImage);

                for (int i = 0; i < finalImage.length - monster.length + 1; i++) {
                    for (int j = 0; j < finalImage[i].length - monster[0].length + 1; j++) {
                        if (match(finalImage, monster, i, j)) {
                            System.out.println("Monster at " + new Pair<>(i, j));
                            remove(finalImageWithoutMonsters, monster, i, j);
                        }
                    }
                }
                monsterImage.rotate();
                if (r == 3) {
                    monsterImage.flipVertical();
                }
            }

            System.out.println(toPic(finalImageWithoutMonsters));

            System.out.println("Final image is " + finalImageWithoutMonsters.length + ":" + finalImageWithoutMonsters[0].length);

            int waves = 0;
            for (int i = 0; i < finalImageWithoutMonsters.length; i++) {
                for (int j = 0; j < finalImageWithoutMonsters[i].length; j++) {
                    if (finalImageWithoutMonsters[i][j].equals(1)) {
                        waves++;
                    }
                }
            }

            return waves;
        }
    }

    private boolean match(Integer[][] map, Integer[][] pattern, int dx, int dy) {
        for (int x = 0; x < pattern.length; x++) {
            for (int y = 0; y < pattern[x].length; y++) {
                assert x + dx < map.length;
                assert y + dy < map[0].length;
                if (pattern[x][y].equals(1)) {
                    if (map[dx + x][dy + y].equals(0)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void remove(Integer[][] map, Integer[][] pattern, int dx, int dy) {
        for (int x = 0; x < pattern.length; x++) {
            for (int y = 0; y < pattern[x].length; y++) {
                if (pattern[x][y].equals(1)) {
                    map[dx + x][dy + y] = 2;
                }
            }
        }
    }

    private boolean isReverse(int a) {
        return a >= 2 && a < 6;
    }

    private boolean reverse(int a, int b) {
        return isReverse(a) && !isReverse(b) || !isReverse(a) && isReverse(b);
    }

    private Pair<Integer> matchIndexes(List<Long> ac, List<Long> bc) {
        for (int i = 0; i < ac.size(); i++) {
            for (int j = 0; j < bc.size(); j++) {
                if (ac.get(i).equals(bc.get(j))) {
                    return new Pair(i, j);
                }
            }
        }
        throw new InternalException("Not match");
    }

    private void placeImage(Integer[][] out, Integer[][] pic, int x, int y, int from, int to) {
        for (int i = from; i < to; i++) {
            for (int j = from; j < to; j++) {
                out[x + i - from][y + j - from] = pic[i][j];
            }
        }
    }

    public long solve2(String file) throws IOException {
        List<Image> images = new ImageCollector(file).processGroups();
        System.out.println(images.size());
        return 0;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(Day20.class);
        long count;
        /*
        count = new Day20(true).solve("day20_test.txt", 1);    //9 tiles
        System.out.println("Result: " + count);
        assert count == 20899048083289l;

        count = new Day20(true).solve("day20.txt", 1); //144 tiles
        System.out.println("Result: " + count);
        assert count == 32287787075651l;
        /*/

//        count = new Day20(true).solve("day20_test.txt", 2);    //9 tiles
//        System.out.println("Result: " + count);
//        assert count == 273;

        count = new Day20(true).solve("2020/day20.txt", 2); //144 tiles
        System.out.println("Result: " + count);
        //        assert count == 273;
        // 2209 too height (not 2208)
        // 303  too low
        //*/
    }
}
