package picasso.parser.language.expressions;

import picasso.parser.language.ExpressionTreeNode;

/**
 * Represents the floor function in the Picasso language.
 * 
 * @author Robert C. Duvall
 * @author Sara Sprenkle
 * 
 */
public class Plus extends BinaryOperator {

	/**
	 * Create a plus expression tree that takes as a parameters the given expression // FIXME: write correct comments
	 * 
	 * @param param the expression to floor
	 */
	public Plus(ExpressionTreeNode left, ExpressionTreeNode right) {
		super(left, right);
	}
	/**
	 * Evaluates this expression at the given x,y point by evaluating the floor of
	 * the function's parameter.
	 * 
	 * @return the color from evaluating the floor of the expression's parameter
	 */
	@Override
	public RGBColor evaluate(double x, double y) {
		RGBColor left_result = left.evaluate(x, y);
		RGBColor right_result = right.evaluate(x, y);
		double red = left_result.getRed() + right_result.getRed();
		double green = left_result.getGreen() + right_result.getGreen();
		double blue = left_result.getBlue() + right_result.getBlue();

		return new RGBColor(red, green, blue);
	}

}