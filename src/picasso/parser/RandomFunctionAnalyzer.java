package picasso.parser;

import java.util.Stack;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.RandomFunction;
import picasso.parser.language.expressions.StringValue;
import picasso.parser.tokens.StringToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.functions.RandomFunctionToken;

public class RandomFunctionAnalyzer implements SemanticAnalyzerInterface {

	@Override
	public ExpressionTreeNode generateExpressionTree(Stack<Token> tokens) {
		RandomFunctionToken token = (RandomFunctionToken) tokens.pop();
		return new RandomFunction();
	}

}
