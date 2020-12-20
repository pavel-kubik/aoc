package cz.pk.adventofcode.y2020.day19;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.BitSet;
import java.util.List;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import cz.pk.adventofcode.y2020.day19.genTest.GTOutLexer;
import cz.pk.adventofcode.y2020.day19.genTest.GTOutParser;
import lombok.Data;

@Data
public class Day19ANTLR implements ANTLRErrorListener {

    int error;
    int sum;

    public long solve(String file) throws IOException, InterruptedException {
        URL resource = getClass().getClassLoader().getResource(file);
        List<String> data = Files.readAllLines(Path.of(resource.getPath()));

        sum = 0;
        for (String line : data) {
            System.out.println(line);
            error = 0;
            ANTLRInputStream input = new ANTLRInputStream(line);

            GTOutLexer lexer = new GTOutLexer(input);
            //lexer.removeErrorListeners();
            lexer.addErrorListener(this);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            //tokens.fill();
            GTOutParser parser = new GTOutParser(tokens);
            //parser.removeErrorListeners();
            parser.addErrorListener(this);
            ParseTree tree = parser.rule0();

            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(new ParseTreeListener() {
                @Override
                public void visitTerminal(TerminalNode terminalNode) {
                }
                @Override
                public void visitErrorNode(ErrorNode errorNode) {
                    error++;
                }
                @Override
                public void enterEveryRule(ParserRuleContext parserRuleContext) {
                }
                @Override
                public void exitEveryRule(ParserRuleContext parserRuleContext) {
                }
            }, tree);

            //Thread.sleep(500);
            if (error == 0) {
                sum++;
            }
        }
        return sum;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        long count;
        //*
//        count = new Day19().solve("day19_test.txt");
//        System.out.println("Result: " + count);
//        assert count == 2;

        count = new Day19ANTLR().solve("2020/day19.txt");
        System.out.println("Result: " + count);
        //assert count == 845;
        // low 103
        // high 156 hmm 157 (?) WTF
        // not 125 :D

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

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object o, int i, int i1, String s, RecognitionException e) {
        error++;
    }

    @Override
    public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, BitSet bitSet, ATNConfigSet atnConfigSet) {
        error++;
    }

    @Override
    public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, ATNConfigSet atnConfigSet) {
        error++;
    }

    @Override
    public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, ATNConfigSet atnConfigSet) {
        error++;
    }
}
