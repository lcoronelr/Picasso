package picasso.parser.language;

import java.util.Map;
import java.util.TreeMap;

import picasso.parser.language.expressions.RGBColor;

/**
 * All elements of the language (e.g., that make up ExpressionTree) should
 * extend ExpressionTreeNode.
 * 
 * @author Robert C. Duvall
 * @author Sara Sprenkle
 */
public abstract class ExpressionTreeNode implements EvaluatableExpression {

	/**
	 * Evaluate this expression, given x and y
	 * 
	 * @param x the value of x
	 * @param y the value of y
	 * 
	 * @return the result of evaluating the expression for the given values of x and
	 *         y
	 */
	public abstract RGBColor evaluate(double x, double y);

	static protected Map<ExpressionTreeNode, Object> gelementsToValue = new TreeMap<ExpressionTreeNode, Object>();
}
