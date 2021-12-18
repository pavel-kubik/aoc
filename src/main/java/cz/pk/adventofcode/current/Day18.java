package cz.pk.adventofcode.current;

import java.util.BitSet;
import java.util.List;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTree;

import cz.pk.adventofcode.current.parser.GTOutLexer;
import cz.pk.adventofcode.current.parser.GTOutParser;
import cz.pk.adventofcode.util.StringCollector;
import lombok.Data;

@Data
public class Day18 implements ANTLRErrorListener {

    private final boolean debug;

    @Data
    private static class SnailfishNumber {
        private SnailfishNumber left;
        private SnailfishNumber right;
        private SnailfishNumber parent;

        private Integer value;

        public SnailfishNumber(SnailfishNumber parent) {
            this.parent = parent;
        }

        public SnailfishNumber(SnailfishNumber parent, Integer value) {
            this.parent = parent;
            this.value = value;
        }

        public int getDeep() {
            return value != null ? 0 : Math.max(left.getDeep(), right.getDeep()) + 1;
        }

        public boolean isRight() {
            if (this.getParent() == null) {
                return false;
            }
            return this.getParent().getRight() == this;
        }

        public boolean isLeft() {
            if (this.getParent() == null) {
                return false;
            }
            return this.getParent().getLeft() == this;
        }

        public void addValueRight(int value) {
            if (this.value != null) {
                this.value += value;
            } else {
                getRight().addValueRight(value);
            }
        }

        public void addValueLeft(int value) {
            if (this.value != null) {
                this.value += value;
            } else {
                getLeft().addValueLeft(value);
            }
        }

        public SnailfishNumber getTooDeep(int deep) {
            if (value == null) {
                if (deep == 0 && getDeep() == 1) {
                    return this;
                }
                SnailfishNumber leftDeep = left.getTooDeep(deep - 1);
                SnailfishNumber rightDeep = right.getTooDeep(deep - 1);
                if (leftDeep != null) {
                    return leftDeep;
                }
                if (rightDeep != null) {
                    return rightDeep;
                }

            }
            return null;
        }

        public SnailfishNumber getHigherThan(int threshold) {
            if (this.value != null) {
                if (this.value >= threshold) {
                    return this;
                }
            } else {
                SnailfishNumber leftHT = this.getLeft().getHigherThan(threshold);
                if (leftHT != null) {
                    return leftHT;
                }
                SnailfishNumber rightHT = this.getRight().getHigherThan(threshold);
                if (rightHT != null) {
                    return rightHT;
                }
            }
            return null;
        }

        public boolean contain10AndMore() {
            return value != null ? value >= 10 : left.contain10AndMore() || right.contain10AndMore();
        }

        public long getMagnitude() {
            if (this.value != null) {
                return this.value;
            } else {
                return 3*left.getMagnitude()+2*right.getMagnitude();
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (value != null) {
                sb.append(this.value);
            } else {
                sb
                        .append("[")
                        .append(this.left.toString())
                        .append(",")
                        .append(this.right.toString())
                        .append("]");
            }
            return sb.toString();
        }
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
        return parseTree(null, tree);
    }

    private SnailfishNumber parseTree(SnailfishNumber parent, ParseTree tree) {
        if (tree.getChildCount() >= 5) {
            SnailfishNumber newNode = new SnailfishNumber(parent);
            newNode.setLeft(parseTree(newNode, tree.getChild(1)));
            newNode.setRight(parseTree(newNode, tree.getChild(3)));
            return newNode;
        } else {
            return new SnailfishNumber(parent, Integer.valueOf(tree.getText()));
        }
    }

    private SnailfishNumber add(SnailfishNumber a, SnailfishNumber b) {
        SnailfishNumber newNumber = new SnailfishNumber(null);
        newNumber.setLeft(a);
        newNumber.setRight(b);
        a.setParent(newNumber);
        b.setParent(newNumber);
        return newNumber;
    }

    private SnailfishNumber explode(SnailfishNumber a) {
        SnailfishNumber firstTooDeep = a.getTooDeep(4); //TODO debug this magic constant
        Integer left = firstTooDeep.getLeft().getValue();
        Integer right = firstTooDeep.getRight().getValue();
        // null node
        firstTooDeep.setValue(0);
        firstTooDeep.setLeft(null);
        firstTooDeep.setRight(null);
        // transfer left value
        SnailfishNumber leftTarget = firstTooDeep;
        while (leftTarget.getParent() != null) {
            if (leftTarget.isRight()) {
                leftTarget.getParent().getLeft().addValueRight(left);
                break;
            } else {
                leftTarget = leftTarget.getParent();
            }
        }
        // transfer right value
        SnailfishNumber rightTarget = firstTooDeep;
        while (rightTarget.getParent() != null) {
            if (rightTarget.isLeft()) {
                rightTarget.getParent().getRight().addValueLeft(right);
                break;
            } else {
                rightTarget = rightTarget.getParent();
            }
        }
        return a;
    }

    private void split(SnailfishNumber a) {
        SnailfishNumber hT10 = a.getHigherThan(10);
        int left = (int)Math.floor(hT10.getValue() / 2.0);
        int right = (int)Math.ceil(hT10.getValue() / 2.0);
        hT10.setLeft(new SnailfishNumber(hT10, left));
        hT10.setRight(new SnailfishNumber(hT10, right));
        hT10.setValue(null);
    }

    private SnailfishNumber reduce(SnailfishNumber a) {
        while (a.getDeep() > 4 || a.contain10AndMore()) {
            if (a.getDeep() > 4) {
                explode(a);
            } else if (a.contain10AndMore()) {
                split(a);
            }
        }
        return a;
    }

    public long solve(String file) {
        assert explode(parseLine("[[[[[9,8],1],2],3],4]")).toString().equals("[[[[0,9],2],3],4]");
        assert explode(parseLine("[7,[6,[5,[4,[3,2]]]]]")).toString().equals("[7,[6,[5,[7,0]]]]");
        assert explode(parseLine("[[6,[5,[4,[3,2]]]],1]")).toString().equals("[[6,[5,[7,0]]],3]");
        assert explode(parseLine("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]")).toString().equals("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]");
        assert explode(parseLine("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")).toString().equals("[[3,[2,[8,0]]],[9,[5,[7,0]]]]");

        List<String> lines = new StringCollector(file).process();

        SnailfishNumber out = null;
        for (String line : lines) {
            SnailfishNumber number = parseLine(line);
            assert number  != null;
            if (out == null) {
                out = number;
            } else {
                assert out != null;
                out = add(out, number);
                reduce(out);
            }
        }

        System.out.println(out);
        return out.getMagnitude();
    }

    public long solve2(String file) {
        assert parseLine("[[1,2],[[3,4],5]]").getMagnitude() == 143;
        assert parseLine("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]").getMagnitude() == 1384;
        assert parseLine("[[[[1,1],[2,2]],[3,3]],[4,4]]").getMagnitude() == 445;
        assert parseLine("[[[[3,0],[5,3]],[4,4]],[5,5]]").getMagnitude() == 791;
        assert parseLine("[[[[5,0],[7,4]],[5,5]],[6,6]]").getMagnitude() == 1137;
        assert parseLine("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]").getMagnitude() == 3488;
        List<String> lines = new StringCollector(file).process();

        long highestMagnitude = 0;
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.size(); j++) {
                if (i == j) {
                    continue;
                }
                long currentMag = reduce(add(parseLine(lines.get(i)), parseLine(lines.get(j)))).getMagnitude();
                if (currentMag > highestMagnitude) {
                    highestMagnitude = currentMag;
                }
            }
        }

        return highestMagnitude;
    }

    public static void main(String[] args) {
        System.out.println(Day18.class);
        long count;
        //*
        count = new Day18(true).solve("day18_test.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 4140;

        count = new Day18(true).solve("day18_test2.txt");
        System.out.println("Result: " + count);
        // add vm option -ea to run configuration to throw exception on assert
        assert count == 3488;

        count = new Day18(true).solve("day18.txt");
        System.out.println("Result: " + count);
        assert count == 3806;

        //*/

        count = new Day18(true).solve2("day18_test.txt");
        System.out.println("Result: " + count);
        assert count == 3993;

        count = new Day18(true).solve2("day18.txt");
        System.out.println("Result: " + count);
        assert count == 4727; // < 4796
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
