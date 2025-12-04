package picasso.parser;

import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.UnaryFunction;
import picasso.parser.tokens.Token;

/**
 * A class that parses a function that takes one expression as a parameter.
 * 
 * @author Asya Yurkovskaya
 * 
 */
public class UnaryFunctionAnalyzer implements SemanticAnalyzerInterface {
	 
	@Override
	public ExpressionTreeNode generateExpressionTree(Stack<Token> tokens) {
		Token topToken = tokens.pop(); 
		Class<?> topTokenClass = topToken.getClass();
		String topTokenName = topTokenClass.getCanonicalName();
		String topExpressionName = topTokenName.replace("tokens.functions", "language.expressions").replace("Token", "");

		ExpressionTreeNode paramETN = SemanticAnalyzer.getInstance().generateExpressionTree(
				tokens);

		UnaryFunction topExpression = null;

		try {
			Class<?>[] paramTypes = {ExpressionTreeNode.class};
			topExpression = (UnaryFunction) Class.forName(topExpressionName).getConstructor(paramTypes).newInstance(paramETN);
			
		} catch (ClassNotFoundException e) {
			throw new ParseException(topExpression + " not found " + e);
		} catch (InstantiationException e) {
			throw new ParseException(topExpression + " not instantiated " + e);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new ParseException(topExpression + " not creatable " + e);
		}

		return topExpression;
	}

}
