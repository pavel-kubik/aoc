package cz.pk.adventofcode.y2021.day4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.GroupProcessor;
import lombok.AllArgsConstructor;
import lombok.Data;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day4 {

    private final boolean debug;

    @Data
    @AllArgsConstructor
    class Subject {
        Type type;
        int size;
    }

    enum Type {
        PLACEHOLDER("p"),
        PLACEHOLDER2("q"),
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

    class TypeCollector extends DataCollector<Subject> {

        public TypeCollector(String file) {
            super(file);
        }

        @Override
        protected Subject processLine(String line) {
            Type type = Type.get(String.valueOf(line.charAt(0)));
            Integer size = Integer.valueOf(line.substring(1));
            return new Subject(type, size);
        }
    }

    class BingoProcessor extends GroupProcessor {
        private List<Integer> numbers;
        private List<Integer[][]> bingos;
        private boolean firstGroup;

        public BingoProcessor(String file) throws IOException {
            super(file);
            bingos = new ArrayList<>();
            firstGroup = true;
        }

        @Override
        protected int processGroup(List<String> groupLines) {
            if (firstGroup) {
                String firstLine = groupLines.get(0);
                numbers = stream(firstLine.split(",")).map(Integer::valueOf).collect(toList());
                firstGroup = false;
            } else {
                Integer[][] bingo = new Integer[groupLines.size()][];
                for (int i = 0; i < groupLines.size(); i++) {
                    String line = groupLines.get(i).trim();
                    Integer[] lineNumbers = stream(line.split("\\s+")).map(Integer::valueOf).collect(toList()).toArray(new Integer[1]);
                    bingo[i] = lineNumbers;
                }
                bingos.add(bingo);
            }
            return 0;
        }

        public List<Integer> getNumbers() {
            return numbers;
        }

        public List<Integer[][]> getBingos() {
            return bingos;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("numbers", numbers)
                    .append("bingos", bingos)
                    .toString();
        }
    }

    private void markNumberAtBingos(Integer number, List<Integer[][]> bingos) {
        for (int i=0;i<bingos.size();i++) {
            markNumberAtBingos(number, bingos.get(i));
        }
    }

    private void markNumberAtBingos(Integer number, Integer[][] bingo) {
        for (int i = 0; i < bingo.length; i++) {
            for (int j = 0; j < bingo[i].length; j++) {
                if (bingo[i][j].equals(number)) {
                    bingo[i][j] = -1;
                }
            }
        }
    }

    private int checkBingo(List<Integer[][]> bingos) {
        for (int i=0;i<bingos.size();i++) {
            if (checkBingo(bingos.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private boolean checkBingo(Integer[][] bingo) {
        // line
        for (int i = 0; i < bingo.length; i++) {
            boolean checked = true;
            for (int j = 0; j < bingo[i].length; j++) {
                if (!bingo[i][j].equals(-1)) {
                    checked = false;
                    break;
                }
            }
            if (checked) {
                return true;
            }
        }
        // column
        for (int i = 0; i < bingo.length; i++) {
            boolean checked = true;
            for (int j = 0; j < bingo[i].length; j++) {
                if (!bingo[j][i].equals(-1)) {
                    checked = false;
                    break;
                }
            }
            if (checked) {
                return true;
            }
        }
        return false;
    }

    private int sumRemainingNumbers(Integer[][] bingo) {
        int sum = 0;
        for (int i = 0; i < bingo.length; i++) {
            for (int j = 0; j < bingo[i].length; j++) {
                if (!bingo[i][j].equals(-1)) {
                    sum += bingo[i][j];
                }
            }
        }
        return sum;
    }

    public long solve(String file) throws IOException {
        BingoProcessor bingos = new BingoProcessor(file);
        bingos.processGroups();

        int winner = -1;
        int lastNumber = -1;
        for (int i=0;i<bingos.getNumbers().size();i++) {
            markNumberAtBingos(bingos.getNumbers().get(i), bingos.getBingos());
            int winIdx = checkBingo(bingos.getBingos());
            if (winIdx != -1) {
                winner = winIdx;
                lastNumber = bingos.getNumbers().get(i);
                break;
            }
        }

        int remainingNumbers = sumRemainingNumbers(bingos.getBingos().get(winner));

        System.out.println(bingos);
        return lastNumber*remainingNumbers;
    }

    public long solve2(String file) throws IOException {
        BingoProcessor bingos = new BingoProcessor(file);
        bingos.processGroups();

        int winner = -1;
        int lastNumber = -1;
        for (int i=0;i<bingos.getNumbers().size();i++) {
            System.out.println("Take number " + bingos.getNumbers().get(i));
            markNumberAtBingos(bingos.getNumbers().get(i), bingos.getBingos());
            int winIdx = checkBingo(bingos.getBingos());
            boolean finish = false;
            while (winIdx != -1) {
                winner = winIdx;
                if (bingos.getBingos().size() > 1) {
                    bingos.getBingos().remove(winner);
                    System.out.println("Removed idx " + winIdx);
                } else {
                    lastNumber = bingos.getNumbers().get(i);
                    finish = true;
                    break;
                }
                winIdx = checkBingo(bingos.getBingos());
            }
            if (finish) break;
        }

        int remainingNumbers = sumRemainingNumbers(bingos.getBingos().get(winner));

        System.out.println(bingos);
        return lastNumber*remainingNumbers;
    }

    public static void main(String[] args) throws IOException {
        long count;
        //*
        count = new Day4(true).solve("2021/day4_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 4512;

        count = new Day4(true).solve("2021/day4.txt");
        System.out.println("Result: " + count);
        assert count == 11774; //28987

        //*/

        count = new Day4(true).solve2("2021/day4_test.txt");
        System.out.println("Result: " + count);
        assert count == 1924;

        count = new Day4(true).solve2("2021/day4.txt");
        System.out.println("Result: " + count);
        assert count == 4495;    //0
        //*/
    }
}
