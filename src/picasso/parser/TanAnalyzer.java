package picasso.parser;

import java.util.Stack;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.Tangent;
import picasso.parser.tokens.Token;


/**
 * Handles parsing the tan function.
 * 
 * @author Therese Elvira Mombou Gatsing
 * 
 */
public class TanAnalyzer extends UnaryFunctionAnalyzer {
	
	@Override
	public ExpressionTreeNode generateExpressionTree(Stack<Token> tokens) {
		tokens.pop(); 
		ExpressionTreeNode paramETN = SemanticAnalyzer.getInstance().generateExpressionTree(
				tokens);
		return new Tangent(paramETN);
	}

}
