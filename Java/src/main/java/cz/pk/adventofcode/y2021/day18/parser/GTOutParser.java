// Generated from GTOut.g4 by ANTLR 4.0
package cz.pk.adventofcode.y2021.day18.parser;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class GTOutParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__2=1, T__1=2, T__0=3, NUMBER=4;
	public static final String[] tokenNames = {
		"<INVALID>", "'['", "','", "']'", "NUMBER"
	};
	public static final int
		RULE_snailfishNumber = 0, RULE_node = 1, RULE_open_bracket = 2, RULE_comma = 3, 
		RULE_close_bracket = 4;
	public static final String[] ruleNames = {
		"snailfishNumber", "node", "open_bracket", "comma", "close_bracket"
	};

	@Override
	public String getGrammarFileName() { return "GTOut.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public GTOutParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class SnailfishNumberContext extends ParserRuleContext {
		public List<NodeContext> node() {
			return getRuleContexts(NodeContext.class);
		}
		public CommaContext comma() {
			return getRuleContext(CommaContext.class,0);
		}
		public NodeContext node(int i) {
			return getRuleContext(NodeContext.class,i);
		}
		public Open_bracketContext open_bracket() {
			return getRuleContext(Open_bracketContext.class,0);
		}
		public Close_bracketContext close_bracket() {
			return getRuleContext(Close_bracketContext.class,0);
		}
		public TerminalNode EOF() { return getToken(GTOutParser.EOF, 0); }
		public SnailfishNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_snailfishNumber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GTOutListener ) ((GTOutListener)listener).enterSnailfishNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GTOutListener ) ((GTOutListener)listener).exitSnailfishNumber(this);
		}
	}

	public final SnailfishNumberContext snailfishNumber() throws RecognitionException {
		SnailfishNumberContext _localctx = new SnailfishNumberContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_snailfishNumber);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(10); open_bracket();
			setState(11); node();
			setState(12); comma();
			setState(13); node();
			setState(14); close_bracket();
			setState(15); match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NodeContext extends ParserRuleContext {
		public List<NodeContext> node() {
			return getRuleContexts(NodeContext.class);
		}
		public CommaContext comma() {
			return getRuleContext(CommaContext.class,0);
		}
		public TerminalNode NUMBER() { return getToken(GTOutParser.NUMBER, 0); }
		public NodeContext node(int i) {
			return getRuleContext(NodeContext.class,i);
		}
		public Open_bracketContext open_bracket() {
			return getRuleContext(Open_bracketContext.class,0);
		}
		public Close_bracketContext close_bracket() {
			return getRuleContext(Close_bracketContext.class,0);
		}
		public NodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_node; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GTOutListener ) ((GTOutListener)listener).enterNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GTOutListener ) ((GTOutListener)listener).exitNode(this);
		}
	}

	public final NodeContext node() throws RecognitionException {
		NodeContext _localctx = new NodeContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_node);
		try {
			setState(24);
			switch (_input.LA(1)) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(17); open_bracket();
				setState(18); node();
				setState(19); comma();
				setState(20); node();
				setState(21); close_bracket();
				}
				break;
			case NUMBER:
				enterOuterAlt(_localctx, 2);
				{
				setState(23); match(NUMBER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Open_bracketContext extends ParserRuleContext {
		public Open_bracketContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_open_bracket; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GTOutListener ) ((GTOutListener)listener).enterOpen_bracket(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GTOutListener ) ((GTOutListener)listener).exitOpen_bracket(this);
		}
	}

	public final Open_bracketContext open_bracket() throws RecognitionException {
		Open_bracketContext _localctx = new Open_bracketContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_open_bracket);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26); match(1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CommaContext extends ParserRuleContext {
		public CommaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comma; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GTOutListener ) ((GTOutListener)listener).enterComma(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GTOutListener ) ((GTOutListener)listener).exitComma(this);
		}
	}

	public final CommaContext comma() throws RecognitionException {
		CommaContext _localctx = new CommaContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_comma);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(28); match(2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Close_bracketContext extends ParserRuleContext {
		public Close_bracketContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_close_bracket; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GTOutListener ) ((GTOutListener)listener).enterClose_bracket(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GTOutListener ) ((GTOutListener)listener).exitClose_bracket(this);
		}
	}

	public final Close_bracketContext close_bracket() throws RecognitionException {
		Close_bracketContext _localctx = new Close_bracketContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_close_bracket);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(30); match(3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\2\3\6#\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\33\n\3\3\4\3\4\3\5\3\5\3\6\3\6"+
		"\3\6\2\7\2\4\6\b\n\2\2\36\2\f\3\2\2\2\4\32\3\2\2\2\6\34\3\2\2\2\b\36\3"+
		"\2\2\2\n \3\2\2\2\f\r\5\6\4\2\r\16\5\4\3\2\16\17\5\b\5\2\17\20\5\4\3\2"+
		"\20\21\5\n\6\2\21\22\7\1\2\2\22\3\3\2\2\2\23\24\5\6\4\2\24\25\5\4\3\2"+
		"\25\26\5\b\5\2\26\27\5\4\3\2\27\30\5\n\6\2\30\33\3\2\2\2\31\33\7\6\2\2"+
		"\32\23\3\2\2\2\32\31\3\2\2\2\33\5\3\2\2\2\34\35\7\3\2\2\35\7\3\2\2\2\36"+
		"\37\7\4\2\2\37\t\3\2\2\2 !\7\5\2\2!\13\3\2\2\2\3\32";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}