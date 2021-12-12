package cz.pk.adventofcode.y2020.day23;

import cz.pk.adventofcode.util.CodeTimer;
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
        return last;
    }

    /**
     * I know, circle has no start and end, but this is pointer to one special Node in circle
     */
    private Node switchCircleStart(Node circle, Node from, int count) {
        Node lastRemoved = from.getNext();
        boolean moveCircleHead = lastRemoved == circle;
        for (int i = 0; i < count - 1; i++) {
            lastRemoved = lastRemoved.getNext();
            if (lastRemoved == circle) {
                moveCircleHead = true;
            }
        }
        return moveCircleHead ? lastRemoved.getNext() : circle;
    }

    private Node removeCups(Node from, int count) {
        Node firstRemoved = from.getNext();
        Node lastRemoved = firstRemoved;
        for (int i = 0; i < count - 1; i++) {
            lastRemoved = lastRemoved.getNext();
        }
        from.setNext(lastRemoved.getNext());
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
        return printCups(circle, null);
    }

    private String printCups(Node circle, Node currentCup) {
        StringBuilder out = new StringBuilder();
        Node current = circle;
        do {
            if (current == currentCup) {
                out.append("(");
            }
            out.append(current.getValue());
            if (current == currentCup) {
                out.append(")");
            }
            out.append(" ");
            current = current.getNext();
        } while (current != null && current != circle);
        return out.toString();
    }

    private long finalCups(Node circle) {
        StringBuilder out = new StringBuilder();
        Node current = circle;
        do {
            if (current.getValue() != 1) {
                out.append(current.getValue());
            }
            current = current.getNext();
        } while (current != circle);
        return Long.parseLong(out.toString());
    }

    private Node findNodeBeforeValue(Node circle, int value) {
        Node current = circle;
        do {
            if (current.getNext().getValue() == value) {
                return current;
            }
            current = current.getNext();
        } while (current != circle);
        return null;
    }

    private Node findNodeByValue(Node circle, int value) {
        Node current = circle;
        do {
            if (current.getValue() == value) {
                return current;
            }
            current = current.getNext();
        } while (current != circle);
        return null;
    }

    private Node findDestination(Node circle, int value, int maxValue) {
        Node target = null;
        do {
            if (value-- < 0) {
                value = maxValue;
            };
            target = findNodeByValue(circle, value);
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
        Node circle = buildCircle(cupValues);   // pointer at last for 2nd part
        Node currentCup = circle.getNext(); // start from first
        for (int i = 0; i < 100; i++) {
            System.out.println("-- move " + (i+1) + " --");
            System.out.println("cups:" + printCups(circle, currentCup));
            circle = switchCircleStart(circle, currentCup, 3);
            Node removed = removeCups(currentCup, 3);
            System.out.println("pick up: " + printCups(removed));
            System.out.println("remaining: " + printCups(circle));
            System.out.println("destination: " + (currentCup.getValue() - 1));

            Node destination = findDestination(circle, currentCup.getValue(), 9);

            addCups(destination, removed);

            currentCup = currentCup.getNext();
        }
        System.out.println(printCups(circle, currentCup));
        Node first = findNodeByValue(circle, 1);

        System.out.println("-- final --\n" + printCups(first));

        return finalCups(first);
    }

    public long solve2(String file) {
        int MAX_CUPS  =  1000000;
        int STEPS     = 10000000;
        int ITERATION =   100000;
        List<Integer> cupValues = new StringCollector(file).process()
                .get(0).chars()
                .mapToObj(c -> (char)c)
                .map(c -> Integer.parseInt(c.toString()))
                .collect(toList());
        //add missing cups
        for (int i = cupValues.size(); i <= MAX_CUPS; i++) {
            cupValues.add(i);
        }
        System.out.println("10M cups generated");

        // build circle
        Collections.reverse(cupValues);
        Node circle = buildCircle(cupValues);   // pointer at last for 2nd part
        Node currentCup = circle.getNext(); // start from first
        System.out.println("10M cups sorted in circle");
        CodeTimer ct = new CodeTimer();
        for (int i = 0; i < STEPS; i++) {
            if (i % ITERATION == 0) {
                System.out.println("Step " + (i/ITERATION) + "/" + (STEPS/ITERATION));
                ct.log();
            }
            //System.out.println("-- move " + (i+1) + " --");
            //System.out.println("cups:" + printCups(circle, currentCup));
            ct.start("switchCircleStart");
            circle = switchCircleStart(circle, currentCup, 3);
            ct.start("removeCups");
            Node removed = removeCups(currentCup, 3);
            //System.out.println("pick up: " + printCups(removed));
            //System.out.println("remaining: " + printCups(circle));
            //System.out.println("destination: " + (currentCup.getValue() - 1));

            ct.start("findDestination");
            Node destination = findDestination(circle, currentCup.getValue(), MAX_CUPS);

            ct.start("addCups");
            addCups(destination, removed);
            ct.stop();

            currentCup = currentCup.getNext();
        }
        //System.out.println(printCups(circle, currentCup));
        Node first = findNodeByValue(circle, 1);

        //System.out.println("-- final --\n" + printCups(first));

        int a = first.getNext().getValue();
        int b = first.getNext().getNext().getValue();
        System.out.println("A: " + a);
        System.out.println("B: " + b);
        return a*b;
    }

    public static void main(String[] args) {
        System.out.println(Day23.class);
        long count;
        //*
        count = new Day23(true).solve("2020/day23_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 67384529;

        count = new Day23(true).solve("2020/day23.txt");
        System.out.println("Result: " + count);
        assert count == 68245739;

        //*/

        count = new Day23(true).solve2("2020/day23_test.txt");
        System.out.println("Result: " + count);
        assert count == 33;

        count = new Day23(true).solve2("2020/day23.txt");
        System.out.println("Result: " + count);
        assert count == 44;
        //*/
    }
}
