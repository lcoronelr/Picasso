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
	    tokens.pop();
	    
	    ExpressionTreeNode yCoord = SemanticAnalyzer.getInstance().generateExpressionTree(tokens);
	    
	    if (tokens.isEmpty()) {
	        throw new ParseException("imageClip requires 3 arguments but only received 1: missing x coordinate and filename");
	    }
	    
	    ExpressionTreeNode xCoord = SemanticAnalyzer.getInstance().generateExpressionTree(tokens);
	    
	    if (tokens.isEmpty()) {
	        throw new ParseException("imageClip requires 3 arguments but only received 2: missing filename");
	    }
	    
	    Token filenameToken = tokens.pop();
	    if (!(filenameToken instanceof StringToken)) {
	        throw new ParseException("imageClip requires 3 arguments: filename (string), x coordinate, and y coordinate");
	    }
	    
	    String filename = ((StringToken) filenameToken).getValue();
	    return new ImageClip(filename, xCoord, yCoord);
	}
}