// Generated from GTOut.g4 by ANTLR 4.0
package cz.pk.adventofcode.current.parser;

import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Token;

public interface GTOutListener extends ParseTreeListener {
	void enterNode(GTOutParser.NodeContext ctx);
	void exitNode(GTOutParser.NodeContext ctx);

	void enterComma(GTOutParser.CommaContext ctx);
	void exitComma(GTOutParser.CommaContext ctx);

	void enterOpen_bracket(GTOutParser.Open_bracketContext ctx);
	void exitOpen_bracket(GTOutParser.Open_bracketContext ctx);

	void enterClose_bracket(GTOutParser.Close_bracketContext ctx);
	void exitClose_bracket(GTOutParser.Close_bracketContext ctx);

	void enterSnailfishNumber(GTOutParser.SnailfishNumberContext ctx);
	void exitSnailfishNumber(GTOutParser.SnailfishNumberContext ctx);
}