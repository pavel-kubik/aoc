package cz.pk.adventofcode.y2020.day7;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import cz.pk.adventofcode.util.DataCollector;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import static cz.pk.adventofcode.util.StringUtil.removeSuffix;

public class Day7 {

    @Data
    @RequiredArgsConstructor
    class Rule {
        private final String bag;
        private final List<String> content;
        private final List<Integer> count;
    }

    class ParseRules extends DataCollector<Rule> {
        public ParseRules(String file) throws IOException {
            super(file);
        }

        @Override
        protected Rule processLine(String line) {
            String[] rule = line.split("contain");
            assert rule.length == 2;
            String bag = removeSuffix(rule[0].trim(), " bags");
            List<String> bags = new ArrayList<>();
            List<Integer> bagCounts = new ArrayList<>();
            if (!rule[1].trim().equals("no other bags.")) {
                String[] contentBags = rule[1].substring(0, rule[1].length() - 1).split(",");
                for (int i=0;i<contentBags.length;i++) {
                    String[] contentParsed = contentBags[i].trim().split(" ");
                    String count = contentParsed[0];
                    StringBuilder bagColor = new StringBuilder();
                    for (int j=1;j<contentParsed.length - 1;j++) {
                        bagColor.append(contentParsed[j]);
                        bagColor.append(' ');
                    }
                    bags.add(bagColor.toString().trim());
                    bagCounts.add(Integer.valueOf(count));
                }
            }
            System.out.println(bag + " => " + bags + bagCounts);
            return new Rule(bag, bags, bagCounts);
        }
    }

    public int countVariants(String bagColor) throws IOException {
        List<Rule> rules = new ParseRules("2020/day7.txt").process();
        Set<String> holdingBags = new HashSet<>();
        for (Rule rule : rules) {
            if (rule.getContent().contains(bagColor)) {
                holdingBags.add(rule.getBag());
            }
        }
        int size;
        do {
            size = holdingBags.size();
            Set<String> newHoldingBags = new HashSet<>();
            for (String bag : holdingBags) {
                for (Rule rule : rules) {
                    if (rule.getContent().contains(bag)) {
                        newHoldingBags.add(rule.getBag());
                    }
                }
            }
            holdingBags.addAll(newHoldingBags);
        } while (size != holdingBags.size());
        return holdingBags.size();
    }

    public int countContent(String bagColor) throws IOException {
        List<Rule> rules = new ParseRules("2020/day7.txt").process();
        Map<String, Rule> rulesByBag = new HashMap<>();
        for (Rule rule : rules) {
            rulesByBag.put(rule.getBag(), rule);
        }
        return nestedBags(bagColor, rulesByBag) - 1;
    }

    int nestedBags(String bag, Map<String, Rule> rulesByBag) {
        Rule rule = rulesByBag.get(bag);
        int bags = 1;
        for (int i=0;i<rule.getContent().size();i++) {
            bags += rule.getCount().get(i)*nestedBags(rule.getContent().get(i), rulesByBag);
        }
        return bags;
    }

    public static void main(String[] args) throws IOException {
        //System.out.println(new Day7().countVariants("shiny gold")); //177
        System.out.println(new Day7().countContent("shiny gold"));  // 127 - test, 34988
    }
}
