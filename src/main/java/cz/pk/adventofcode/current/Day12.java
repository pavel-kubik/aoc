package cz.pk.adventofcode.current;

import cz.pk.adventofcode.util.DataCollector;
import cz.pk.adventofcode.util.StringCollector;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day12 {

    private final boolean debug;

    @Data
    @AllArgsConstructor
    class Connection {
        String from;
        String to;
    }

    class ConnectionCollector extends DataCollector<Connection> {

        public ConnectionCollector(String file) {
            super(file);
        }

        @Override
        protected Connection processLine(String line) {
            String[] pathPoints =  line.split("-");
            return new Connection(pathPoints[0], pathPoints[1]);
        }
    }

    void generatePath(String startPoint,
                      Map<String, Set<String>> nodes,
                      Set<String> visited,
                      List<String> path,
                      List<String> paths) {
        if (startPoint.equals("end")) {
            System.out.println("END");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < path.size(); i++) {
                if (i != 0) {
                    sb.append(",");
                }
                sb.append(path.get(i));
            }
            paths.add(sb.toString());
            return;
        }
        for (String node : nodes.getOrDefault(startPoint, Set.of())) {
            if (node.toLowerCase().equals(node) && visited.contains(node)) {
                continue;
            }
            System.out.println(startPoint + " -> " + node);
            visited.add(node);
            path.add(node);
            generatePath(node, nodes, visited, path, paths);
            visited.remove(node);
            path.remove(node);
        }
    }

    public long solve(String file) {
        List<Connection> connections = new ConnectionCollector(file).process();

        // build nodes map
        Map<String, Set<String>> nodes = new HashMap<>();
        for (Connection connection : connections) {
            nodes.compute(connection.from, (key, value) -> {
                if (value == null) {
                    value = new HashSet<>();
                }
                value.add(connection.to);
                return value;
            });

            nodes.compute(connection.to, (key, value) -> {
                if (value == null) {
                    value = new HashSet<>();
                }
                value.add(connection.from);
                return value;
            });
        }

        // generate all paths
        List<String> allPaths = new ArrayList<>();
        Set<String> visitedNodes = new HashSet<>();
        visitedNodes.add("start");
        List<String> currentPath = new ArrayList<>();
        currentPath.add("start");
        generatePath("start", nodes, visitedNodes, currentPath, allPaths);

        System.out.println(allPaths);

        return allPaths.size();
    }

    void generatePath2(String startPoint,
                       Map<String, Set<String>> nodes,
                       Set<String> visited,
                       Set<String> visitedTwice,
                       List<String> path,
                       List<String> paths) {
        if (path.size() > 2*nodes.size()) {
            return;
        }
        // end -> generate path
        if (startPoint.equals("end")) {
            System.out.print("END (" + paths.size()+") ");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < path.size(); i++) {
                if (i != 0) {
                    sb.append(",");
                }
                sb.append(path.get(i));
            }
            paths.add(sb.toString());
            System.out.println(sb.toString());
            return;
        }
        // iterate nodes
        for (String node : nodes.getOrDefault(startPoint, Set.of())) {
            if (node.toLowerCase().equals(node) && visited.contains(node)) {
                if (!visitedTwice.isEmpty() || node.equals("start")) {
                    continue;
                }
                visitedTwice.add(node);
            }
            System.out.println(startPoint + " -> " + node);
            visited.add(node);
            path.add(node);
            generatePath2(node, nodes, visited, visitedTwice, path, paths);
            System.out.println("UP " + node);
            if (visitedTwice.contains(node)) {
                visitedTwice.remove(node);
            } else {
                visited.remove(node);
            }
            path.remove(path.size()-1); // remove last added node
        }
    }

    public long solve2(String file) {
        List<Connection> connections = new ConnectionCollector(file).process();

        // build nodes map
        Map<String, Set<String>> nodes = new HashMap<>();
        for (Connection connection : connections) {
            nodes.compute(connection.from, (key, value) -> {
                if (value == null) {
                    value = new HashSet<>();
                }
                value.add(connection.to);
                return value;
            });

            nodes.compute(connection.to, (key, value) -> {
                if (value == null) {
                    value = new HashSet<>();
                }
                value.add(connection.from);
                return value;
            });
        }

        // generate all paths
        List<String> allPaths = new ArrayList<>();
        Set<String> visitedNodes = new HashSet<>();
        visitedNodes.add("start");
        List<String> currentPath = new ArrayList<>();
        currentPath.add("start");
        Set<String> visitedTwice = new HashSet<>();
        generatePath2("start", nodes, visitedNodes, visitedTwice, currentPath, allPaths);

        System.out.println(allPaths);

        return allPaths.size();
    }

    public static void main(String[] args) {
        System.out.println(Day12.class);
        long count;
        //*
        count = new Day12(true).solve("day12_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 10;

        count = new Day12(true).solve("day12_test2.txt");
        System.out.println("Result: " + count);
        assert count == 19;

        count = new Day12(true).solve("day12_test3.txt");
        System.out.println("Result: " + count);
        assert count == 226;


        count = new Day12(true).solve("day12.txt");
        System.out.println("Result: " + count);
        assert count == 4707;

        //*/

        count = new Day12(true).solve2("day12_test.txt");
        System.out.println("Result: " + count);
        assert count == 36;

        count = new Day12(true).solve2("day12_test2.txt");
        System.out.println("Result: " + count);
        assert count == 103;

        count = new Day12(true).solve2("day12_test3.txt");
        System.out.println("Result: " + count);
        assert count == 3509;

        count = new Day12(true).solve2("day12.txt");
        System.out.println("Result: " + count);
        assert count == 130493;
        //*/
    }
}
