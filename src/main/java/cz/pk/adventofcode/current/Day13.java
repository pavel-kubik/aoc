package cz.pk.adventofcode.current;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
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

    List<Integer> busesHeader = new ArrayList<>();

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

    public long solve(String file) throws IOException {
        BusSheet[] busSheets = new TypeCollector("day13_sheet.txt").process().toArray(new BusSheet[1]);

        URL resource = getClass().getClassLoader().getResource(file);
        List<String> lines = Files.readAllLines(Path.of(resource.getPath()));
        long timestamp = Integer.valueOf(lines.get(0));
        List<Integer> validBusses = Arrays.asList(lines.get(1).split(",")).stream()
                .filter(b -> !b.equals("x"))
                .map(b -> Integer.valueOf(b))
                .collect(Collectors.toList());

        long minTimestamp = Long.MAX_VALUE;
        int minBus = -1;
        for (Integer bus : validBusses) {
            long busIterations = timestamp / bus + 1;
            long busTimestamp = busIterations * bus;
            if (busTimestamp < minTimestamp) {
                minTimestamp = busTimestamp;
                minBus = bus;
            }
        }
        return (minTimestamp - timestamp) * minBus;
    }

    public long solve2(String busSequence, long halt, long initialStep) throws IOException {
        long step = initialStep;
        List<Integer> validBusses = Arrays.asList(busSequence.split(",")).stream()
                .map(b -> !b.equals("x") ? Integer.valueOf(b) : 0)
                .collect(Collectors.toList());

        long minTimestamp = Long.MAX_VALUE;
        int minBus = -1;
        int maxBus = validBusses.stream()
                .mapToInt(v -> v)
                .max()
                .orElseThrow(NoSuchElementException::new);
        int maxIdx = validBusses.indexOf(maxBus);
        //long dreamTimestamp = maxBus;
        //long dreamTimestamp = halt > 0 ? ((halt - 1)/ maxBus) * maxBus : maxBus;
        long dreamTimestamp = step > 0 ? step * maxBus : maxBus;
        long it = 0;
        int logger = 0;
        while (true) {
            //            if (halt > 0 && dreamTimestamp > halt + 2*maxBus) {
            //                assert false;
            //            }
            it++;
            logger = logTimestamp(logger, dreamTimestamp);
            dreamTimestamp += step > 0 ? step : maxBus;
            long iterationStart = dreamTimestamp - maxIdx + validBusses.size() - 1;
            boolean allMatch = true;
            for (int i = validBusses.size() - 1; i >= 0; i--) {
                //System.out.println(dreamTimestamp);
                Integer bus = validBusses.get(i);
                int diff = validBusses.size() - i - 1;
                boolean busMatch = (iterationStart - diff) % bus == 0;
                if (busMatch) {
                    // TODO determine match period and increase step
                    step =
                }
                if (bus > 0 && !busMatch) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) {
                dreamTimestamp = iterationStart;
                break;
            }
        }
        System.out.println("Iter: " + it);
        return dreamTimestamp - validBusses.size() + 1;
    }

    public static void main(String[] args) throws IOException {
        long count;
        /*
        count = new Day13(true).solve("day13_test.txt");
        System.out.println("Result: " + count);
        assert count == 295;

        count = new Day13(true).solve("day13.txt");
        System.out.println("Result: " + count);
        assert count == 2215;
        // not 1005163

        /*/

        count = new Day13(true).solve2("17,x,13,19", 0, 0);
        System.out.println("Result: " + count);
        assert count == 3417;

        count = new Day13(true).solve2("x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,823,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,443,x,x,x,x,x,x,x,x,x,x,x,x,x", 0, 0);
        System.out.println("Result: " + count);

        count = new Day13(true).solve2("67,7,59,61", 0, 0);
        System.out.println("Result: " + count);
        assert count == 754018;

        count = new Day13(true).solve2("67,x,7,59,61", 0, 0);
        System.out.println("Result: " + count);
        assert count == 779210;

        count = new Day13(true).solve2("67,7,x,59,61", 0, 0);
        System.out.println("Result: " + count);
        assert count == 1261476;

        count = new Day13(true).solve2("1789,37,47,1889", 0, 0);
        System.out.println("Result: " + count);
        assert count == 1202161486;

        count = new Day13(true).solve2("19,x,x,x,x,x,x,x,x,41,x,x,x,x,x,x,x,x,x,823,x,x,x,x,x,x,x,23,x,x,x,x,x,x,x,x,17,x,x,x,x,x,x,x,x,x,x,x,29,x,443,x,x,x,x,x,37,x,x,x,x,x,x,13", 0, 111);
        System.out.println("FINAL Result: " + count);
        //        assert count == 1202161486;

        //        count = new Day13(true).solve2("2020/day12.txt");
        //        System.out.println("Result: " + count);
        //assert count == 27016;

        //*/
    }

    public int logTimestamp(int logger, long dreamTimestamp) {
        if (logger == 0 && dreamTimestamp > 10000000000l) {
            System.out.println("iter -4 @" + LocalDateTime.now());
            logger++;
        }
        if (logger == 1 && dreamTimestamp > 100000000000l) {
            System.out.println("iter -3 @" + LocalDateTime.now());
            logger++;
        }
        if (logger == 2 && dreamTimestamp > 1000000000000l) {
            System.out.println("iter -2 @" + LocalDateTime.now());
            logger++;
        }
        if (logger == 3 && dreamTimestamp > 10000000000000l) {
            System.out.println("iter -1 @" + LocalDateTime.now());
            logger++;
        }
        if (logger == 4 && dreamTimestamp > 100000000000000l) {
            System.out.println("iter 0 @" + LocalDateTime.now());
            logger++;
        }
        if (logger == 5 && dreamTimestamp > 1000000000000000l) {
            System.out.println("iter 1 @" + LocalDateTime.now());
            logger++;
        }
        if (logger == 6 && dreamTimestamp > 10000000000000000l) {
            System.out.println("iter 2 @" + LocalDateTime.now());
            logger++;
        }
        return logger;
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
}