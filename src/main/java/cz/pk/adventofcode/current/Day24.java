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

    /*
     * w = 1..9
     * dz = 1, 26
     * az = 13, -11, 12, 12, 14, -5, 10, 0, -13, -11
     * ay = 8, 1, 0, 16, 13, 10, 4, 5, 7, 2, 15, 9, 13, 14
     */
    public long processNative(long w, long z, long dz, long ax, long ay) {
        long x;
        long y;

        x = z % 26 + ax == w ? 0 : 1;
        y = 25*x + 1;
        return z / dz * y + (w + ay)*x;
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

    public long solveFromBack(String file) {
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

        programOutputs.put(14, Set.of(0L));
        for (int i = 14; i >= 1; i--) {
            System.out.println("Program " + i);
            for (int w = 1; w <= 9; w++) {
                long zMax = i >= 5 && i <= 12 ? 260000L : 10000L;
                for (long z = 0; z < zMax; z++) {
                    long out = runProgram(i, w, z);
                    if (programOutputs.get(i).contains(out)) {
                        long finalZ = z;
                        programOutputs.compute(i-1, (k, v) -> {
                            if (v == null) {
                                v = new HashSet<>();
                            }
                            v.add(finalZ);
                            return v;
                        });
//                        System.out.println("For number " + w + " and z=" + z + " got " + out);
                        //break;
                    }
                }
            }
            System.out.println("Outputs: " + programOutputs.get(i-1).size());
        }
        return 0;
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
        programOutputs.put(14, Set.of(0L));

        for (int i = 14; i >= 7; i--) {
            System.out.println("Program " + i);
            for (int w = 1; w <= 9; w++) {
                long zMax = i >= 5 && i <= 12 ? 260000L : 10000L;
                for (long z = 0; z < zMax; z++) {
                    long out = runProgram(i, w, z);
                    if (programOutputs.get(i).contains(out)) {
                        long finalZ = z;
                        programOutputs.compute(i-1, (k, v) -> {
                            if (v == null) {
                                v = new HashSet<>();
                            }
                            v.add(finalZ);
                            return v;
                        });
                        programWZOutputs.compute((i-1) + ":" + w, (k, v) -> {
                            if (v == null) {
                                v = new HashSet<>();
                            }
                            v.add(new Vector2<>(finalZ, out));
                            return v;
                        });
                    }
                }
            }
            System.out.println("Outputs: " + programOutputs.get(i-1).size());
        }

        for (int i = 1; i <= 7; i++) {
            System.out.println("Program " + i);
            for (int w = 1; w <= 9; w++) {
                for (Long z : programOutputs.get(i-1)) {
                    long outZ = runProgram(i, w, z);
                    programOutputs.compute(i, (k, v) -> {
                        if (v == null) {
                            v = new HashSet<>();
                        }
                        v.add(outZ);
                        return v;
                    });
                    programWZOutputs.compute(i + ":" + w, (k, v) -> {
                        if (v == null) {
                            v = new HashSet<>();
                        }
                        v.add(new Vector2<>(z, outZ));
                        return v;
                    });
                }
            }
            System.out.println("Outputs: " + programOutputs.get(i).size());
        }

        long lastZ = 0;
        String outputNumber = "";
        for (int i = 1; i <= 13; i++) {
            for (int w = 9; w >= 1; w--) {
                long finalLastZ = lastZ;
                List<Vector2<Long>> matches = programWZOutputs
                        .get(i + ":" + w)
                        .stream()
                        .filter(v -> v.x == finalLastZ)
                        .toList();
                if (!matches.isEmpty()) {
                    outputNumber += w;
                    lastZ = matches.get(0).y;
                    break;
                };
            }
        }
        System.out.println(outputNumber);

        for (int i = 1111; i <= 9999; i++) {
            //if ()

        }

        return 0;
    }

    private long solveBF(String file) {
        List<Instruction> instructions = new InstructionCollector(file).process();

        //input = "13579246899999";
        //input = "99999999999999";
        //input = "11111111111111";
        //input = "123456789123456789";
        //input = "99999999999999";
        long minZ = Long.MAX_VALUE;
        for (long i = 99999999999999L; i > 0; i--) {
            // 99996539300000
            input = Long.toString(i);
            if (input.indexOf('0') == -1) {

                variables.put("w", 0L);
                variables.put("x", 0L);
                variables.put("y", 0L);
                variables.put("z", 0L);

                inputIndex = 0;

                for (Instruction instruction : instructions) {
                    //System.out.println(instruction);
                    processInstruction(instruction);
                    //System.out.println(variables);
                }
                if (variables.get("z") == 0) {
                    System.out.println("ZERO for " + input);
                    break;
                }
                //System.out.println("Min z for " + input + " z=" + minZ);
                if (variables.get("z") < minZ) {
                    minZ = variables.get("z");
                    System.out.println("New min z for " + input + " z=" + minZ);
                }
            }
            if (i % 100000 == 0) {
                System.out.println("Iteration " + i);
            }
        }
        return Long.parseLong(input);
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