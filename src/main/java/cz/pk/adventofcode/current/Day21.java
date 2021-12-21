package cz.pk.adventofcode.current;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.StringCollector;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

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

    public long solve2(String file) {
        List<Integer> data = stream(new StringCollector(file)
                .process().get(0).split(",")).map(Integer::valueOf).collect(toList());
        System.out.println(data);
        return data.get(0)+22;
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
        assert count == 22; //not 65260

        //*/

        count = new Day21(true).solve2("day_test.txt");
        System.out.println("Result: " + count);
        assert count == 33;

        count = new Day21(true).solve2("day.txt");
        System.out.println("Result: " + count);
        assert count == 44;
        //*/

        assert "VM options -ea set. You can delete this.".isEmpty();
        throw new RuntimeException("Don't forgot to set vm option -ea");
    }
}
