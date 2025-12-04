package picasso.parser;

import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.BinaryOperator;
import picasso.parser.language.expressions.UnaryFunction;
import picasso.parser.tokens.Token;

/**
 * A class that parses an operator that takes two expressions as parameters.
 * 
 * @author Asya Yurkovskaya
 * 
 */
public class BinaryOperatorAnalyzer implements SemanticAnalyzerInterface {

	@Override
	public ExpressionTreeNode generateExpressionTree(Stack<Token> tokens) {
		Token topToken = tokens.pop(); 
		Class<?> topTokenClass = topToken.getClass();
		String topTokenName = topTokenClass.getCanonicalName();
		String topExpressionName = topTokenName.replace("tokens.operations", "language.expressions").replace("Token", "");

		ExpressionTreeNode paramETNRight = SemanticAnalyzer.getInstance().generateExpressionTree(
			tokens);
		ExpressionTreeNode paramETNLeft = SemanticAnalyzer.getInstance().generateExpressionTree(
				tokens);

		BinaryOperator topExpression = null;

		try {
			Class<?>[] paramTypes = {ExpressionTreeNode.class, ExpressionTreeNode.class};
			topExpression = (BinaryOperator) Class.forName(topExpressionName).getConstructor(paramTypes).newInstance(paramETNLeft, paramETNRight);
				
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