package picasso.parser.language.expressions;

import picasso.parser.language.ExpressionTreeNode;

/**
 * Represents the exponent operator in the Picasso language.
 * 
 * @author Asya Yurkovskaya
 * 
 */
public class Exponent extends BinaryOperator {

	/**
	 * Create a Exponent expression tree that takes as parameters two given expressions
	 * 
	 * @param left the left expression
	 * @param right the right expression
	 */
	public Exponent(ExpressionTreeNode left, ExpressionTreeNode right) {
		super(left, right);
	}
	/**
	 * Evaluates this expression at the given x,y point by adding the parameters.
	 * 
	 * @return the color from adding the expression's parameters
	 */
	@Override
	public RGBColor evaluate(double x, double y) {
		RGBColor left_result = left.evaluate(x, y);
		RGBColor right_result = right.evaluate(x, y);
		double red = Math.pow(left_result.getRed(), right_result.getRed());
		double green = Math.pow(left_result.getGreen(), right_result.getGreen());
		double blue = Math.pow(left_result.getBlue(), right_result.getBlue());

		return new RGBColor(red, green, blue);
	}

}