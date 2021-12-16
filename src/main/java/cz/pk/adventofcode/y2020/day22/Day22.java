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


    Map<String, Boolean> playedGames = new HashMap<>();

    private boolean determineOneGame(Deck deck1, Deck deck2, int gameNumber) {
        String gameKey = deck1.getCards().toString() + "|" + deck2.getCards().toString();
        if (playedGames.containsKey(gameKey)) {
            return playedGames.get(gameKey);
        }
        Queue<Integer> cards1 = new LinkedList<>(deck1.getCards());
        Queue<Integer> cards2 = new LinkedList<>(deck2.getCards());
        int round = 1;
        Set<String> playedRounds = new HashSet<>();
        while (!cards1.isEmpty() && !cards2.isEmpty()) {

            String deckKey = cards1.toString() + "|" + cards2.toString();
            if (playedRounds.contains(deckKey)) {
                return true;
            }

            playedRounds.add(deckKey);            System.out.println("-- Round " + round + " (Game " + gameNumber +") --");
            System.out.println("Player 1's dec: " + cards1);
            System.out.println("Player 2's dec: " + cards2);
            Integer card1 = cards1.poll();
            Integer card2 = cards2.poll();
            if (deck1.getCards().size() >= card1 && deck2.getCards().size() >= card2) {
                // recursive combat
                System.out.println("\n=== Game " + gameNumber + " ===");
                if (determineOneGame(new Deck("Player 1:", cards1), new Deck("Player 2:", cards2), gameNumber + 1)) {
                    System.out.println("Player 1 wins round " + round + ".\n");
                    cards1.add(card1);
                    cards1.add(card2);
                } else {
                    System.out.println("Player 2 wins round " + round + ".\n");
                    cards2.add(card2);
                    cards2.add(card1);
                }
            } else {
                if (card1.compareTo(card2) < 0) {
                    System.out.println("Player 2 wins round " + round + ".\n");
                    cards2.add(card2);
                    cards2.add(card1);
                } else {
                    System.out.println("Player 1 wins round " + round + ".\n");
                    cards1.add(card1);
                    cards1.add(card2);
                }
            }
            round++;
        }
        playedGames.put(gameKey, cards2.isEmpty());
        return cards2.isEmpty();
    }

    private Deck playRecursiveGame(Deck deck1, Deck deck2) {
        int game = 1;
        int round = 1;
        while (!deck1.getCards().isEmpty() && !deck2.getCards().isEmpty()) {
            System.out.println("-- Round " + round + " (Game 1) --");
            System.out.println("Player 1's dec: " + deck1.getCards());
            System.out.println("Player 2's dec: " + deck2.getCards());
            Integer card1 = deck1.getCards().poll();
            Integer card2 = deck2.getCards().poll();
            if (deck1.getCards().size() >= card1 && deck2.getCards().size() >= card2) {
                // recursive combat
                game++;
                System.out.println("\n=== Game " + game + " ===");
                if (determineOneGame(deck1, deck2, game)) {
                    System.out.println("Player 1 wins round " + round + ".\n");
                    deck1.getCards().add(card1);
                    deck1.getCards().add(card2);
                } else {
                    System.out.println("Player 2 wins round " + round + ".\n");
                    deck2.getCards().add(card2);
                    deck2.getCards().add(card1);
                }
            } else {
                if (card1.compareTo(card2) < 0) {
                    System.out.println("Player 2 wins round " + round + ".\n");
                    deck2.getCards().add(card2);
                    deck2.getCards().add(card1);
                } else {
                    System.out.println("Player 1 wins round " + round + ".\n");
                    deck1.getCards().add(card1);
                    deck1.getCards().add(card2);
                }
            }
            round++;
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

//        count = new Day22(true).solve2("2020/day22.txt");
//        System.out.println("Result: " + count);
//        assert count == 33559;  //32516
        //*/
    }
}
