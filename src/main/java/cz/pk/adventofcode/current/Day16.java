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

        public abstract Long count();
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

        @Override
        public Long count() {
            System.out.print(literalValue + " ");
            return literalValue;
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

        @Override
        public Long count() {
            Long out = 0l;
            switch (getType()) {
                case 0:
                    // sum
                    System.out.println("SUM");
                    for (Packet packet : getSubpackets()) {
                        out += packet.count();
                    }
                    break;
                case 1:
                    // product
                    System.out.println("MUL");
                    out = 1l;
                    for (Packet packet : getSubpackets()) {
                        out *= packet.count();
                    }
                    break;
                case 2:
                    // min
                    System.out.println("MIN");
                    out = Long.MAX_VALUE;
                    for (Packet packet : getSubpackets()) {
                        if (out > packet.count()) {
                            out = packet.count();
                        }
                    }
                    break;
                case 3:
                    // max
                    System.out.println("MAX");
                    out = Long.MIN_VALUE;
                    for (Packet packet : getSubpackets()) {
                        if (out < packet.count()) {
                            out = packet.count();
                        }
                    }
                    break;
                case 5:
                    System.out.println(">");
                    // greater than
                    assert getSubpackets().size() == 2;
                    out = getSubpackets().get(0).count().compareTo(getSubpackets().get(1).count()) > 0 ? 1l : 0l;
                    break;
                case 6:
                    System.out.println("<");
                    // less than
                    assert getSubpackets().size() == 2;
                    out = getSubpackets().get(0).count().compareTo(getSubpackets().get(1).count()) < 0 ? 1l : 0l;
                    break;
                case 7:
                    System.out.println("==");
                    // less than
                    assert getSubpackets().size() == 2;
                    out = getSubpackets().get(0).count().compareTo(getSubpackets().get(1).count()) == 0 ? 1l : 0l;
                    break;
                default:
                    throw new RuntimeException("Fuck");
            }
            return out;
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

        return packetList.get(0).count();
    }

    public static void main(String[] args) {
        System.out.println(Day16.class);
        long count;
        //*
        count = new Day16(true).solve("day16_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 6;

        count = new Day16(true).solve("day16_test2.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 9;

        count = new Day16(true).solve("day16_test3.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 14;

        count = new Day16(true).solve("day16_test4.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 16;

        count = new Day16(true).solve("day16_test5.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 12;

        count = new Day16(true).solve("day16_test6.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 23;

        count = new Day16(true).solve("day16_test7.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 31;

        count = new Day16(true).solve("day16.txt");
        System.out.println("Result: " + count);
        assert count == 927;

        //*/

        count = new Day16(true).solve2("day16_test8.txt");
        System.out.println("Result: " + count);
        assert count == 3;

        count = new Day16(true).solve2("day16_test9.txt");
        System.out.println("Result: " + count);
        assert count == 1;

        count = new Day16(true).solve2("day16_test10.txt");
        System.out.println("Result: " + count);
        assert count == 0;

        count = new Day16(true).solve2("day16_test11.txt");
        System.out.println("Result: " + count);
        assert count == 0;

        count = new Day16(true).solve2("day16_test12.txt");
        System.out.println("Result: " + count);
        assert count == 1;

        count = new Day16(true).solve2("day16_test13.txt");
        System.out.println("Result: " + count);
        assert count == 9;

        count = new Day16(true).solve2("day16_test14.txt");
        System.out.println("Result: " + count);
        assert count == 7;

        count = new Day16(true).solve2("day16_test15.txt");
        System.out.println("Result: " + count);
        assert count == 54;

        count = new Day16(true).solve2("day16.txt");
        System.out.println("Result: " + count);
        assert count == 1725277876501l; // -9223370315264430709
        //*/
    }
}
