package cz.pk.adventofcode.util;

import cz.pk.adventofcode.util.HexagonUtil.Direction;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static cz.pk.adventofcode.util.HexagonUtil.Direction.*;

public class HexagonUtilTest {

    @Test
    void shortTripBack() {
        List<Direction> directions = Arrays.asList(new Direction[]{
                NORTHWEST,
                WEST,
                SOUTHWEST,
                EAST,
                EAST,
        });
        Pair<Integer> finalStop = HexagonUtil.walkOnHexagon(directions);
        System.out.println("Final: " + finalStop);
        assert finalStop.first == 0;
        assert finalStop.second == 0;
    }

    @Test
    void shorterTripBack() {
        List<Direction> directions = Arrays.asList(new Direction[]{
                NORTHEAST,
                SOUTHEAST,
                WEST,
        });
        Pair<Integer> finalStop = HexagonUtil.walkOnHexagon(directions);
        System.out.println("Final: " + finalStop);
        assert finalStop.first == 0;
        assert finalStop.second == 0;
    }

    @Test
    void circle() {
        List<Direction> directions = Arrays.asList(new Direction[]{
                NORTHEAST,
                NORTHEAST,
                EAST,
                EAST,
                SOUTHEAST,
                SOUTHEAST,
                SOUTHWEST,
                SOUTHWEST,
                WEST,
                WEST,
                NORTHWEST,
                NORTHWEST,
        });
        Pair<Integer> finalStop = HexagonUtil.walkOnHexagon(directions);
        System.out.println("Final: " + finalStop);
        assert finalStop.first == 0;
        assert finalStop.second == 0;
    }
}
