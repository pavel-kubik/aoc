package cz.pk.adventofcode.current;

import java.util.*;

import cz.pk.adventofcode.current.parser.GTOutLexer;
import cz.pk.adventofcode.current.parser.GTOutParser;
import cz.pk.adventofcode.util.StringCollector;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTree;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Data
public class Day18 implements ANTLRErrorListener {

    private final boolean debug;

    @Data
    @NoArgsConstructor
    private static class SnailfishNumber {
        private SnailfishNumber left;
        private SnailfishNumber right;

        private Integer value;
    }

    private SnailfishNumber parseLine(String line) {
        ANTLRInputStream input = new ANTLRInputStream(line);

        GTOutLexer lexer = new GTOutLexer(input);
        //lexer.removeErrorListeners();
        lexer.addErrorListener(this);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        //tokens.fill();
        GTOutParser parser = new GTOutParser(tokens);
        //parser.removeErrorListeners();
        parser.addErrorListener(this);
        ParseTree tree = parser.snailfishNumber();
        return null;
    }

    public long solve(String file) {
        List<String> lines = new StringCollector(file).process();

        for (String line : lines) {
            parseLine(line);
        }


        System.out.println(lines);
        return lines.size();
    }

    public long solve2(String file) {
        List<Integer> data = stream(new StringCollector(file)
                .process().get(0).split(",")).map(Integer::valueOf).collect(toList());
        System.out.println(data);
        return data.get(0)+22;
    }

    public static void main(String[] args) {
        System.out.println(Day18.class);
        long count;
        //*
        count = new Day18(true).solve("day18_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 11;

        count = new Day18(true).solve("day18.txt");
        System.out.println("Result: " + count);
        assert count == 22;

        //*/

        count = new Day18(true).solve2("day18_test.txt");
        System.out.println("Result: " + count);
        assert count == 33;

        count = new Day18(true).solve2("day18.txt");
        System.out.println("Result: " + count);
        assert count == 44;
        //*/
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object o, int i, int i1, String s, RecognitionException e) {
        throw new RuntimeException("syntaxError", e);
    }

    @Override
    public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, BitSet bitSet, ATNConfigSet atnConfigSet) {
        throw new RuntimeException("reportAmbiguity");
    }

    @Override
    public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, ATNConfigSet atnConfigSet) {
        throw new RuntimeException("reportAttemptingFullContext");
    }

    @Override
    public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, ATNConfigSet atnConfigSet) {
        throw new RuntimeException("reportContextSensitivity");
    }

}
