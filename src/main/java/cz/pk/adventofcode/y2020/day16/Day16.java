package cz.pk.adventofcode.y2020.day16;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Day16 {

    @Data
    @AllArgsConstructor
    public static class Interval {
        long from;
        long to;

        public boolean contains(long value) {
            return from <= value && value <= to;
        }
    }

    public List<Long> parseTicket(String ticket) {
        return Arrays.stream(ticket.split(","))
                .map(v -> Long.valueOf(v))
                .collect(Collectors.toList());
    }

    public boolean isPartOf(long value, List<Interval> intervals) {
        for (Interval interval : intervals) {
            if (interval.contains(value)) {
                return true;
            }
        }
        return false;
    }

    public long solve(String file, int part) throws IOException {
        URL resource = getClass().getClassLoader().getResource(file);
        List<String> data = Files.readAllLines(Path.of(resource.getPath()));

        List<String> numbersAtTicket = new ArrayList<>();
        List<String> yourTicket = new ArrayList<>();
        List<String> otherTickets = new ArrayList<>();
        List<String> current = numbersAtTicket;
        for (String line : data) {
            if (line.isEmpty()) {
                if (current == numbersAtTicket) {
                    current = yourTicket;
                } else if (current == yourTicket) {
                    current = otherTickets;
                }
                continue;
            }
            current.add(line);
        }

        // process numbers
        Map<String, List<Interval>> fieldsByInterval = new HashMap<>();
        List<String> fieldOrdered = new ArrayList<>();

        List<Interval> intervals = new ArrayList<>();
        for (String ticketItem : numbersAtTicket) {
            String fieldName = ticketItem.split(":")[0];
            fieldOrdered.add(fieldName);
            List<Interval> lineInterval = Arrays.stream(ticketItem.split(":")[1].split("or"))
                    .map(l -> l.trim())
                    .map(l -> new Interval(Long.parseLong(l.split("-")[0]),
                            Long.parseLong(l.split("-")[1])))
                    .collect(Collectors.toList());
            fieldsByInterval.put(fieldName, lineInterval);
            intervals.addAll(lineInterval);
        }

        List<List<Long>> validTickets = new ArrayList<>();

        long wrongNumbers = 0;
        for (String ticket : otherTickets.subList(1, otherTickets.size())) {
            List<Long> values = parseTicket(ticket);
            boolean validTicket = true;
            for (Long value : values) {
                boolean found = false;
                for (Interval interval : intervals) {
                    if (interval.contains(value)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    wrongNumbers += value;
                    System.out.println("Wrong number: " + value);
                    validTicket = false;
                }
            }
            if (validTicket) {
                validTickets.add(values);
            }
        }

        long multiple = 1;
        if (part == 2) {
            //validTickets.add(parseTicket(yourTicket.get(1)));
            // pre filter rows - find allowed positions for each field
            Map<String, List<Integer>> allowedPositionsForKey = new HashMap<>();
            for (int position = 0; position < fieldOrdered.size(); position++) {
                for (String field : fieldOrdered) {
                    boolean match = true;
                    for (List<Long> ticket : validTickets) {
                        if (!isPartOf(ticket.get(position), fieldsByInterval.get(field))) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        if (allowedPositionsForKey.get(field) == null) {
                            allowedPositionsForKey.put(field, new ArrayList<>(Arrays.asList(position)));
                        } else {
                            allowedPositionsForKey.get(field).add(position);
                        }
                    }
                }
            }

            // part 2 determine rows
            String[] keys = fieldOrdered.toArray(new String[1]);//fieldsByInterval.keySet().toArray(new String[1]);
            List<String> keyOrder = new ArrayList<>(Collections.nCopies(keys.length, ""));
            Set<String> matchedKeys = new HashSet<>();

            while (true) {
                List<String> selectedKeys = allowedPositionsForKey.entrySet().stream()
                        .filter(e -> e.getValue().size() == 1)
                        .map(e -> e.getKey())
                        .collect(Collectors.toList());
                for (String key : selectedKeys) {
                    assert !matchedKeys.contains(key);
                    int knownPosition = allowedPositionsForKey.get(key).get(0);
                    keyOrder.set(knownPosition, key);
                    matchedKeys.add(key);
                    allowedPositionsForKey.remove(key);
                    allowedPositionsForKey.entrySet().stream()
                            .filter(e -> e.getValue().contains(knownPosition))
                            .forEach(e -> e.getValue().remove((Integer)knownPosition));
                }
                if (selectedKeys.isEmpty()) {
                    break;
                }
            }

            for (Integer value : matchedKeys.stream()
                    .filter(k -> k.startsWith("departure"))
                    .map(k -> keyOrder.indexOf(k))
                    .collect(Collectors.toList())) {
                multiple *= parseTicket(yourTicket.get(1)).get(value);
            }

//            for (int position = 0; position < keys.length; position++) {
//                boolean found = false;
//                for (int filedIdx = 0; filedIdx < keys.length; filedIdx++) {
//                    if (matchedKeys.contains(keys[filedIdx])) {
//                        continue;
//                    }
//                    // find first key where all numbers match this interval
//                    boolean match = true;
//                    for (List<Long> ticket : validTickets) {
//                        if (!isPartOf(ticket.get(position), fieldsByInterval.get(keys[filedIdx]))) {
//                            match = false;
//                            break;
//                        }
//                    }
//                    if (match) {
//                        // j-th key match to i-th interval
//                        if (keys[filedIdx].startsWith("departure")) {
//                            System.out.println("Match " + position + " => " + keys[filedIdx]);
//                            multiple *= position;
//                        }
//                        keyOrder.set(position, keys[filedIdx]);
//                        matchedKeys.add(keys[filedIdx]);
//                        found = true;
//                        break;
//                    }
//                }
//                if (!found) {
//
//                }
//            }

            System.out.println(keyOrder);
        }

        return part == 1 ? wrongNumbers : multiple;
    }

    public static void main(String[] args) throws IOException {
        long count;
        //*
        count = new Day16().solve("2020/day16_test.txt", 1);
        System.out.println("Result: " + count);
        assert count == 71;

        count = new Day16().solve("2020/day16.txt", 1);
        System.out.println("Result: " + count);
        assert count == 26026;

        count = new Day16().solve("2020/day16_test2.txt", 2);
        System.out.println("Result: " + count);
        assert count == 1;

        count = new Day16().solve("2020/day16.txt", 2);
        System.out.println("Result: " + count);
        assert count == 1305243193339l;
        // wrong 291840
        // 27360
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
