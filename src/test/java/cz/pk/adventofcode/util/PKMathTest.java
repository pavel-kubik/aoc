package cz.pk.adventofcode.util;

import org.junit.jupiter.api.Test;

import java.util.List;

public class PKMathTest {

    @Test
    public void minimum() {
        assert PKMath.min(0, -1, 5) == -1;
        assert PKMath.min(9, 8, 7, 6, 5) == 5;
        assert PKMath.min(-1, -3, -7) == -7;
    }

    @Test
    public void minimumArray() {
        assert PKMath.min(List.of(0, -1, 5)) == -1;
        assert PKMath.min(List.of(9, 8, 7, 6, 5)) == 5;
        assert PKMath.min(List.of(-1, -3, -7)) == -7;
    }

    @Test
    public void maximum() {
        assert PKMath.max(0, -1, 5) == 5;
        assert PKMath.max(9, 8, 7, 6, 5) == 9;
        assert PKMath.max(-1, -3, -7) == -1;
    }

    @Test
    public void maximumArray() {
        assert PKMath.max(List.of(0, -1, 5)) == 5;
        assert PKMath.max(List.of(9, 8, 7, 6, 5)) == 9;
        assert PKMath.max(List.of(-1, -3, -7)) == -1;
    }

}
