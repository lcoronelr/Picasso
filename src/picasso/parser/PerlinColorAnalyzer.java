package picasso.parser;

import java.util.Stack;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.PerlinColor;
import picasso.parser.tokens.Token;

/**
 * Handles parsing the PerlinColor function
 * 
 * @author Luis Coronel
 */
public class PerlinColorAnalyzer implements SemanticAnalyzerInterface {

    @Override
    public ExpressionTreeNode generateExpressionTree(Stack<Token> tokens) {
        tokens.pop(); // Remove the function token
        
        // again, the reverse order

        ExpressionTreeNode yExpr = SemanticAnalyzer.getInstance().generateExpressionTree(tokens);
        
        if (tokens.isEmpty()) {
            throw new ParseException("perlinColor requires 2 arguments");
        }
        
        ExpressionTreeNode xExpr = SemanticAnalyzer.getInstance().generateExpressionTree(tokens);
        
        return new PerlinColor(xExpr, yExpr);
    }
}