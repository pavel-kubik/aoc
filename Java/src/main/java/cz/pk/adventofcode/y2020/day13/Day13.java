package cz.pk.adventofcode.y2020.day13;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.FileReadUtil;
import cz.pk.adventofcode.y2020.day1.Day1;
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
                        .toList();
                return null;
            } else {
                Integer time = Integer.valueOf(items[0]);
                List<Boolean> buses = Arrays.asList(items).stream()
                        .skip(1)
                        .map(i -> i.equals("D"))
                        .toList();
                return new BusSheet(time, buses);
            }
        }
    }

    public long solve(String file) throws IOException {
        BusSheet[] busSheets = new TypeCollector("2020/day13_sheet.txt").process().toArray(new BusSheet[1]);

        List<String> lines = FileReadUtil.readAllLines(file);
        long timestamp = Integer.valueOf(lines.get(0));
        List<Integer> validBusses = Arrays.asList(lines.get(1).split(",")).stream()
                .filter(b -> !b.equals("x"))
                .map(b -> Integer.valueOf(b))
                .toList();

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

    @AllArgsConstructor
    private static class Bus {
        int busNumber;
        int offset;

        public long at(int period) {
            return period * busNumber - offset;
        }
    }

    public long solve2(String busSequence, long halt, long initialStep) throws IOException {
        long step = initialStep;
        List<Integer> validBusses = Arrays.asList(busSequence.split(",")).stream()
                .map(b -> !b.equals("x") ? Integer.valueOf(b) : 0)
                .toList();

        List<Bus> buses = new ArrayList<>();
        for (int i = 0; i < validBusses.size(); i++) {
            if (validBusses.get(i) != 0) {
                buses.add(new Bus(validBusses.get(i), i));
            }
        }

        //TODO order
        //buses.sort((bus1, bus2) -> Integer.compare(bus1.busNumber, bus2.busNumber));

        long dreamTimestamp = buses.get(0).at(1);
        long commonPeriod = buses.get(0).busNumber;
        int logger = 0;
        int it = 0;
        for (int i = 1; i < buses.size(); i++) {
            Bus bus = buses.get(i);
            // find match: dreamTimestamp + x*commonPeriod == bus.offset + y*bus.busNumber
            long matchTimestamp = -1;
            for (long s = 0; s < Long.MAX_VALUE; s++) {
                it++;

                long t = dreamTimestamp + s * commonPeriod; // all previous busses match

                if (t < 0) {
                    continue;
                }

                logger = logTimestamp(logger, t);
                //System.out.println(t);

                // match new bus
                long y = (t - buses.get(0).offset) / bus.busNumber; // <= bus.offset + y*bus.busNumber = t
                if ((0 == (t + bus.offset) % bus.busNumber)) {
                    matchTimestamp = t;
                    break;
                }

            }
            if (matchTimestamp == -1) {
                assert matchTimestamp != -1;
            }
            dreamTimestamp = matchTimestamp;
            commonPeriod *= bus.busNumber;
            System.out.println("Bus " + i + "/" + buses.size() + " t=" + dreamTimestamp + " T=" + commonPeriod + " it=" + it);
        }
        System.out.println(it);

        return dreamTimestamp;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(Day13.class);
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

//        count = new Day13(true).solve2("3,5", 0, 0);
//        System.out.println("Result: " + count);
//        assert count == 9;
//
//        count = new Day13(true).solve2("x,3,5", 0, 0);
//        System.out.println("Result: " + count);
//        assert count == 8;

        count = new Day13(true).solve2("x,x,x,x,x,x,3,5", 0, 0);
        System.out.println("Result: " + count);
        assert count == 3;

        count = new Day13(true).solve2("5,3", 0, 0);
        System.out.println("Result: " + count);
        assert count == 5;

        count = new Day13(true).solve2("3,5,7", 0, 0);
        System.out.println("Result: " + count);
        assert count == 54;

        count = new Day13(true).solve2("17,x,13,19", 0, 0);
        System.out.println("Result: " + count);
        assert count == 3417;   //2771

        //count = new Day13(true).solve2("x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,823,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,443,x,x,x,x,x,x,x,x,x,x,x,x,x", 0, 0);
        //System.out.println("Result: " + count);

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
        if (logger == 0 && dreamTimestamp > 1234567890l) {
            System.out.println("iter -5 @" + LocalDateTime.now());
            logger++;
        }
        if (logger == 1 && dreamTimestamp > 12345678901l) {
            System.out.println("iter -4 @" + LocalDateTime.now());
            logger++;
        }
        if (logger == 2 && dreamTimestamp > 123456789012l) {
            System.out.println("iter -3 @" + LocalDateTime.now());
            logger++;
        }
        if (logger == 3 && dreamTimestamp > 1234567890123l) {
            System.out.println("iter -2 @" + LocalDateTime.now());
            logger++;
        }
        if (logger == 4 && dreamTimestamp > 12345678901234l) {
            System.out.println("iter -1 @" + LocalDateTime.now());
            logger++;
        }
        //                                  100000000000000
        if (logger == 5 && dreamTimestamp > 123456789012345l) {
            System.out.println("iter 0 @" + LocalDateTime.now());
            logger++;
        }
        if (logger == 6 && dreamTimestamp > 1234567890123456l) {
            System.out.println("iter 1 @" + LocalDateTime.now());
            logger++;
        }
        if (logger == 6 && dreamTimestamp > 12345678901234567l) {
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