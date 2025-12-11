package picasso.parser;

import java.util.Stack;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;

/**
 * 
 * 
 * @author Therese Elvira Mombou Gatsing
 */
public class EqualAnalyzer implements SemanticAnalyzerInterface { 

	/**
	 *  Handles assignment expressions of the form "name = expression".
	 *
	 * @return the expression tree for the right-hand side
	 */
    @Override
    public ExpressionTreeNode generateExpressionTree(Stack<Token> tokens) { 
        // Remove the "=" token from the stack
        tokens.pop();

        ExpressionTreeNode rhs =
                SemanticAnalyzer.getInstance().generateExpressionTree(tokens);

        Token leftToken = tokens.pop();

        if (!(leftToken instanceof IdentifierToken)) {
            throw new ParseException("Left side of '=' must be an identifier.");
        }

        IdentifierToken idToken = (IdentifierToken) leftToken;
        String variableName = idToken.getName();
        
        if ("x".equals(variableName) || "y".equals(variableName)) {
            throw new ParseException("Cannot assign to built-in variable '" + variableName + "'.");
        }

        SemanticAnalyzer sem = SemanticAnalyzer.getInstance();
        sem.defineVariable(variableName, rhs);

        return rhs;
    }
}
