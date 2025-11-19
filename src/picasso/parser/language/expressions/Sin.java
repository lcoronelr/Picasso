package picasso.parser.language.expressions;

import picasso.parser.language.ExpressionTreeNode;

/**
 * Represents the Sin function in the Picasso language.
 * 
 * 
 * @author Luis Coronel
 * 
 */
public class Sin extends UnaryFunction{

	public Sin(ExpressionTreeNode param) {
		super(param);
	}

	@Override
	public RGBColor evaluate(double x, double y) {
	RGBColor result = param.evaluate(x, y);
	double red = Math.sin(result.getRed());
	double green = Math.sin(result.getGreen());
	double blue = Math.sin(result.getBlue());

	return new RGBColor(red, green, blue);

	}
}