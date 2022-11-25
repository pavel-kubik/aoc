package cz.pk.adventofcode.y2020.day8;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.y2020.day1.Day1;

public class Day8 {

    public static void main(String[] args) throws IOException {
        System.out.println(Day8.class);
        //System.out.println(new Day8().findCycle()); //1137
        System.out.println(new Day8().fixCycle());
    }

    int simulateRun(List<Instruction> instructions, boolean findLoop) {
        int accumulator = 0;
        int instructionPointer = 0;
        while (instructionPointer != instructions.size() &&
                !instructions.get(instructionPointer).getOperation().equals(Operation.DONE)) {
            Instruction currentInstruction = instructions.get(instructionPointer);

            switch (currentInstruction.getOperation()) {
                case NOP:
                    instructionPointer++;
                    break;
                case ACC:
                    accumulator += currentInstruction.getParameter();
                    instructionPointer++;
                    break;
                case JMP:
                    instructionPointer += currentInstruction.getParameter();
                    break;
                case DONE:
                default:
                    assert false;
            }
            currentInstruction.setOperation(Operation.DONE);
        }
        if (findLoop) {
            return accumulator;
        } else {
            if (instructionPointer == instructions.size()) {
                return accumulator;
            } else {
                return -1;
            }
        }
    }

    public int findCycle() throws IOException {
        List<Instruction> instructions = new CodeParser("2020/day8.txt").process();
        return simulateRun(instructions, true);
    }

    public int fixCycle() throws IOException {
        List<Instruction> instructions = new CodeParser("2020/day8.txt").process();
        int acc = -1;
        for (int i = 0; i < instructions.size(); i++) {
            System.out.println("Iteration " + i);
            if (instructions.get(i).getOperation().equals(Operation.ACC)) {
                continue;
            }
            List<Instruction> newCode = copy(instructions);
            Instruction instruction = newCode.get(i);
            if (instruction.getOperation().equals(Operation.NOP)) {
                instruction.setOperation(Operation.JMP);
            } else if (instruction.getOperation().equals(Operation.JMP)) {
                instruction.setOperation(Operation.NOP);
            } else {
                assert false;
            }
            acc = simulateRun(newCode, false);
            if (acc != -1) {
                break;
            }
        }
        return acc;
    }

    List<Instruction> copy(List<Instruction> instructionList) {
        return instructionList.stream().map(i -> i.copy()).toList();
    }

    enum Operation {
        NOP,
        ACC,
        JMP,
        DONE
    }

    class Instruction {
        private Operation operation;
        private Integer parameter;

        public Instruction(Operation operation, Integer parameter) {
            this.operation = operation;
            this.parameter = parameter;
        }

        public Operation getOperation() {
            return operation;
        }

        public Instruction setOperation(Operation operation) {
            this.operation = operation;
            return this;
        }

        public Integer getParameter() {
            return parameter;
        }

        public Instruction setParameter(Integer parameter) {
            this.parameter = parameter;
            return this;
        }

        public Instruction copy() {
            return new Instruction(operation, parameter);
        }
    }

    class CodeParser extends DataCollector<Instruction> {
        public CodeParser(String file) throws IOException {
            super(file);
        }

        @Override
        protected Instruction processLine(String line) {
            String[] parts = line.split(" ");
            assert parts.length == 2;
            Operation operation = Operation.valueOf(parts[0].toUpperCase());
            Integer value = Integer.valueOf(parts[1]);
            return new Instruction(operation, value);
        }
    }
}
