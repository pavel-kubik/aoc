package cz.pk.adventofcode.y2020.day6;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cz.pk.adventofcode.util.GroupProcessor;

public class Day6 {

    class UniqueCharGroupProcessor extends GroupProcessor {
        public UniqueCharGroupProcessor(String file) throws IOException {
            super(file);
        }

        @Override
        protected int processGroup(List<String> groupLines) {
            Set<Character> answers = new HashSet<>();
            for(String line : groupLines) {
                for (int i=0;i<line.length();i++) {
                    answers.add(line.charAt(i));
                }
            }
            System.out.println(answers.size());
            return answers.size();
        }
    }

    class CommonCharGroupProcessor extends GroupProcessor {
        public CommonCharGroupProcessor(String file) throws IOException {
            super(file);
        }

        @Override
        protected int processGroup(List<String> groupLines) {
            Set<Character> commonAnswers = new HashSet<>();
            String firstLine = groupLines.get(0);
            for (int i=0;i<firstLine.length();i++) {
                commonAnswers.add(firstLine.charAt(i));
            }
            for(int i=1;i<groupLines.size();i++) {
                String line = groupLines.get(i);
                Set<Character> answers = new HashSet<>();
                for (int j=0;j<line.length();j++) {
                    answers.add(line.charAt(j));
                }
                commonAnswers = commonAnswers.stream().filter(item -> answers.contains(item)).collect(Collectors.toSet());
            }
            System.out.println(commonAnswers.size());
            return commonAnswers.size();
        }
    }

    int sumGroups() throws IOException {
        return new CommonCharGroupProcessor("2020/day6.txt").processGroups();
    }

    public static void main(String[] args) throws IOException {
        int sum = new Day6().sumGroups();
        System.out.println("Sum: " + sum);
    }
}
