package picasso.parser;

import java.util.Stack;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.Plus;
import picasso.parser.tokens.Token;

/**
 * Handles parsing the plus or "addition function".
 * 
 * @author Robert C. Duvall
 * @author Asya Yurkovskaya
 * 
 */
public class PlusAnalyzer extends BinaryOperatorAnalyzer {

	@Override
	public ExpressionTreeNode generateExpressionTree(Stack<Token> tokens) {
		tokens.pop(); 

		ExpressionTreeNode paramETNLeft = SemanticAnalyzer.getInstance().generateExpressionTree(
				tokens);
		ExpressionTreeNode paramETNRight = SemanticAnalyzer.getInstance().generateExpressionTree(
				tokens);
				
		return new Plus(paramETNLeft, paramETNRight);
	}

}
