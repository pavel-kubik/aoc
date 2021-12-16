package cz.pk.adventofcode.current;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.StringCollector;
import lombok.*;

import java.util.*;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day16 {

    private final boolean debug;

    @Getter
    @RequiredArgsConstructor
    @ToString
    private static abstract class Packet {
        private final int version;
        private final int type;
        private List<Packet> subpackets = new ArrayList<>();
    }

    @Getter
    @Setter
    @ToString(callSuper = true)
    private static class LiteralPacket extends Packet {
        private Long literalValue;

        public LiteralPacket(int version, int type, long literalValue) {
            super(version, type);
            this.literalValue = literalValue;
        }
    }

    @Getter
    @Setter
    @ToString(callSuper = true)
    private static class OperatorPacket extends Packet {
        int lengthTypeId;

        public OperatorPacket(int version, int type, int lengthTypeId) {
            super(version, type);
            this.lengthTypeId = lengthTypeId;
        }
    }

    private String parsePacket(String inputBin, List<Packet> packetList) {
        int version = Integer.parseInt(inputBin.substring(0, 3), 2);
        int packetType = Integer.parseInt(inputBin.substring(3, 6), 2);
        Packet currentPacket;
        String remainingBits = "";
        if (packetType == 4) {
            // literal package - Nx 5 bits
            StringBuilder literalValueBin = new StringBuilder();
            int pointer = 6;
            while (inputBin.substring(pointer, pointer + 1).equals("1")) {
                literalValueBin.append(inputBin.substring(pointer+1, pointer+5));
                pointer += 5;
            }
            literalValueBin.append(inputBin.substring(pointer+1, pointer+5));
            pointer += 5;
            currentPacket = new LiteralPacket(version,
                    packetType,
                    Long.parseLong(literalValueBin.toString(), 2));
            remainingBits = inputBin.substring(pointer);
        } else {
            //operator package
            int lengthTypeId = Integer.parseInt(inputBin.substring(6, 7), 2);
            currentPacket = new OperatorPacket(version, packetType, lengthTypeId);
            if (lengthTypeId == 0) {
                // 15bits = number of total bit length of sub-packets
                int length = Integer.parseInt(inputBin.substring(7, 22), 2);
                String restOfBits = inputBin.substring(22, 22 + length);
                while (!restOfBits.isEmpty() && !isZero(restOfBits)) {
                    restOfBits = parsePacket(restOfBits, currentPacket.getSubpackets());
                }
                remainingBits = inputBin.substring(22 + length);
            } else {
                // 11bits = number of sub-packets
                int numberOfSubPackets = Integer.parseInt(inputBin.substring(7, 18), 2);
                remainingBits = inputBin.substring(18);
                for (int i = 0; i < numberOfSubPackets; i++) {
                    remainingBits = parsePacket(remainingBits, currentPacket.getSubpackets());
                }
            }
        }
        packetList.add(currentPacket);
        return remainingBits;
    }

    private boolean isZero(String bin) {
        for (int i = 0; i < bin.length(); i++) {
            if (!bin.substring(i, i + 1).equals("0")) {
                return false;
            }
        }
        return true;
    }

    public long solve(String file) {
        String inputHex = new StringCollector(file).process().get(0);
        System.out.println(inputHex);

        StringBuilder inputBin = new StringBuilder();
        // transform Hex to Bin
        for (int i = 0; i < inputHex.length(); i++) {
            Integer decimalDigit = Integer.parseInt(inputHex.substring(i, i+1), 16);
            inputBin.append(String.format("%4s", Integer.toString(decimalDigit, 2)).replace(' ', '0'));
        }
        System.out.println(inputBin.toString());

        List<Packet> packetList = new ArrayList<>();
        String remainingBits = parsePacket(inputBin.toString(), packetList);
        System.out.println("Remaining bits: " + remainingBits);
        System.out.println(packetList.get(0));

        long packetTypeSum = 0;
        Queue<Packet> packets = new LinkedList<>();
        packets.add(packetList.get(0));
        while (!packets.isEmpty()) {
            Packet current = packets.poll();
            packetTypeSum += current.getVersion();
            packets.addAll(current.getSubpackets());
        }

        return packetTypeSum;
    }

    public long solve2(String file) {
        List<Integer> data = stream(new StringCollector(file)
                .process().get(0).split(",")).map(Integer::valueOf).collect(toList());
        System.out.println(data);
        return data.get(0)+22;
    }

    public static void main(String[] args) {
        System.out.println(Day16.class);
        long count;
        //*
//        count = new Day16(true).solve("day16_test.txt");
//        System.out.println("Result: " + count);
//        // add vm option -ea to run configuration to throw exception on assert
//        assert count == 6;
//
//        count = new Day16(true).solve("day16_test2.txt");
//        System.out.println("Result: " + count);
//        // add vm option -ea to run configuration to throw exception on assert
//        assert count == 9;
//
//        count = new Day16(true).solve("day16_test3.txt");
//        System.out.println("Result: " + count);
//        // add vm option -ea to run configuration to throw exception on assert
//        assert count == 14;
//
//        count = new Day16(true).solve("day16_test4.txt");
//        System.out.println("Result: " + count);
//        // add vm option -ea to run configuration to throw exception on assert
//        assert count == 16;
//
//        count = new Day16(true).solve("day16_test5.txt");
//        System.out.println("Result: " + count);
//        // add vm option -ea to run configuration to throw exception on assert
//        assert count == 12;
//
//        count = new Day16(true).solve("day16_test6.txt");
//        System.out.println("Result: " + count);
//        // add vm option -ea to run configuration to throw exception on assert
//        assert count == 23;
//
//        count = new Day16(true).solve("day16_test7.txt");
//        System.out.println("Result: " + count);
//        // add vm option -ea to run configuration to throw exception on assert
//        assert count == 31;

        count = new Day16(true).solve("day16.txt");
        System.out.println("Result: " + count);
        assert count == 22;

        //*/

        count = new Day16(true).solve2("day16_test.txt");
        System.out.println("Result: " + count);
        assert count == 33;

        count = new Day16(true).solve2("day16.txt");
        System.out.println("Result: " + count);
        assert count == 44;
        //*/
    }
}
