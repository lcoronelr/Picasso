package picasso.parser.language.expressions;

import picasso.parser.language.ExpressionTreeNode;

/**
 * 
 * 
 * @author Therese Elvira Mombou Gatsing
 */
public class Negate extends UnaryOperator{
	
	public Negate(ExpressionTreeNode param) {
		super(param);
	}

	@Override
	public RGBColor evaluate(double x, double y) {
		RGBColor value = param.evaluate(x, y);
		double red = -value.getRed();
		double green = -value.getGreen();
		double blue = -value.getBlue();

		return new RGBColor(red, green, blue);
	}

}
