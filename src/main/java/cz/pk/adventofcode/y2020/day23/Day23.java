package cz.pk.adventofcode.y2020.day23;

import cz.pk.adventofcode.util.StringCollector;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.*;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day23 {

    private final boolean debug;

    @Data
    @RequiredArgsConstructor
    private class Node {
        private final int value;
        private Node next;
        //TODO wtf
        //private Node previous;

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    '}';
        }
    }

    private Node buildCircle(List<Integer> values) {
        Node last = null;
        Node previous = null;
        Node current = null;
        Node first = null;
        for (Integer value : values){
            current = new Node(value);
            if (last == null) {
                last = current;
            }
            current.setNext(previous);
            previous = current;
        }
        first = current;
        last.setNext(first);
        return first;
    }

    private Node removeCups(Node circle, int count) {
        Node firstRemoved = circle.getNext();
        Node lastRemoved = firstRemoved;
        for (int i = 0; i < count - 1; i++) {
            lastRemoved = lastRemoved.getNext();
        }
        circle.setNext(lastRemoved.getNext());
        lastRemoved.setNext(null);
        return firstRemoved;
    }

    private void addCups(Node add, Node newNodes) {
        Node lastNewNodes = newNodes;
        while (lastNewNodes.getNext() != null) {
            lastNewNodes = lastNewNodes.getNext();
        }

        Node connectTo = add.getNext();
        add.setNext(newNodes);
        lastNewNodes.setNext(connectTo);
    }

    private String printCups(Node circle) {
        StringBuilder out = new StringBuilder();
        Node current = circle;
        do {
            out.append(current.getValue()).append(" ");
            current = current.getNext();
        } while (current != null && current != circle);
        return out.toString();
    }

    private long cups2Long(Node circle) {
        StringBuilder out = new StringBuilder();
        Node current = circle;
        do {
            out.append(current.getValue());
            current = current.getNext();
        } while (current != circle);
        return Long.parseLong(out.toString());
    }

    private Node findNodeBeforeValue(Node circle, int value) {
        Node current = circle;
        do {
            if (current.getValue() == value) {
                return current;
            }
            current = current.getNext();
        } while (current != circle);
        return null;
    }

    private Node findDestination(Node circle, int value) {
        Node target = null;
        do {
            if (value-- < 0) {
                value = 9;
            };
            target = findNodeBeforeValue(circle, value);
        } while (target == null);

        return target;
    }

    public long solve(String file) {
        List<Integer> cupValues = new StringCollector(file).process()
                .get(0).chars()
                .mapToObj(c -> (char)c)
                .map(c -> Integer.parseInt(c.toString()))
                .collect(toList());

        // build circle
        Collections.reverse(cupValues);
        Node circle = buildCircle(cupValues);
        System.out.println(printCups(circle));
        for (int i = 0; i < 10; i++) {
            Node removed = removeCups(circle, 3);
            System.out.println("Removed: " + printCups(removed));
            System.out.println("Remaining circle: " + printCups(circle));

            Node destination = findDestination(circle, removed.getValue());

            // wrong - should find cup with label
            addCups(destination, removed);

            circle = circle.getNext();
            System.out.println(printCups(circle));
        }

        return cups2Long(circle);
    }

    public long solve2(String file) {
        List<Integer> data = stream(new StringCollector(file)
                .process().get(0).split(",")).map(Integer::valueOf).collect(toList());
        System.out.println(data);
        return data.get(0)+22;
    }

    public static void main(String[] args) {
        System.out.println(Day23.class);
        long count;
        //*
        count = new Day23(true).solve("2020/day23_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 11;

        count = new Day23(true).solve("2020/day23.txt");
        System.out.println("Result: " + count);
        assert count == 22;

        //*/

        count = new Day23(true).solve2("2020/day23_test.txt");
        System.out.println("Result: " + count);
        assert count == 33;

        count = new Day23(true).solve2("2020/day23.txt");
        System.out.println("Result: " + count);
        assert count == 44;
        //*/

        assert "Don't forgot to set vm option -ea".isEmpty();
    }
}
