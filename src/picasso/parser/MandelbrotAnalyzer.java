package picasso.parser;

import java.util.Arrays;
import java.util.Stack;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.Mandelbrot;
import picasso.parser.tokens.Token;

/**
 * Handles parsing the Mandelbrot function
 * @author Menilik Deneke
 */
public class MandelbrotAnalyzer implements SemanticAnalyzerInterface {

    @Override
    public ExpressionTreeNode generateExpressionTree(Stack<Token> tokens) {
        tokens.pop();
        
        ExpressionTreeNode paramImag = SemanticAnalyzer.getInstance().generateExpressionTree(tokens);
        ExpressionTreeNode paramReal = SemanticAnalyzer.getInstance().generateExpressionTree(tokens);

        return new Mandelbrot(paramReal, paramImag);
    }
}