package picasso.parser;

import java.util.Stack;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.Floor;
import picasso.parser.language.expressions.rgbToYCrCb;
import picasso.parser.tokens.Token;

/** 
 * Handles parsing the rgbToYCrCb function
 * 
 * @author Abhishek Pradhan
 *
 */
public class rgbToYCrCbAnalyzer extends UnaryFunctionAnalyzer{
	
	@Override
	
	public ExpressionTreeNode generateExpressionTree(Stack<Token> tokens){
		tokens.pop();
		ExpressionTreeNode paramETN = SemanticAnalyzer.getInstance().generateExpressionTree(
				tokens);
		return new rgbToYCrCb(paramETN);
	}

}
