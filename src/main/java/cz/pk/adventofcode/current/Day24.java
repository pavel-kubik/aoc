package cz.pk.adventofcode.current;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.StringCollector;
import cz.pk.adventofcode.util.Vector2;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

import static cz.pk.adventofcode.current.Day24.Operation.INP;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day24 {

    private final boolean debug;

    @Data
    @AllArgsConstructor
    class Instruction {
        private Operation operation;
        private String a;
        private String b;

        @Override
        public String toString() {
            return operation.toString() +
                    " " + a + " " + (b != null ? b : "");
        }
    }

    enum Operation {
        INP("inp"),
        ADD("add"),
        MUL("mul"),
        DIV("div"),
        MOD("mod"),
        EQL("eql"),
        ;

        private static final Map<String, Operation> values;

        static {
            values = new HashMap<>();
            Arrays.stream(values()).forEach(p -> values.put(p.value, p));
        }

        private final String value;

        Operation(String place) {
            this.value = place;
        }

        public static Operation get(String value) {
            return values.get(value);
        }

        public String toString() {
            return value;
        }
    }

    class InstructionCollector extends DataCollector<Instruction> {

        public InstructionCollector(String file) {
            super(file);
        }

        @Override
        protected Instruction processLine(String line) {
            String[] parts = line.split(" ");
            Operation operation = Operation.get(parts[0]);
            return new Instruction(operation, parts[1], parts.length == 3 ? parts[2] : null);
        }
    }

    private Map<String, Long> variables = new HashMap<>();
    private String input;
    private int inputIndex = 0;

    private long getNextInput() {
        return Integer.parseInt(Character.toString(input.charAt(inputIndex++)));
    }

    private long parseSecondParameter(String parameter) {
        try {
            return Integer.parseInt(parameter);
        } catch(NumberFormatException e) {
            return variables.get(parameter);
        }
    }

    private void processInstruction(Instruction instruction) {
        long a, b;
        switch (instruction.getOperation()) {
            case INP:
                variables.put(instruction.getA(), getNextInput());
                break;
            case ADD:
                a = variables.get(instruction.getA());
                b = parseSecondParameter(instruction.getB());
                variables.put(instruction.getA(), a + b);
                break;
            case MUL:
                a = variables.get(instruction.getA());
                b = parseSecondParameter(instruction.getB());
                variables.put(instruction.getA(), a * b);
                break;
            case DIV:
                a = variables.get(instruction.getA());
                b = parseSecondParameter(instruction.getB());
                variables.put(instruction.getA(), a / b);
                break;
            case MOD:
                a = variables.get(instruction.getA());
                b = parseSecondParameter(instruction.getB());
                variables.put(instruction.getA(), a % b);
                break;
            case EQL:
                a = variables.get(instruction.getA());
                b = parseSecondParameter(instruction.getB());
                variables.put(instruction.getA(), a == b ? 1L : 0L);
                break;
            default:
                throw new RuntimeException("Unknown instruction " + instruction);
        }
    }

    Map<Integer, List<Instruction>> programs = new HashMap<>();
    Map<Integer, Set<Long>> programOutputs = new HashMap<>();
    Map<String, Set<Vector2<Long>>> programWZOutputs = new HashMap<>();

    private long runProgram(int prgId, int inputNumber, long z) {
        variables.put("w", 0L);
        variables.put("x", 0L);
        variables.put("y", 0L);
        variables.put("z", z);
        inputIndex = 0;
        input = Integer.toString(inputNumber);
        for (Instruction instruction : programs.get(prgId)) {
            processInstruction(instruction);
        }
        return variables.get("z");
    }

    private void testProgramNative() {
        for (int prgId = 1; prgId <= 14; prgId++) {
            System.out.println("Test program " + prgId);
            for (int w = 1; w <= 9; w++) {
                //System.out.println("Input number " + w);
                for (long z = 0; z <= 26 * 100; z++) {
                    long outZ = runProgram(prgId, w, z);
                    long outNativeZ = prgNative(prgId, w, z);
                    //System.out.println(w + " " + z + " " + outZ + "          " + outNativeZ);
                    assert outZ == outNativeZ;
                }
            }
        }
        System.out.println("Test program OK");
    }

    public long solve(String file) {
        List<Instruction> instructions = new InstructionCollector(file).process();

        int programId = 0;
        for (Instruction instruction : instructions) {
            if (instruction.getOperation() == INP) {
                programId++;
            }
            programs.compute(programId, (k, v) -> {
                if (v == null) {
                    v = new ArrayList<>();
                }
                v.add(instruction);
                return v;
            });
        }

        programOutputs.put(0, Set.of(0L));

        //testProgramNative();

        for (int prgId = 1; prgId <= 14; prgId++) {
            System.out.println("Program " + prgId);
            for (long w = 1; w <= 9; w++) {
                for (Long z : programOutputs.get(prgId-1)) {
                    long outZ = prgNative(prgId, w, z);
                    programOutputs.compute(prgId, (k, v) -> {
                        if (v == null) {
                            v = new HashSet<>();
                        }
                        v.add(outZ);
                        return v;
                    });
                }
            }
            System.out.println(programOutputs.get(prgId-1).size());
        }

        assert programOutputs.get(14).contains(0L) == true;
        System.out.println("Phase II - program 14 out z=0 exists!");
        programOutputs.put(14, Set.of(0L));

        for (int prgId = 14; prgId >= 1; prgId--) {
            System.out.println("Program " + prgId);
            Set<Long> cutInputs = new HashSet<>();
            for (long w = 1; w <= 9; w++) {
                //System.out.println("Number " + w);
                for (Long z : programOutputs.get(prgId-1)) {
                    long outZ = prgNative(prgId, w, z);
                    if (programOutputs.get(prgId).contains(outZ)) {
                        //System.out.println("Prog " + prgId + " has inZ=" + z+ " outZ=" + outZ + " for w=" + w);
                        cutInputs.add(z);
                    }
                }
            }
            programOutputs.put(prgId-1, cutInputs);
            System.out.println(programOutputs.get(prgId-1).size());
        }

        System.out.println("Phase III");
        long out = 0;
        for (int prgId = 1; prgId <= 14; prgId++) {
            boolean found = false;
            for (long w = 9; w >= 1; w--) {
                for (Long z : programOutputs.get(prgId-1)) {
                    long outZ = prgNative(prgId, w, z);
                    if (programOutputs.get(prgId).contains(outZ)) {
                        out += w;
                        found = true;
                        System.out.println("Prg " + prgId + " w=" + w);
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }
            assert found == true;
            out *= 10;
            System.out.println(out);
        }

        return out;
    }

    public long prgNative(int prgId, long inputNumber, long z) {
        return switch (prgId) {
            case 1 -> 8 + inputNumber + z*26;   // zOut=w+8 (zOut=9..17)
            case 2 -> 16 + inputNumber + z*26;  // zIn=9..17 / zOut=16+w+zIn*26
            case 3 -> 4 + inputNumber + z*26;   // zIn=?..?  / zOut=4+w+zIn*26
            case 4 -> ((z - 11 - inputNumber) % 26) == 0 ? z/26 : 1 + inputNumber + z/26*26;
            case 5 -> 13 + inputNumber + z*26;
            case 6 -> 5 + inputNumber + z*26;
            case 7 -> inputNumber + z*26;
            case 8 -> ((z - 5 - inputNumber) % 26) == 0 ? z/26 : 10 + inputNumber + z/26*26;
            case 9 -> 7 + inputNumber + z*26;
            case 10 -> ((z - inputNumber) % 26) == 0 ? z/26 : 2 + inputNumber + z/26*26;
            case 11 -> ((z - 11 - inputNumber) % 26) == 0 ? z/26 : 13 + inputNumber + z/26*26;
            case 12 -> ((z - 13 - inputNumber) % 26) == 0 ? z/26 : 15 + inputNumber + z/26*26;
            case 13 -> ((z - 13 - inputNumber) % 26) == 0 ? z/26 : 14 + inputNumber + z/26*26;  // zIn = w_13 + ?        / zOut = 12..20
            case 14 -> ((z - 11 - inputNumber) % 26) == 0 ? z/26 : 9 + inputNumber + z/26*26;   // zIn = w_14 + 11 && zIn < 26 (zIn=12..20) / zOut = 0
            default -> throw new RuntimeException("Unknown program");
        };
    }

    public long solve2(String file) {
        List<Integer> data = stream(new StringCollector(file)
                .process().get(0).split(",")).map(Integer::valueOf).collect(toList());
        System.out.println(data);
        return data.get(0)+22;
    }

    public static void main(String[] args) {
        System.out.println(Day24.class);
        long count;
        //*
//        count = new Day24(true).solve("day24_test.txt");
//        System.out.println("Result: " + count);
//        // add vm option -ea to run configuration to throw exception on assert
//        assert count == 11;

        count = new Day24(true).solve("day24.txt");
        System.out.println("Result: " + count);
        assert count == 22;

        //*/

        count = new Day24(true).solve2("day24_test.txt");
        System.out.println("Result: " + count);
        assert count == 33;

        count = new Day24(true).solve2("day24.txt");
        System.out.println("Result: " + count);
        assert count == 44;
        //*/

        assert "VM options -ea set. You can delete this.".isEmpty();
        throw new RuntimeException("Don't forgot to set vm option -ea");
    }
}

/*

 New min z for 99999999999999 z=213598806
 New min z for 99999999999998 z=213598805
 New min z for 99999999999997 z=213598804
 New min z for 99999999999996 z=213598803
 New min z for 99999999999995 z=213598802
 New min z for 99999999999994 z=213598801
 New min z for 99999999999993 z=213598800
 New min z for 99999999999992 z=213598799
 New min z for 99999999999991 z=213598798
 New min z for 99999999999969 z=8215342
 New min z for 99999999999968 z=8215341
 New min z for 99999999999967 z=8215340
 New min z for 99999999999966 z=8215339
 New min z for 99999999999965 z=8215338
 New min z for 99999999999964 z=8215337
 New min z for 99999999999963 z=315974

 New min z for 99999999299919 z=315970
 New min z for 99999999299918 z=315969
 New min z for 99999999299917 z=315968
 New min z for 99999999299916 z=315967
 New min z for 99999999299915 z=315966
 New min z for 99999999299914 z=315965
 New min z for 99999999299913 z=315964
 New min z for 99999999299912 z=315963
 New min z for 99999999299911 z=315962
 New min z for 99999999298199 z=12160
 New min z for 99999999298198 z=12159
 New min z for 99999999298197 z=12158
 New min z for 99999999298196 z=12157
 New min z for 99999999298195 z=12156
 New min z for 99999999298194 z=12155
 New min z for 99999999298193 z=12154
 New min z for 99999999298192 z=12153
 New min z for 99999999298191 z=12152



 New min z for 99999994293969 z=467


 i + 4293969
 New min z for 9992999 z=17
 New min z for 8992999 z=16
 New min z for 7992999 z=15
 New min z for 6992999 z=14
 New min z for 5992999 z=13
 New min z for 4992999 z=12
 ...
 New min z for 1992999 z=9

 19929994293969 -> 9 :/
*/