package cz.pk.adventofcode.y2019.day2;

import cz.pk.adventofcode.util.FileReadUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day2 {

    public static int runProgram(List<Integer> input) {
        int ip = 0; // instruction pointer
        int arg1, arg2;
        boolean run = true;
        while (run) {
            switch (input.get(ip)) {
                case 1: // add
                    arg1 = input.get(input.get(ip + 1));
                    arg2 = input.get(input.get(ip + 2));
                    input.set(input.get(ip + 3), arg1 + arg2);
                    break;
                case 2: // multiply
                    arg1 = input.get(input.get(ip + 1));
                    arg2 = input.get(input.get(ip + 2));
                    input.set(input.get(ip + 3), arg1 * arg2);
                    break;
                case 99:
                    run = false;
                    break;
            }
            ip += 4;
        }
        return input.get(0);
    }

    public static void main(String[] args) {
        // TEST
        String input1 = "1,9,10,3,2,3,11,0,99,30,40,50";
        List<Integer> in1 = new ArrayList<>(Arrays
                .stream(input1.split(","))
                .map(s -> Integer.valueOf(s))
                .toList());
        int out1 = runProgram(in1);
        assert out1 == 3500;

        // PART 1
        List<String> input2 = FileReadUtil.readAllLines("2019/day2.txt");
        List<Integer> in2 = new ArrayList<>(Arrays
                .stream(input2.get(0).split(","))
                .map(s -> Integer.valueOf(s))
                .toList());
        in2.set(1, 12);
        in2.set(2, 2);
        int out2 = runProgram(in2);
        System.out.println(out2);
        assert out2 == 12490719;

        // PART 2
        List<String> input3 = FileReadUtil.readAllLines("2019/day2.txt");
        int answer = -1;
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                List<Integer> in3 = new ArrayList<>(Arrays
                        .stream(input2.get(0).split(","))
                        .map(s -> Integer.valueOf(s))
                        .toList());
                in3.set(1, i);
                in3.set(2, j);
                int out3 = runProgram(in3);
                if (out3 == 19690720) {
                    answer = 100 * i + j;
                    break;
                }
            }
            if (answer != -1) {
                break;
            }
        }
        System.out.println(answer);
    }
}
