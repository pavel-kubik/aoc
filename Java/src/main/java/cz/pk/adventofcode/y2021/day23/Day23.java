package cz.pk.adventofcode.y2021.day23;

import cz.pk.adventofcode.util.StringCollector;
import cz.pk.adventofcode.util.Vector2;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

import static cz.pk.adventofcode.y2021.day23.Day23.Field.EMPTY;
import static cz.pk.adventofcode.y2021.day23.Day23.Field.WALL;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;

@Data
public class Day23 {

    private final boolean debug;

    enum Field {
        WALL('#'),
        EMPTY('.'),
        A('A'),
        B('B'),
        C('C'),
        D('D');

        private static final Map<Character, Field> values;

        static {
            values = new HashMap<>();
            Arrays.stream(values()).forEach(p -> values.put(p.value, p));
        }

        private char value;

        Field(char value) {
            this.value = value;
        }

        public static Field get(Character value) {
            return values.get(value);
        }
    }

    @Data
    @AllArgsConstructor
    public static class Amphipod {
        private char name;
        private int x;
        private int y;

        public boolean isInHole() {
            return y >= 2;
        }

        public int getMoveCost() {
            return getMovePrice(name);
        }
    }

    private static int getMovePrice(char name) {
        return switch (name) {
            case 'A' -> 1;
            case 'B' -> 10;
            case 'C' -> 100;
            case 'D' -> 1000;
            default -> throw new RuntimeException("Unknown name " + name);
        };
    }

    public static class Game {
        private Field[][] board;
        private int minCost = Integer.MAX_VALUE;
        private boolean debug;

        public Game(List<String> board, boolean debug) {
            this.board = new Field[board.size()][board.get(0).length()];
            for (int j = 0; j < board.size(); j++) {
                for (int i = 0; i < board.get(j).length(); i++) {
                    char field = board.get(j).charAt(i);
                    this.board[j][i] = Field.get(field);
                }
            }
            this.debug = debug;
        }

        public List<Amphipod> getAmphipods() {
            List<Amphipod> amphipods = new ArrayList<>();
            for (int j = 0; j < board.length; j++) {
                for (int i = 0; i < board[j].length; i++) {
                    if (!board[j][i].equals(WALL) && !board[j][i].equals(EMPTY)) {
                        amphipods.add(new Amphipod(board[j][i].value, i, j));
                    }
                }
            }
            return amphipods;
        }

        public boolean isFinalState() {
            return countCorrectPositions() == 4*(board.length - 3);
        }

        public int countCorrectPositions() {
            int out = 0;
            for (int i = 2; i < board.length - 1; i++) {
                if (board[i][3].value == 'A') out++;
            }
            for (int i = 2; i < board.length - 1; i++) {
                if (board[i][5].value == 'B') out++;
            }
            for (int i = 2; i < board.length - 1; i++) {
                if (board[i][7].value == 'C') out++;
            }
            for (int i = 2; i < board.length - 1; i++) {
                if (board[i][9].value == 'D') out++;
            }
            return out;
        }

        private int getDestinationColumn(Amphipod amphipod) {
            return switch (amphipod.getName()) {
                case 'A' -> 3;
                case 'B' -> 5;
                case 'C' -> 7;
                case 'D' -> 9;
                default -> throw new RuntimeException("Unknown name " + amphipod.getName());
            };
        }

        /**
         *
         * @param from - point in hole
         * @param to - point in corridor
         * @return
         */
        public boolean isFreeWay(Vector2<Integer> from, Vector2<Integer> to) {
            // up & sides
            for (int i = from.getY()-1; i >= 1; i--) {
                if (!EMPTY.equals(board[i][from.getX()])) {
                    return false;
                }
            }
            int dx = to.getX() > from.getX() ? 1 : -1;
            for (int j = from.getX(); j != to.getX(); j += dx) {
                if (!EMPTY.equals(board[to.getY()][j])) {
                    return false;
                }
            }
            return true;
        }

        public void revertMove(Amphipod amphipod, Vector2<Integer> to) {
            board[amphipod.getY()][amphipod.getX()] = Field.get(amphipod.getName());
            board[to.getY()][to.getX()] = EMPTY;
        }

        public int move(Amphipod amphipod, Vector2<Integer> to) {
            int steps = 0;
            Vector2<Integer> from = new Vector2<>(amphipod.getX(), amphipod.getY());
            int dx = to.getX() > from.getX() ? 1 : -1;
            if (amphipod.isInHole()) {
                // up
                for (int i = from.getY() - 1; i >= 1; i--) {
                    if (!EMPTY.equals(board[i][from.getX()])) {
                        return -1;
                    }
                    steps++;
                }
                // side
                for (int j = from.getX(); j != to.getX(); j += dx) {
                    if (!EMPTY.equals(board[to.getY()][j])) {
                        return -1;
                    }
                    steps++;
                }
            } else {
                // side
                for (int j = from.getX(); j != to.getX(); j += dx) {
                    if (!EMPTY.equals(board[from.getY()][j+dx])) {
                        return -1;
                    }
                    steps++;
                }
                // down
                for (int i = from.getY()+1; i <= to.getY(); i++) {
                    if (!EMPTY.equals(board[i][to.getX()])) {
                        return -1;
                    }
                    steps++;
                }
            }
            board[amphipod.getY()][amphipod.getX()] = EMPTY;
            board[to.getY()][to.getX()] = Field.get(amphipod.getName());
            return amphipod.getMoveCost()*steps;
        }

        private List<Vector2<Integer>> corridorPoints = List.of(
                new Vector2<>(1, 1),
                new Vector2<>(2, 1),
                new Vector2<>(4, 1),
                new Vector2<>(6, 1),
                new Vector2<>(8, 1),
                new Vector2<>(10, 1),
                new Vector2<>(11, 1));

        public List<Vector2<Integer>> getValidMoves(Amphipod amphipod) {
            List<Vector2<Integer>> moves = new ArrayList<>();
            if (amphipod.isInHole()) {
                boolean isHome = getDestinationColumn(amphipod) == amphipod.getX();

                boolean containsOtherAmhipod = false;
                for (int i = 2; i < board.length - 1; i++) {
                    if (!board[i][amphipod.getX()].equals(EMPTY) &&
                        board[i][amphipod.getX()].value != amphipod.getName()) {
                        containsOtherAmhipod = true;
                        break;
                    }
                }
                if (isHome && !containsOtherAmhipod) {
                    return emptyList();
                }
                Vector2<Integer> from = new Vector2<>(amphipod.getX(), amphipod.getY());
                for (Vector2<Integer> corridorPoint : corridorPoints) {
                    if (EMPTY.equals(board[corridorPoint.getY()][corridorPoint.getX()]) && isFreeWay(from, corridorPoint)) {
                        moves.add(corridorPoint);
                    }
                }
            } else {
                // can go home
                int x = getDestinationColumn(amphipod);
                int y = board.length - 2;
                boolean emptyOrSameKind = true;
                while (!EMPTY.equals(board[y][x]) && y >= 2) {
                    if (amphipod.getName() == board[y][x].value ||
                        board[y][x].equals(EMPTY)) {
                        y--;
                    } else {
                        emptyOrSameKind = false;
                        break;
                    }
                }
                if (y >= 2 && emptyOrSameKind) {
                    Vector2<Integer> home = new Vector2<>(x, y);
                    if (isFreeWay(
                            home,
                            new Vector2<>(amphipod.getX(), amphipod.getY()))) {
                        moves.add(home);
                    }
                }

            }
            return moves;
        }

        public long step(int energy, int steps) {
//            if (steps > 30) {
//                System.out.println("Too much steps");
//                return -1;
//            }
            // iterate all movable amphipods
            List<Amphipod> amphipods = getAmphipods();
            assert amphipods.size() == (board.length - 3)*4;
            for (int i = 0; i < amphipods.size(); i++) {
                Amphipod amphipod = amphipods.get(i);
                // iterate all valid positions
                for (Vector2<Integer> move : getValidMoves(amphipod)) {
                    if (debug) System.out.println("Move " + amphipod + " to " + move);
                    // make move
                    int stepEnergy = this.move(amphipod, move);
                    if (stepEnergy == -1) {
                        // not valid
                    } else if (this.isFinalState()) {
                        if (minCost > energy + stepEnergy) {
                            minCost = energy + stepEnergy;
                            System.out.println("New min cost=" + minCost);
                        }
                    } else {
                        if (energy + stepEnergy < minCost) {
                            if (debug) System.out.println(this);
                            this.step(energy + stepEnergy, steps + 1);
                        } else {
                            if (debug) System.out.println("Too much energy");
                        }
                    }
                    this.revertMove(amphipod, move);
                }
            }
            return 0;
        }

        public long getCost() {
            return minCost;
        }

        public List<String> board() {
            List<String> out = new ArrayList<>();
            for (int i = 0; i < board.length; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < board[i].length; j++) {
                    sb.append(board[i][j].value);
                }
                out.add(sb.toString());
            }
            return out;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    sb.append(board[i][j].value);
                }
                sb.append('\n');
            }
            return "Game\n" +
                    sb.toString();
        }
    }

    public long solve(String file) {
        List<String> data = new StringCollector(file).process();
        Game game = new Game(data, debug);

        System.out.println(game);

        game.step(0, 0);

        return game.getCost();
    }

    public long solve2(String file) {
        List<String> data = new StringCollector(file).process();
        Game game = new Game(data, debug);

        System.out.println(game);

        game.step(0, 0);

        return game.getCost();
    }

    public static void main(String[] args) {
        System.out.println(Day23.class);
        long count;
        //*
        count = new Day23(false).solve("2021/day23_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 12521;

        count = new Day23(false).solve("2021/day23.txt");
        System.out.println("Result: " + count);
        assert count == 14346;

        //*/

        count = new Day23(false).solve2("2021/day23b_test.txt");
        System.out.println("Result: " + count);
        assert count == 44169;

        count = new Day23(false).solve2("2021/day23b.txt");
        System.out.println("Result: " + count);
        assert count == 48984;
        //*/

        assert "VM options -ea set. You can delete this.".isEmpty();
        throw new RuntimeException("Don't forgot to set vm option -ea");
    }
}
