package cz.pk.adventofcode.current;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.StringCollector;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public long processNative() {
        long w = 0;
        long x = 0;
        long y = 0;
        long z = 0;

        w = getNextInput(); //        inp w
        x *= 0;             //        mul x 0
        x += z;             //        add x z
        x += 26;            //        mod x 26
        z /= 1;             //        div z 1
        x += 13;            //        add x 13
        x = x == w ? 1 : 0; //        eql x w
        x = x == 0 ? 1 : 0; //        eql x 0
        y *= 0;             //        mul y 0
        y += 25;            //        add y 25
        y *= x;             //        mul y x
        y += 1;             //        add y 1
        z *= y;             //        mul z y
        y *= 0;             //        mul y 0
        y += w;             //        add y w
        y += 8;             //        add y 8
        y *= x;             //        mul y x
        z += y;             //        add z y
        /*
        (w, x, y, z) -> (
            x': z + 29 == w ? 0 : 1,
            y': (w + 8)*x'
            z': z * (25 * x' + 1) + (w + 8)*x'
            )
        */
//        inp w
//        mul x 0
//        add x z
//        mod x 26
//        div z 1
//        add x 12  // 13
//        eql x w
//        eql x 0
//        mul y 0
//        add y 25
//        mul y x
//        add y 1
//        mul z y
//        mul y 0
//        add y w
//        add y 16  // 8
//        mul y x
//        add z y

//        inp w
//        mul x 0
//        add x z
//        mod x 26
//        div z 1
//        add x 10
//        eql x w
//        eql x 0
//        mul y 0
//        add y 25
//        mul y x
//        add y 1
//        mul z y
//        mul y 0
//        add y w
//        add y 4
//        mul y x
//        add z y

//        inp w
//        mul x 0
//        add x z
//        mod x 26
//        div z 26
//        add x -11
//        eql x w
//        eql x 0
//        mul y 0
//        add y 25
//        mul y x
//        add y 1
//        mul z y
//        mul y 0
//        add y w
//        add y 1
//        mul y x
//        add z y

//        inp w
//        mul x 0
//        add x z
//        mod x 26
//        div z 1
//        add x 14
//        eql x w
//        eql x 0
//        mul y 0
//        add y 25
//        mul y x
//        add y 1
//        mul z y
//        mul y 0
//        add y w
//        add y 13
//        mul y x
//        add z y

//        inp w
//        mul x 0
//        add x z
//        mod x 26
//        div z 1
//        add x 13
//        eql x w
//        eql x 0
//        mul y 0
//        add y 25
//        mul y x
//        add y 1
//        mul z y
//        mul y 0
//        add y w
//        add y 5
//        mul y x
//        add z y

//        inp w
//        mul x 0
//        add x z
//        mod x 26
//        div z 1
//        add x 12
//        eql x w
//        eql x 0
//        mul y 0
//        add y 25
//        mul y x
//        add y 1
//        mul z y
//        mul y 0
//        add y w
//        add y 0
//        mul y x
//        add z y

//        inp w
//        mul x 0
//        add x z
//        mod x 26
//        div z 26
//        add x -5
//        eql x w
//        eql x 0
//        mul y 0
//        add y 25
//        mul y x
//        add y 1
//        mul z y
//        mul y 0
//        add y w
//        add y 10
//        mul y x
//        add z y

//        inp w
//        mul x 0
//        add x z
//        mod x 26
//        div z 1
//        add x 10
//        eql x w
//        eql x 0
//        mul y 0
//        add y 25
//        mul y x
//        add y 1
//        mul z y
//        mul y 0
//        add y w
//        add y 7
//        mul y x
//        add z y

//        inp w
//        mul x 0
//        add x z
//        mod x 26
//        div z 26
//        add x 0
//        eql x w
//        eql x 0
//        mul y 0
//        add y 25
//        mul y x
//        add y 1
//        mul z y
//        mul y 0
//        add y w
//        add y 2
//        mul y x
//        add z y

//        inp w
//        mul x 0
//        add x z
//        mod x 26
//        div z 26
//        add x -11
//        eql x w
//        eql x 0
//        mul y 0
//        add y 25
//        mul y x
//        add y 1
//        mul z y
//        mul y 0
//        add y w
//        add y 13
//        mul y x
//        add z y

//        inp w
//        mul x 0
//        add x z
//        mod x 26
//        div z 26
//        add x -13
//        eql x w
//        eql x 0
//        mul y 0
//        add y 25
//        mul y x
//        add y 1
//        mul z y
//        mul y 0
//        add y w
//        add y 15
//        mul y x
//        add z y

//        inp w
//        mul x 0
//        add x z
//        mod x 26
//        div z 26
//        add x -13
//        eql x w
//        eql x 0
//        mul y 0
//        add y 25
//        mul y x
//        add y 1
//        mul z y
//        mul y 0
//        add y w
//        add y 14
//        mul y x
//        add z y

//        inp w
//        mul x 0
//        add x z
//        mod x 26
//        div z 26
//        add x -11
//        eql x w
//        eql x 0
//        mul y 0
//        add y 25
//        mul y x
//        add y 1
//        mul z y
//        mul y 0
//        add y w
//        add y 9
//        mul y x
//        add z y
        return z;
    }

    Map<Integer, List<Instruction>> programs = new HashMap<>();


    public long solve(String file) {
        List<Instruction> instructions = new InstructionCollector(file).process();

        //input = "13579246899999";
        //input = "99999999999999";
        //input = "11111111111111";
        //input = "123456789123456789";
        //input = "99999999999999";
        long minZ = Long.MAX_VALUE;
        for (long i = 19929994293969L; i > 0; i--) {
            input = Long.toString(i);
        //for (long i = 42939694293969L; i > 0; i--) {
        //for (long i = 9999999L; i > 0; i--) {
        //for (long i = 1000000L; i <= 9999999L; i++) {

        //String base = "19929994293969";
        //for (int i = 0; i < 14; i++) {

//            char[] number = base.toCharArray();
//            number[i] = String.valueOf(Integer.valueOf(Character.toString(number[i])) - 1).charAt(0);
//            input = new String(number);

            variables.put("w", 0L);
            variables.put("x", 0L);
            variables.put("y", 0L);
            variables.put("z", 0L);

            inputIndex = 0;

            //input = Long.toString(i) + "4293969";
            //input = Long.toString(i) + "4183914";

            //input = "5992999" + Long.toString(i);
            //input = "1182999" + Long.toString(i);
            if (input.indexOf('0') == -1) {
                for (Instruction instruction : instructions) {
                    System.out.println(instruction);
                    processInstruction(instruction);
                    System.out.println(variables);
                }
                if (variables.get("z") == 0) {
                    System.out.println("ZERO for " + input);
                    break;
                }
                //System.out.println("Min z for " + input + " z=" + minZ);
                if (variables.get("z") <= minZ) {
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