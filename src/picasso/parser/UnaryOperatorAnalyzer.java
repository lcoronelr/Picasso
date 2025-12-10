package picasso.parser;
import java.util.Stack;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.tokens.Token;

/**
 * Abstract class that parses an operator that takes one expression as parameter.
 * 
 * @author Therese Elvira Mombou Gatsing
 */
public abstract class UnaryOperatorAnalyzer implements SemanticAnalyzerInterface{
	
	@Override
	public abstract ExpressionTreeNode generateExpressionTree(Stack<Token> tokens);
}
