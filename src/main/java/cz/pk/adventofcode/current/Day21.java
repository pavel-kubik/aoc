package cz.pk.adventofcode.current;

import cz.pk.adventofcode.util.StringCollector;
import cz.pk.adventofcode.util.Vector2;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day21 {

    private final boolean debug;

    private static int move(int move, int position) {
        return (move + position) % 10;
    }

    private interface Dice {
        int next();
        int getTotal();
    }

    private class DeterministicDice implements Dice {
        int dice = 0;
        int diceTotal = 0;

        public int next() {
            if (++dice > 100) {
                dice = 1;
            }
            diceTotal++;
            System.out.println("Dice " + dice);
            return dice;
        }

        public int getTotal() {
            return diceTotal;
        }
    }

    @Data
    @AllArgsConstructor
    private class Game {
        private int player1;
        private int player2;
        private int score1;
        private int score2;
        private int winScore;
        private boolean movePlayer1;
        private List<Integer> rolls;
        private Game parentGame;
        private long player1WinSubGames;
        private long player2WinSubGames;

        public Game(int player1, int player2, int winScore, Game parentGame) {
            this(player1, player2, 0, 0, winScore, true, new ArrayList<>(), parentGame, 0, 0);
        }

        public boolean step() {
            if (movePlayer1) {
                // player 1
                int move = rolls.get(0) + rolls.get(1) + rolls.get(2);
                player1 = move(move, player1);
                score1 += player1 + 1;
                //System.out.println("Player 1 rolls " + move + " and go to " + (player1 + 1) + " with score " + score1);
                if (score1 >= 1000) {
                    return true;
                }
            } else {
                // player 2
                int move = rolls.get(0) + rolls.get(1) + rolls.get(2);
                player2 = move(move, player2);
                score2 += player2 + 1;
                //System.out.println("Player 2 rolls " + move + " and go to " + (player2 + 1) + " with score " + score2);

                if (score2 >= 1000) {
                    return true;
                }
            }
            movePlayer1 = !movePlayer1;
            return false;
        }

        public Game addRoll(int roll) {
            rolls.add(roll);
            return this;
        }

        public boolean player1Win() {
            return score1 > score2;
        }

        public Game copy() {
            return new Game(
                    player1,
                    player2,
                    score1,
                    score2,
                    winScore,
                    movePlayer1,
                    new ArrayList<>(rolls),
                    parentGame, //TODO copy or not?
                    player1WinSubGames, //TODO copy or not?
                    player2WinSubGames); //TODO copy or not?
        }
    }

    private static class DiracDice implements Dice {

        @Override
        public int next() {
            return 0;
        }

        @Override
        public int getTotal() {
            return 0;
        }
    }

    public long solve(int player1, int player2) {
        // count field 0-9
        player1--;
        player2--;

        Dice dice = new DeterministicDice();

        Game game = new Game(player1, player2, 1000, null);
        boolean end = false;
        while (!end) {
            game.setRolls(List.of(dice.next(), dice.next(), dice.next()));
            end = game.step();
        }
        System.out.println("Dice total " + dice.getTotal());

        return Math.min(game.getScore1(), game.getScore2()) * dice.getTotal();
    }

    Map<Game, Vector2<Integer>> results = new HashMap<>();

    public long solve2(int player1, int player2) {
        // count field 0-9
        player1--;
        player2--;

        Dice dice = new DiracDice();

        long player1Wins = 0;
        long player2Wins = 0;

        Stack<Game> games = new Stack<>();
        games.add(new Game(player1, player2, 21, null));
        long steps = 0;
        while (!games.isEmpty()) {
            if (steps++ % 10000000 == 0) {
                System.out.println("Step " + steps + " Games pool " + games.size() + " score " + player1Wins + " vs " + player2Wins);
            }
            Game game = games.pop();
            if (game.getRolls().size() < 3) {
                // dice
                // TODO check results before push game to queue
                games.push(game.copy().addRoll(1));
                games.push(game.copy().addRoll(2));
                games.push(game.copy().addRoll(3));
            } else {
                if (game.step()) {
                    //TODO count sub-games win for player 1 and 2 and store it to results
                    if (game.player1Win()) {
                        player1Wins++;
                    } else {
                        player2Wins++;
                    }
                } else {
                    game.setRolls(new ArrayList<>());
                    games.push(game);
                }
            }
        }

        return Math.max(player1Wins, player2Wins);
    }

    public static void main(String[] args) {
        System.out.println(Day21.class);
        long count;
        //*
        count = new Day21(true).solve(4, 8);
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 739785;

        count = new Day21(true).solve(9, 4);
        System.out.println("Result: " + count);
        assert count == 998088; //not 65260

        //*/

        count = new Day21(true).solve2(4, 8);
        System.out.println("Result: " + count);
        assert count == 444356092776315l;

        count = new Day21(true).solve2(9, 4);
        System.out.println("Result: " + count);
        assert count == 44;
        //*/

        assert "VM options -ea set. You can delete this.".isEmpty();
        throw new RuntimeException("Don't forgot to set vm option -ea");
    }
}
