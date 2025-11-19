package picasso.parser;

import java.util.Stack;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.Atangent;
import picasso.parser.tokens.Token;



/**
 * Handles parsing the atan function.
 * 
 * @author Therese Elvira Mombou Gatsing
 * 
 */

public class AtanAnalyzer extends UnaryFunctionAnalyzer {
	
	@Override
	public ExpressionTreeNode generateExpressionTree(Stack<Token> tokens) {
		tokens.pop(); 
		ExpressionTreeNode paramETN = SemanticAnalyzer.getInstance().generateExpressionTree(
				tokens);
		return new Atangent(paramETN);
	}


}
