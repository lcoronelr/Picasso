package picasso.parser;

import java.util.Stack;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.ImageWrap;

import picasso.parser.tokens.Token;
import picasso.parser.tokens.StringToken;

/**
 * Handles parsing the ImageWrap function
 * 
 * @author Luis Coronel
 */
public class ImageWrapAnalyzer implements SemanticAnalyzerInterface {

    @Override
    public ExpressionTreeNode generateExpressionTree(Stack<Token> tokens) {
        tokens.pop();
        
        ExpressionTreeNode yCoord = SemanticAnalyzer.getInstance().generateExpressionTree(tokens);
        
        if (tokens.isEmpty()) {
            throw new ParseException("imageWrap requires 3 arguments but only received 1. Required: filename (string), x coordinate, and y coordinate");
        }
        
        ExpressionTreeNode xCoord = SemanticAnalyzer.getInstance().generateExpressionTree(tokens);
        
        if (tokens.isEmpty()) {
            throw new ParseException("imageWrap requires 3 arguments but only received 2. Required: filename (string), x coordinate, and y coordinate");
        }
        
        Token filenameToken = tokens.pop();
        if (!(filenameToken instanceof StringToken)) {
            throw new ParseException("imageWrap requires 3 arguments: filename (string), x coordinate, and y coordinate");
        }
        
        String filename = ((StringToken) filenameToken).getValue();
        return new ImageWrap(filename, xCoord, yCoord);
    }
}