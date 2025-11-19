package picasso.parser;

import java.util.Stack;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.tokens.Token;

/**
 * Abstract class that parses an operator that takes two expressions as parameters.
 * 
 * @author Asya Yurkovskaya
 * 
 */
public abstract class BinaryOperatorAnalyzer implements SemanticAnalyzerInterface {
	 
    // TODO: figure out a way to refactor
	@Override
	public abstract ExpressionTreeNode generateExpressionTree(
			Stack<Token> tokens);
}