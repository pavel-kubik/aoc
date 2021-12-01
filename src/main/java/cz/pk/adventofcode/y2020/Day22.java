package cz.pk.adventofcode.y2020;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import cz.pk.adventofcode.util.GroupCollector;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Day22 {

    private final boolean debug;

    @Data
    @AllArgsConstructor
    class Deck {
        private String player;
        private Queue<Integer> cards;
    }

    class DeckCollector extends GroupCollector<Deck> {

        public DeckCollector(String file) throws IOException {
            super(file);
        }

        @Override
        protected Deck processGroup(List<String> groupLines) {
            String playes = groupLines.get(0);
            List<Integer> cards = groupLines.subList(1, groupLines.size())
                    .stream()
                    .map(s -> Integer.valueOf(s))
                    .collect(Collectors.toList());
            return new Deck(playes, new LinkedList<>(cards));
        }
    }

    public long solve(String file, int part) throws IOException {
        List<Deck> decks = new DeckCollector(file).processGroups();
        while (!decks.get(0).getCards().isEmpty() && !decks.get(1).getCards().isEmpty()) {
            Integer card1 = decks.get(0).getCards().poll();
            Integer card2 = decks.get(1).getCards().poll();
            if (part == 1) {
                if (card1.compareTo(card2) < 0) {
                    decks.get(1).getCards().add(card2);
                    decks.get(1).getCards().add(card1);
                } else {
                    decks.get(0).getCards().add(card1);
                    decks.get(0).getCards().add(card2);
                }
            } else {

            }
        }

        long sum = 0;
        for (Deck deck : decks) {
            long size = deck.getCards().size();
            for (int i = 0; i < size; i++) {
                sum += (size - i)*deck.getCards().poll();
            }
        }
        return sum;
    }

    public static void main(String[] args) throws IOException {
        long count;
        //*
        count = new Day22(true).solve("day22_test.txt", 1);
        System.out.println("Result: " + count);
        assert count == 306;

        count = new Day22(true).solve("day22.txt", 1);
        System.out.println("Result: " + count);
        assert count == 33559;

        count = new Day22(true).solve("day22_test.txt", 2);
        System.out.println("Result: " + count);
        assert count == 291;

        count = new Day22(true).solve("day22.txt", 2);
        System.out.println("Result: " + count);
        //assert count == 33559;

        /*/

        count = new Day13(true).solve2("2020/day12_test.txt");
        System.out.println("Result: " + count);
        assert count == 25;

        count = new Day13(true).solve2("2020/day12.txt");
        System.out.println("Result: " + count);
        //assert count == 27016;

         */
        //*/
    }
}
