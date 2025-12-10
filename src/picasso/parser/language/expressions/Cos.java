package picasso.parser.language.expressions;
import picasso.parser.language.ExpressionTreeNode;

/**
 * Represents the Cos function in the Picasso language.
 * 
 * 
 * @author Therese Elvira Mombou Gatsing
 * 
 */
public class Cos extends UnaryFunction{
	
	public Cos(ExpressionTreeNode param) {
		super(param);
	}

	@Override
	public RGBColor evaluate(double x, double y) {
	RGBColor result = param.evaluate(x, y);
	double red = Math.cos(result.getRed());
	double green = Math.cos(result.getGreen());
	double blue = Math.cos(result.getBlue());

	return new RGBColor(red, green, blue);

	}

}
