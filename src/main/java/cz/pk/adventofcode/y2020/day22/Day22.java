package cz.pk.adventofcode.y2020.day22;

import java.io.IOException;
import java.util.*;
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

        public DeckCollector(String file) {
            super(file);
        }

        @Override
        protected Deck processGroup(List<String> groupLines) {
            String playes = groupLines.get(0);
            List<Integer> cards = groupLines.subList(1, groupLines.size())
                    .stream()
                    .map(s -> Integer.valueOf(s))
                    .toList();
            return new Deck(playes, new LinkedList<>(cards));
        }
    }

    private Deck playOneGame(Deck deck1, Deck deck2) {
        while (!deck1.getCards().isEmpty() && !deck2.getCards().isEmpty()) {
            Integer card1 = deck1.getCards().poll();
            Integer card2 = deck2.getCards().poll();
            if (card1.compareTo(card2) < 0) {
                deck2.getCards().add(card2);
                deck2.getCards().add(card1);
            } else {
                deck1.getCards().add(card1);
                deck1.getCards().add(card2);
            }
        }
        if (deck1.getCards().isEmpty()) {
            return deck2;
        } else {
            return deck1;
        }
    }

    private long countDeckValue(Deck deck) {
        long sum = 0;
        long size = deck.getCards().size();
        for (int i = 0; i < size; i++) {
            sum += (size - i)*deck.getCards().poll();
        }
        return sum;
    }

    public long solve(String file)  {
        List<Deck> decks = new DeckCollector(file).processGroups();
        Deck winningDeck = playOneGame(decks.get(0), decks.get(1));
        return countDeckValue(winningDeck);
    }

    private boolean determineOneGame(Deck deck1, Deck deck2) {
        Queue<Integer> cards1 = new LinkedList<>(deck1.getCards());
        Queue<Integer> cards2 = new LinkedList<>(deck2.getCards());
        while (!cards1.isEmpty() && !cards2.isEmpty()) {
            Integer card1 = cards1.poll();
            Integer card2 = cards2.poll();
            if (card1.compareTo(card2) < 0) {
                cards2.add(card2);
                cards2.add(card1);
            } else {
                cards1.add(card1);
                cards1.add(card2);
            }
        }
        return cards2.isEmpty();
    }

    private Deck playRecursiveGame(Deck deck1, Deck deck2) {
        while (!deck1.getCards().isEmpty() && !deck2.getCards().isEmpty()) {
            Integer card1 = deck1.getCards().poll();
            Integer card2 = deck2.getCards().poll();
            if (deck1.getCards().size() >= card1 && deck2.getCards().size() >= card2) {
                // recursive combat
                if (determineOneGame(deck1, deck2)) {
                    deck1.getCards().add(card1);
                    deck1.getCards().add(card2);
                } else {
                    deck2.getCards().add(card2);
                    deck2.getCards().add(card1);
                }
            } else {
                if (card1.compareTo(card2) < 0) {
                    deck2.getCards().add(card2);
                    deck2.getCards().add(card1);
                } else {
                    deck1.getCards().add(card1);
                    deck1.getCards().add(card2);
                }
            }
        }
        if (deck1.getCards().isEmpty()) {
            return deck2;
        } else {
            return deck1;
        }
    }

    public long solve2(String file)  {
        List<Deck> decks = new DeckCollector(file).processGroups();
        Deck winningDeck = playRecursiveGame(decks.get(0), decks.get(1));
        return countDeckValue(winningDeck);
    }

    public static void main(String[] args) throws IOException {
        System.out.println(Day22.class);
        long count;
        //*
        count = new Day22(true).solve("2020/day22_test.txt");
        System.out.println("Result: " + count);
        assert count == 306;

        count = new Day22(true).solve("2020/day22.txt");
        System.out.println("Result: " + count);
        assert count == 33559;

        //*/

        count = new Day22(true).solve2("2020/day22_test.txt");
        System.out.println("Result: " + count);
        assert count == 291;  //TODO finish part 2 :)

        count = new Day22(true).solve2("2020/day22.txt");
        System.out.println("Result: " + count);
        assert count == 33559;  //32516
        //*/
    }
}
