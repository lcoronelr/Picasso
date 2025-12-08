package picasso.parser;

import java.util.Stack;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.PerlinBW;
import picasso.parser.tokens.Token;

/**
 * Handles parsing the PerlinBW function
 * 
 * @author Luis Coronel
 */
public class PerlinBWAnalyzer implements SemanticAnalyzerInterface {

    @Override
    public ExpressionTreeNode generateExpressionTree(Stack<Token> tokens) {
        tokens.pop(); // Remove the function token
        
        // again, the reverse order

        ExpressionTreeNode yExpr = SemanticAnalyzer.getInstance().generateExpressionTree(tokens);
        
        if (tokens.isEmpty()) {
            throw new ParseException("perlinColor requires 2 arguments");
        }
        
        ExpressionTreeNode xExpr = SemanticAnalyzer.getInstance().generateExpressionTree(tokens);
        
        return new PerlinBW(xExpr, yExpr);
    }
}