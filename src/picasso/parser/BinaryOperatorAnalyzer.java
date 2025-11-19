package picasso.parser;

import java.util.Stack;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.Plus;
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
 * Abstract class that parses a function that takes one expression as a parameter.
 * 
 * @author Sara Sprenkle
 * 
 */
public abstract class BinaryOperatorAnalyzer implements SemanticAnalyzerInterface {

	@Override
	public ExpressionTreeNode generateExpressionTree(Stack<Token> tokens) {
		tokens.pop(); // Remove the plus token
		// the parameters are the next tokens on the stack.
		// But, they need to be processed

		ExpressionTreeNode paramETNLeft = SemanticAnalyzer.getInstance().generateExpressionTree(
				tokens);
		ExpressionTreeNode paramETNRight = SemanticAnalyzer.getInstance().generateExpressionTree(
				tokens);
				
		return new Plus(paramETNLeft, paramETNRight);
	}

}
