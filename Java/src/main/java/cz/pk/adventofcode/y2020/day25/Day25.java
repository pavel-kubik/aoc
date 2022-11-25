package cz.pk.adventofcode.y2020.day25;

import cz.pk.adventofcode.util.DataCollector;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cz.pk.adventofcode.util.DataCollectorFactory.collectData;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day25 {

    private final boolean debug;

    private static final Long base = 20201227l;

    private long cryptoStep(long value, long subjectNumber) {
        value *= subjectNumber;
        return value % base;
    }

    private long transform(long value, long subjectNumber, int iter) {
        for (int i = 0; i < iter; i++) {
            value = cryptoStep(value, subjectNumber);
        }
        return value;
    }

    public long solve(long carPK, long doorPK) {
        int MAX_LOOP_SIZE = 1000000;
        long subjectNumber = 7l;
        long initialValue = 1l;

        int doorLoopSize = 0;
        int cardLoopSize = 0;

        // find loop size
        System.out.println("Searching card loop size...");
        for (int i = 0; i <= MAX_LOOP_SIZE; i++) {
            long iterValue = transform(initialValue, subjectNumber, i);
            if (i % (MAX_LOOP_SIZE/10) == 0) {
                System.out.println(format("  ..%d: %d", i, iterValue));
            }
            if (carPK == iterValue) {
                cardLoopSize = i;
                System.out.println("Card loop size: " + cardLoopSize);
            };
            if (doorPK == iterValue) {
                doorLoopSize = i;
                System.out.println("Door loop size: " + doorLoopSize);
                //75188
            };
            if (cardLoopSize + doorLoopSize > 0) {
                break;
            }
        }
        System.out.println("Card loop size: " + cardLoopSize);
        System.out.println("Door loop size: " + doorLoopSize);

        long encryptionKeyFromDoorPK = 0;
        if (cardLoopSize > 0) {
            encryptionKeyFromDoorPK = transform(initialValue, doorPK, cardLoopSize);
            System.out.println("Encryption Key from DoorPK: " + encryptionKeyFromDoorPK);
        }

        long encryptionKeyFromCardPK = 0;
        if (doorLoopSize > 0) {
            encryptionKeyFromCardPK = transform(initialValue, carPK, doorLoopSize);
            System.out.println("Encryption Key from CardPK: " + encryptionKeyFromCardPK);
        }

        return encryptionKeyFromCardPK + encryptionKeyFromDoorPK;
    }

    public long solve2(String file) {

        return 0;
    }

    public static void main(String[] args) {
        System.out.println(Day25.class);
        long count;
        //*
        count = new Day25(true).solve(5764801l, 17807724l);
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 14897079;

        count = new Day25(true).solve(12320657l, 9659666l);
        System.out.println("Result: " + count);
        assert count == 6421487;    //>4883439

        /*/

        count = new Day25(true).solve2("day_test.txt");
        System.out.println("Result: " + count);
        assert count == 333;

        count = new Day25(true).solve2("day.txt");
        System.out.println("Result: " + count);
        assert count == 444;
        //*/
    }
}
