package picasso.parser.language.expressions;
import picasso.parser.language.ExpressionTreeNode;

/**
 * Represents the abs function in the Picasso language.
 * 
 * @author Therese Elvira Mombou Gatsing
 */

public class Abs extends UnaryFunction {
	
	public Abs(ExpressionTreeNode param) {
		super(param);
	}

	@Override
	public RGBColor evaluate(double x, double y) {
		RGBColor result = param.evaluate(x, y);
		double red = Math.abs(result.getRed());
		double green = Math.abs(result.getGreen());
		double blue = Math.abs(result.getBlue());

		return new RGBColor(red, green, blue);
	}
}
