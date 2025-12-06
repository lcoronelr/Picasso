package picasso.parser;

import java.util.Stack;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.ImageClip;
import picasso.parser.language.expressions.StringValue;
import picasso.parser.tokens.Token;

/**
 * Handles parsing the ImageClip function
 * 
 * @author Luis Coronel
 */
public class ImageClipAnalyzer implements SemanticAnalyzerInterface {

	@Override
	public ExpressionTreeNode generateExpressionTree(Stack<Token> tokens) {
		tokens.pop();
		// Get y coordinate expression (top of stack after function)
		ExpressionTreeNode yCoord = SemanticAnalyzer.getInstance().generateExpressionTree(tokens);
		
		// Get x coordinate expression
		ExpressionTreeNode xCoord = SemanticAnalyzer.getInstance().generateExpressionTree(tokens);
		
		// Get filename now its a StringValue (ExpressionTreeNode)
		ExpressionTreeNode filenameNode = SemanticAnalyzer.getInstance().generateExpressionTree(tokens);
		
		String filename = ((StringValue) filenameNode).getValue();
		
		return new ImageClip(filename, xCoord, yCoord);
	}
}


