package picasso.parser;

import java.util.Stack;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.ImageClip;
import picasso.parser.tokens.StringToken;
import picasso.parser.tokens.Token;

/**
 * Handles parsing the ImageClip function
 * 
 * @author Luis Coronel
 */
public class ImageClipAnalyzer implements SemanticAnalyzerInterface {

	@Override
	public ExpressionTreeNode generateExpressionTree(Stack<Token> tokens) {
		// Pop ImageClip token (it's on top)
		tokens.pop();
		
		// In postfix, arguments are before the function
		// So we need to pop in reverse order: y, x, string
		
		// Get y coordinate expression (top of stack after function)
		ExpressionTreeNode yCoord = SemanticAnalyzer.getInstance().generateExpressionTree(tokens);
		
		// Get x coordinate expression
		ExpressionTreeNode xCoord = SemanticAnalyzer.getInstance().generateExpressionTree(tokens);
		String filename = ((StringToken) tokens.pop()).getValue();
		
		return new ImageClip(filename, xCoord, yCoord);
	}
}