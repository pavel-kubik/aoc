package cz.pk.adventofcode.util;

import java.util.List;
import java.util.stream.Collectors;

public class BinaryUtil {

    public static long bin2Dec(List<Integer> bits) {
        assert bits.size() == 36;
        long number = 0;
        long base = 1;
        for (int i = bits.size() - 1; i >= 0; i--) {
            number += base * bits.get(i);
            base *= 2;
        }
        assert dec2Bin(number).equals(bits);
        return number;
    }

    public static List<Integer> dec2Bin(long number) {
        List<Integer> bin;
        bin = Long.toBinaryString(number).chars()
                .mapToObj(c -> (char) c)
                .map(c -> c == '1' ? 1 : 0)
                .collect(Collectors.toList());
        // add leading zeros
        for (int i = bin.size(); i < 36; i++) {
            bin.add(0, 0);
        }
        //Collections.reverse(bin);
        //assert bin2Dec(bin) == number;
        if (bin.size() != 36) {
            assert bin.size() == 36;
        }
        return bin;
    }
}
