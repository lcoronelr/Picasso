package picasso.parser.language.expressions;

import picasso.parser.language.ExpressionTreeNode;

/**
 * Represents the clamp function in the Picasso Language
 * 
 * @author Menilik Deneke
 */

public class Clamp extends UnaryFunction {
	public Clamp(ExpressionTreeNode param) {
		super(param);
	}
	
	@Override
	public RGBColor evaluate(double x, double y) {
		// TODO Auto-generated method stub
		RGBColor result = param.evaluate(x, y);
		double red = Math.clamp(result.getRed(), -1, 1);
		double green = Math.clamp(result.getGreen(), -1, 1);
		double blue = Math.clamp(result.getBlue(), -1, 1);
		return new RGBColor(red, green, blue);
	}

}