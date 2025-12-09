package picasso.parser;
import java.util.Stack;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.Negate;
import picasso.parser.tokens.Token;

/**
 * Handles parsing the negate
 * 
 * @author Therese Elvira Mombou Gatsing
 */
public class NegateAnalyzer extends UnaryOperatorAnalyzer {
	
	@Override
	public ExpressionTreeNode generateExpressionTree(Stack<Token> tokens) {
		tokens.pop(); 

		ExpressionTreeNode param = SemanticAnalyzer.getInstance().generateExpressionTree(
			tokens);
		
				
		return new Negate(param);
	}

}
