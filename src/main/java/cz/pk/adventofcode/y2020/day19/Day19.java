package cz.pk.adventofcode.y2020.day19;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day19 {

    Map<Integer, Rule> rules;
    Map<Integer, Set<String>> ruleCache = new HashMap<>();
    long it = 0;

    List<String> puzzleInput;

    private static final int ITER = 3;

    static class Rule {
        int id;
        String finalText;
        List<List<Integer>> rules;

        @Override
        public String toString() {
            if (finalText != null) {
                return new StringBuilder()
                        .append("\"")
                        .append(finalText)
                        .append("\"")
                        .toString();
            } else {
                StringBuilder out = new StringBuilder();
                for (List<Integer> rule : rules) {
                    for (Integer part : rule) {
                        out.append(" ").append(part);
                    }
                    out.append(" |");
                }
                return out.toString();
            }
        }
    }

    private List<String> loadFile(String file) throws IOException {
        URL resource = getClass().getClassLoader().getResource(file);
        List<String> data = Files.readAllLines(Path.of(resource.getPath()));
        return data;
    }

    public Map<Integer, Rule> processGrammar(String file) throws IOException {
        Map<Integer, Rule> out = new HashMap<>();
        List<String> lines = loadFile(file);
        for (String line : lines) {
            String[] rule = line.split(":");
            assert rule.length == 2;
            int ruleId = Integer.valueOf(rule[0]);
            Rule myRule = new Rule();
            myRule.id = ruleId;
            List<List<Integer>> rulesOr = new ArrayList<>();
            for (String or : rule[1].trim().split("\\|")) {
                List<Integer> rulesAnd = new ArrayList<>();
                for (String match : or.trim().split(" ")) {
                    if (match.startsWith("\"")) {
                        myRule.finalText = match.replace("\"", "");
                    } else {
                        rulesAnd.add(Integer.valueOf(match));
                    }
                }
                rulesOr.add(rulesAnd);
            }
            if (myRule.finalText == null) {
                myRule.rules = rulesOr;
            }
            out.put(ruleId, myRule);
        }
        return out;
    }

    private Set<String> expandRule(Rule rule) {
        // max input is 100 - don't eval - max 13x
        it++;
        if (rule.finalText != null) {
            return Set.of(rule.finalText);
        } else if (ruleCache.containsKey(rule.id)) {
            return ruleCache.get(rule.id);
        } else if (rule.id == 8) {
            System.out.println("Expanding rule 8...");
            Set<String> words = expandRule(rules.get(42));
            for (int i = 0; i < ITER; i++) {
                Set<String> out = kartezian(words, words);
                Set<String> filtered = filterWords(out);
                if (filtered.isEmpty()) {
                    break;
                }
                words.addAll(filtered);
            }
            System.out.println("... rule 8 DONE");
            ruleCache.put(rule.id, words);
            return words;
        } else if (rule.id == 11) {
            System.out.println("Expanding rule 11...");
            Set<String> expand42 = expandRule(rules.get(42));
            Set<String> expand31 = expandRule(rules.get(31));
            Set<String> words = filterWords(kartezian(expand42, expand31));
            for (int i = 0; i < ITER; i++) {
                Set<String> part1 = filterWords(kartezian(expand42, words));
                Set<String> part2 = filterWords(kartezian(part1, expand31));
                if (part2.isEmpty()) {
                    break;
                }
                words.addAll(part2);
            }
            System.out.println("... rule 11 DONE");
            ruleCache.put(rule.id, words);
            return words;
        } else {
            Set<String> words = new HashSet<>();
            for (List<Integer> cond : rule.rules) {

                Set<String> items = new HashSet<>();
                for (Integer item : cond) {
                    Set<String> lowWords = expandRule(rules.get(item));
                    if (items.isEmpty()) {
                        items.addAll(lowWords);
                    } else {
                        Set<String> kartezianWords = new HashSet<>();
                        for (String prefix : items) {
                            for (String lowWord : lowWords) {
                                kartezianWords.add(prefix + lowWord);
                            }
                        }
                        items = kartezianWords;
                    }
                }

                words.addAll(items);
            }
            ruleCache.put(rule.id, words);
            return words;
        }
    }

    Set<String> filterWords(Set<String> in) {
        Set<String> out = new HashSet<>();
        for (String i : in) {
            boolean used = false;
            for (String p : puzzleInput) {
                if (p.contains(i)) {
                    used = true;
                    break;
                }
            }
            if (used) {
                out.add(i);
            }
        }
        System.out.println("Filtered from " + in.size() + " to " + out.size());
        return out;
    }

    Set<String> kartezian(Set<String> a, Set<String> b) {
        Set<String> out = new HashSet<>();
        for (String prefix : a) {
            for (String suffix : b) {
                out.add(prefix + suffix);
            }
        }
        return out;
    }

    public long count(String grammar, String file) throws IOException {
        rules = processGrammar(grammar);
        for (int i = 0; i < rules.size(); i++) {
            System.out.println(i + ": " + rules.get(i));
        }
        puzzleInput = loadFile(file);

        // generate all words from grammar
        Set<String> words = expandRule(rules.get(0));

        // check matching words
        int sum = 0;
        for (String input : puzzleInput) {
            if (words.contains(input)) {
                sum++;
            }
        }
        System.out.println("Iterations " + it); // 1480 --cache--> 506;
        System.out.println("42: " + ruleCache.get(42));
        System.out.println("31: " + ruleCache.get(31));

        return sum;
    }

    public static void main(String[] args) throws IOException {
        long count;

        //        count = new Day19().count("grammar_test.txt", "day19_test.txt");
        //        System.out.println("Result: " + count);
        //        assert count == 2;

        count = new Day19().count("grammar.txt", "day19.txt");
        System.out.println("Result: " + count);
        assert count == 332;    // ITER = 3
        // 330 too low [ITER = 2]
    }
}
