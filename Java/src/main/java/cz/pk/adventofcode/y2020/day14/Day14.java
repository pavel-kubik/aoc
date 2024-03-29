package cz.pk.adventofcode.y2020.day14;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.Vector2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static cz.pk.adventofcode.util.BinaryUtil.bin2Dec;
import static cz.pk.adventofcode.util.BinaryUtil.dec2Bin;
import static java.lang.String.format;

@Data
public class Day14 {

    private final boolean debug;

    private Map<Long, List<Integer>> memory;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    class Instruction {
        Type type;
        long addr;
        long number;
        List<Integer> mask;

        public Instruction(Type type, List<Integer> mask) {
            this.type = type;
            this.mask = mask;
        }

        public Instruction(Type type, int addr, long number) {
            this.type = type;
            this.addr = addr;
            this.number = number;
        }
    }

    enum Type {
        MASK("mask"),
        MEM("mem"),
        ;

        private static final Map<String, Type> values;

        static {
            values = new HashMap<>();
            Arrays.stream(values()).forEach(p -> values.put(p.value, p));
        }

        private final String value;

        Type(String place) {
            this.value = place;
        }

        public static Type get(String value) {
            return values.get(value);
        }

        public String toString() {
            return value;
        }
    }

    class TypeCollector extends DataCollector<Instruction> {

        public TypeCollector(String file) {
            super(file);
        }

        @Override
        protected Instruction processLine(String line) {
            if (line.startsWith("mask")) {
                String[] parts = line.split(" ");
                assert parts.length == 3;
                List<Integer> mask = parts[2].chars()
                        .mapToObj(c -> (char) c)
                        .map(c -> c == 'X' ? -1 : Integer.valueOf(c.toString()))
                        .toList();
                return new Instruction(Type.MASK, mask);
            } else if (line.startsWith("mem")) {
                String[] parts = line.split(" ");
                assert parts.length == 3;
                return new Instruction(
                        Type.MEM,
                        Integer.valueOf(parts[0].substring(4, parts[0].length() - 1)),
                        Integer.valueOf(parts[2]));
            } else {
                throw new RuntimeException("Unknown instruction");
            }
        }
    }

    private void addNumberToMemory(long mem, List<Integer> value) {
        assert value.size() == 36;
        memory.put(mem, value);
        System.out.println(format("Add [%d] = %d", mem, bin2Dec(value)));
    }

    private List<Integer> applyMask(List<Integer> value, List<Integer> mask) {
        assert value.size() == 36;
        assert mask.size() == 36;
        Integer[] init = new Integer[36];
        Arrays.fill(init, 0);
        List<Integer> out = Arrays.asList(init);
        for (int i = 0; i < value.size(); i++) {
            if (mask.get(i) == -1) {
                out.set(i, value.get(i));
            } else {
                out.set(i, mask.get(i));
            }
        }
        return out;
    }

    private List<Integer> applyMask2(List<Integer> value, List<Integer> mask) {
        assert value.size() == 36;
        assert mask.size() == 36;
        Integer[] init = new Integer[36];
        Arrays.fill(init, 0);
        List<Integer> out = Arrays.asList(init);
        for (int i = 0; i < value.size(); i++) {
            if (mask.get(i) == 0) {
                out.set(i, value.get(i));
            } else if (mask.get(i) == 1) {
                out.set(i, 1);
            }
        }
        return out;
    }

    private int firstFluid(List<Integer> ints) {
        for (int i = 0; i < ints.size(); i++) {
            if (ints.get(i) == -1) {
                return i;
            }
        }
        return -1;
    }

    private List<Vector2<Long>> applyMaskToMemory(long addr, List<Integer> mask) {
        assert mask.size() == 36;
        int fluidIdx = firstFluid(mask);
        if (fluidIdx == -1) {
            return Arrays.asList(new Vector2<Long>(bin2Dec(applyMask2(dec2Bin(addr), mask)), bin2Dec(mask)));
        } else {
            List<Vector2<Long>> out = new ArrayList<>();

            List<Integer> addr0 = setBit(fluidIdx, 0, dec2Bin(addr));
            List<Integer> addr1 = setBit(fluidIdx, 1, dec2Bin(addr));

            List<Integer> newMask = setBit(fluidIdx, 0, mask);
            out.addAll(applyMaskToMemory(bin2Dec(addr0), newMask));
            out.addAll(applyMaskToMemory(bin2Dec(addr1), newMask));
            return out;
        }
    }

    private List<Integer> setBit(int idx, int value, List<Integer> i) {
        List<Integer> out = new ArrayList<>(i);
        out.set(idx, value);
        return out;
    }

    public long solve(String file) {
        Instruction[] instructions = new TypeCollector(file).process().toArray(new Instruction[1]);
        memory = new HashMap<>();
        List<Integer> mask = null;
        for (int i = 0; i < instructions.length; i++) {
            if (instructions[i].type == Type.MASK) {
                mask = instructions[i].mask;
            } else {
                List<Integer> number = dec2Bin(instructions[i].number);
                addNumberToMemory(instructions[i].addr, applyMask(number, mask));
            }
        }
        long sum = 0;
        for (Long mem : memory.keySet()) {
            sum += bin2Dec(memory.get(mem));
        }
        System.out.println(Arrays.asList(instructions));
        return sum;
    }

    public long solve2(String file) {
        Instruction[] instructions = new TypeCollector(file).process().toArray(new Instruction[1]);
        memory = new HashMap<>();
        List<Integer> mask = null;
        for (int i = 0; i < instructions.length; i++) {
            if (instructions[i].type == Type.MASK) {
                mask = instructions[i].mask;
            } else {
                List<Integer> number = dec2Bin(instructions[i].number);
                List<Vector2<Long>> addresses = applyMaskToMemory(instructions[i].addr, mask);
                for (Vector2<Long> addrAndMask : addresses) {
                    Long addr = addrAndMask.x;
                    Long maskDec = addrAndMask.y;
                    assert addr >= 0;
                    addNumberToMemory(addr, number);
                }
            }
        }
        long sum = 0;
        for (Long mem : memory.keySet()) {
            sum += bin2Dec(memory.get(mem));
        }
        System.out.println(Arrays.asList(instructions));
        return sum;
    }

    public void test() {
        Integer[] init = new Integer[36];
        Arrays.fill(init, 1);
        long num = bin2Dec(Arrays.asList(init));
        List<Integer> out = dec2Bin(num);
    }

    public static void main(String[] args) {
        System.out.println(Day14.class);
        new Day14(true).test();

        long count;
        /*
        count = new Day14(true).solve("day14_test.txt");    //TOOD fix reading at windows
        System.out.println("Result: " + count);
        assert count == 165;

        count = new Day14(true).solve("day14.txt");
        System.out.println("Result: " + count);
        assert count == 4297467072083l;
        // not 41154481747

        /*/

        count = new Day14(true).solve2("2020/day14_test2.txt");
        System.out.println("Result: " + count);
        assert count == 208;

        count = new Day14(true).solve2("2020/day14.txt");
        System.out.println("Result: " + count);
        //assert count == 27016;
        // not 187230630710742 (should be lower)

        //*/
    }
}
