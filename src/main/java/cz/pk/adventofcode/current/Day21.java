package cz.pk.adventofcode.current;

import java.util.HashMap;
import java.util.Map;

import cz.pk.adventofcode.util.Vector2;
import lombok.Data;

@Data
public class Day21 {

    private final boolean debug;

    private int move(int move, int position) {
        return (move + position) % 10;
    }

    int dice = 0;
    int diceTotal = 0;
    private int nextDice() {
        if (++dice > 100) {
            dice = 1;
        }
        diceTotal++;
        System.out.println("Dice " + dice);
        return dice;
    }

    public long solve(int player1, int player2) {
        int score1 = 0;
        int score2 = 0;

        // count field 0-9
        player1--;
        player2--;

        while (score1 < 1000 && score2 < 1000) {
            // player 1
            int move = nextDice() + nextDice() + nextDice();
            player1 = move(move, player1);
            score1 += player1 + 1;
            System.out.println("Player 1 rolls " + move + " and go to " + (player1+1) + " with score " + score1);

            if (score1 >= 1000) {
                break;
            }

            // player 2
            move = nextDice() + nextDice() + nextDice();
            player2 = move(move, player2);
            score2 += player2 + 1;
            System.out.println("Player 2 rolls " + move + " and go to " + (player2+1) + " with score " + score2);
        }
        System.out.println("Dice total " + diceTotal);

        return Math.min(score1, score2) * diceTotal;
    }

    private Vector2<Long> evalStep(int p1, int p2, int s1, int s2, boolean p1p, long times) {
        // 3 time roll  => 27x copy (but just 7 combinations)
        if (s1 >= 21) {
            return new Vector2<>(times, 0L);
        }
        if (s2 >= 21) {
            return new Vector2<>(0L, times);
        }
        Vector2<Long> wins = new Vector2<>(0L, 0L);
        if (p1p) {
            for (int i = 3; i <= 9; i++) {
                int newP1 = move(i, p1);
                Vector2<Long> c = evalStep(newP1, p2, s1 + newP1 + 1, s2, false, times*quantumRollsSum.get(i));
                wins.x += c.x;
                wins.y += c.y;
            }
        } else {
            for (int i = 3; i <= 9; i++) {
                int newP2 = move(i, p2);
                Vector2<Long> c = evalStep(p1, newP2, s1, s2 + newP2 + 1, true, times*quantumRollsSum.get(i));
                wins.x += c.x;
                wins.y += c.y;
            }
        }
        return wins;
    }

    Map<Integer, Integer> quantumRollsSum = new HashMap<>();

    public long solve2(int player1, int player2) {
        // quantum simulator
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                for (int k = 1; k <= 3; k++) {
                    System.out.printf("%d %d %d => %d\n", i, j, k, i + j + k);
                    quantumRollsSum.compute(i + j + k, (key, v) -> v != null ? v + 1 : 1);
                }
            }
        }
        System.out.println(quantumRollsSum);
        // play
        Vector2<Long> wins = evalStep(player1 - 1, player2 - 1, 0, 0, true, 1);
        System.out.println(wins);
        //444356092776315
        //341960390180808

        return Math.max(wins.x, wins.y);
    }

    public static void main(String[] args) {
        System.out.println(Day21.class);
        long count;
        /*
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
        assert count == 444356092776315L;

        count = new Day21(true).solve2(9, 4);
        System.out.println("Result: " + count);
        assert count == 306621346123766L; //< 306621346123766   WTF why it say for first time it is wrong?! BAD puzzle :-)
        //*/
    }
}

    /*
    1 1 1
        2
        3
      2 1
        2
        3
      3 1
        2
        3
    2 1 1
        2
        3
      2 1
        2
        3
      3 1
        2
        3
    3 1 1
        2
        3
      2 1
        2
        3
      3 1
        2
        3
     */
