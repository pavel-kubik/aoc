package cz.pk.adventofcode.current;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cz.pk.adventofcode.util.DataCollector;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Day13 {

    private final boolean debug;

    @Data
    @AllArgsConstructor
    class BusSheet {
        int timestamp;
        List<Boolean> buses;
    }

    List <Integer> busesHeader = new ArrayList<>();

    class TypeCollector extends DataCollector<BusSheet> {

        public TypeCollector(String file) {
            super(file);
        }

        @Override
        protected BusSheet processLine(String line) {
            String[] items = line.split("\\s+");
            if (this.lineNumber == 0) {
                // header
                busesHeader = Arrays.asList(items).stream()
                        .skip(1)
                        .filter(i -> !i.equals("bus"))
                        .map(b -> Integer.valueOf(b))
                        .collect(Collectors.toList());
                return null;
            } else {
                Integer time = Integer.valueOf(items[0]);
                List<Boolean> buses = Arrays.asList(items).stream()
                        .skip(1)
                        .map(i -> i.equals("D"))
                        .collect(Collectors.toList());
                return new BusSheet(time, buses);
            }
        }
    }

    public int solve(String file) throws IOException {
        BusSheet[] busSheets = new TypeCollector("day13_sheet.txt").process().toArray(new BusSheet[1]);

        URL resource = getClass().getClassLoader().getResource(file);
        List<String> lines = Files.readAllLines(Path.of(resource.getPath()));
        int timestamp = Integer.valueOf(lines.get(0));
        List<Integer> validBusses = Arrays.asList(lines.get(1).split(",")).stream()
                .filter(b -> !b.equals("x"))
                .map(b -> Integer.valueOf(b))
                .collect(Collectors.toList());

        int minTimestamp = Integer.MAX_VALUE;
        int minBus = -1;
        for (Integer bus : validBusses) {
            int busIterations = timestamp / bus + 1;
            int busTimestamp = busIterations*bus;
            if (busTimestamp < minTimestamp) {
                minTimestamp = busTimestamp;
                minBus = bus;
            }
        }
        return (minTimestamp - timestamp)*minBus;
    }

    public int solve2(String file) {
        BusSheet[] data = new TypeCollector(file).process().toArray(new BusSheet[1]);
        System.out.println(data);
        return 0;
    }

    public static void main(String[] args) throws IOException {
        int count;
        /*
        count = new Day13(true).solve("day13_test.txt");
        System.out.println("Result: " + count);
        assert count == 295;

        count = new Day13(true).solve("day13.txt");
        System.out.println("Result: " + count);
        assert count == 2215;
        // not 1005163

        /*/

        count = new Day13(true).solve2("2020/day12_test.txt");
        System.out.println("Result: " + count);
        assert count == 25;

        count = new Day13(true).solve2("2020/day12.txt");
        System.out.println("Result: " + count);
        //assert count == 27016;

         */
        //*/
    }
}

/*
        for (BusSheet busTime : busSheets) {
            if (busTime.getTimestamp() >= timestamp) {
                for (int i=0;i<busTime.getBuses().size();i++) {
                    if (busTime.getBuses().get(i)) {
                        if (validBusses.contains(busesHeader.get(i))) {
                            minTimestamp = busTime.getTimestamp();
                            bus = busesHeader.get(i);
                            break;
                        }
                    }
                }
                if (bus != -1) {
                    break;
                }
            }
            if (bus != -1) {
                break;
            }
        }
 */
