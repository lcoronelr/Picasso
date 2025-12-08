package picasso.parser;

import java.util.Stack;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.StringValue;
import picasso.parser.tokens.StringToken;
import picasso.parser.tokens.Token;

/**
 * Analyzer for string literals (image filenames)
 * 
 * @author Luis Coronel
 */
public class StringAnalyzer implements SemanticAnalyzerInterface {

	@Override
	public ExpressionTreeNode generateExpressionTree(Stack<Token> tokens) {
		StringToken token = (StringToken) tokens.pop();
		return new StringValue(token.getValue());
	}
}