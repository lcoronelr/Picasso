package picasso.parser.language.expressions;

import picasso.parser.language.ExpressionTreeNode;

/**
 * Represents the atan function in the Picasso language.
 * 
 * 
 * @author Therese Elvira Mombou Gatsing
 * 
 */
public class Atan extends UnaryFunction{
	
	public Atan(ExpressionTreeNode param) {
		super(param);
	}

	
	@Override
	public RGBColor evaluate(double x, double y) {
		RGBColor result = param.evaluate(x, y);
		double red = Math.atan(result.getRed());
		double green = Math.atan(result.getGreen());
		double blue = Math.atan(result.getBlue());

		return new RGBColor(red, green, blue);
	}
}
