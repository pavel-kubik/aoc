package cz.pk.adventofcode.y2021;

import cz.pk.adventofcode.util.Vector2;
import cz.pk.adventofcode.y2021.day23.Day23.Amphipod;
import cz.pk.adventofcode.y2021.day23.Day23.Game;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Day23Test {

    @Test
    void testGameEnd() {
        assert new Game(List.of(
                "#############",
                "#...........#",
                "###B#C#B#D###",
                "###A#D#C#A###",
                "#############"), true)
                .isFinalState() == false;

        assert new Game(List.of(
                "#############",
                "#...........#",
                "###A#B#C#D###",
                "###A#B#C#D###",
                "#############"), true)
                .isFinalState() == true;
    }

    @Test
    void testGame16End() {
        assert new Game(List.of(
                "#############",
                "#...........#",
                "###B#C#B#D###",
                "###A#D#C#A###",
                "###B#C#B#D###",
                "###A#D#C#A###",
                "#############"), true)
                .isFinalState() == false;

        assert new Game(List.of(
                "#############",
                "#...........#",
                "###A#B#C#D###",
                "###A#B#C#D###",
                "###A#B#C#D###",
                "###A#B#C#D###",
                "#############"), true)
                .isFinalState() == true;
    }

    @Test
    void testFreeWay() {
        assert new Game(List.of(
                "#############",
                "#...........#",
                "###B#C#B#D###",
                "###A#D#C#A###",
                "#############"), true)
                .isFreeWay(new Vector2<>(3, 2), new Vector2<>(2, 1)) == true;
    }

    @Test
    void testFreeWay2() {
        assert new Game(List.of(
                "#############",
                "#...........#",
                "###.#B#C#D###",
                "###A#B#C#D###",
                "#############"), true)
                .isFreeWay(new Vector2<>(3, 3), new Vector2<>(8,1)) == true;
    }

    @Test
    void testNotFreeWay() {
        assert new Game(List.of(
                "#############",
                "#...........#",
                "###A#B#C#D###",
                "###A#B#C#D###",
                "#############"), true)
                .isFreeWay(new Vector2<>(3, 3), new Vector2<>(2,1)) == false;
    }

    @Test
    void testNotFreeWay2() {
        assert new Game(List.of(
                "#############",
                "#.....C.....#",
                "###.#B#.#D###",
                "###A#B#C#D###",
                "#############"), true)
                .isFreeWay(new Vector2<>(3, 3), new Vector2<>(8,1)) == false;
    }

    @Test
    void backToHoleAvailableMove() {
        assert new Game(List.of(
                "#############",
                "#...C.B.D.A.#",
                "###.#.#B#.###",
                "###A#D#C#.###",
                "#############"), true)
                .getValidMoves(new Amphipod('D', 8, 1))
                .contains(new Vector2<>(9, 3));
    }

    @Test
    void cantMoveToWrongHome() {
        List<Vector2<Integer>> validMoves = new Game(List.of(
                "#############",
                "#...C.B.D.A.#",
                "###.#.#B#.###",
                "###A#D#C#.###",
                "#############"), true)
                .getValidMoves(new Amphipod('A', 10, 1));
        assert validMoves.isEmpty();
    }

    @Test
    void backToHole() {
        assert new Game(List.of(
                "#############",
                "#...C.B.D.A.#",
                "###.#.#B#.###",
                "###A#D#C#.###",
                "#############"), true)
                .move(new Amphipod('D', 8, 1), new Vector2<>(9, 3)) == 3000;
    }

    @Test
    void dontMoveOutFromHome() {
        assert new Game(List.of(
               "#############",
               "#BC.B.D.....#",
               "###.#.#.#.###",
               "###A#D#C#A###",
               "#############"
        ), true).getValidMoves(new Amphipod('C', 7, 3)).isEmpty();
    }

    @Test
    void test() {
        List<Vector2<Integer>> validMoves = new Game(List.of(
                "#############",
                "#.B.D.....AD#",
                "###.#.#C#.###",
                "###A#B#C#.###",
                "#############"
        ), true).getValidMoves(new Amphipod('D', 4, 1));
        assert validMoves.contains(new Vector2<>(9, 3));

        assert new Game(List.of(
                "#############",
                "#.B.D.....AD#",
                "###.#.#C#.###",
                "###A#B#C#.###",
                "#############"
        ), true).move(
                new Amphipod('D', 4, 1),
                new Vector2<>(9, 3)) == 7000;
    }
}
