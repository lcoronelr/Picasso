package picasso.parser.language.expressions;

import picasso.parser.language.ExpressionTreeNode;

/**
 * Represents the tan function in the Picasso language.
 * 
 * 
 * @author Therese Elvira Mombou Gatsing
 * 
 */
public class Tangent extends UnaryFunction {
	
	
	/**
	 * @param param
	 */
	public Tangent(ExpressionTreeNode param) {
		super(param);
	}
	
	
	/**
	 * @param x
	 * @param y
	 * @return
	 * @see picasso.parser.language.ExpressionTreeNode#evaluate(double, double)
	 */
	@Override
	public RGBColor evaluate(double x, double y) {
		RGBColor result = param.evaluate(x, y);
		double red = Math.tan(result.getRed());
		double green = Math.tan(result.getGreen());
		double blue = Math.tan(result.getBlue());

		return new RGBColor(red, green, blue);
	}

}
