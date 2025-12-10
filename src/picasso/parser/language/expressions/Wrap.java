package picasso.parser.language.expressions;

import picasso.parser.language.ExpressionTreeNode;

public class Wrap extends UnaryFunction {
	public Wrap(ExpressionTreeNode param) {
		super(param);
	}

	@Override
	public RGBColor evaluate(double x, double y) {
		RGBColor result = param.evaluate(x, y);
		double red = wrapHelper(result.getRed());
		double green = wrapHelper(result.getGreen());
		double blue = wrapHelper(result.getBlue());
		return new RGBColor(red, green, blue);
	}
	
	private static double wrapHelper(double val) {
		double range = 2.0;
		double shifted = val + 1;
		
		double wrapped = shifted % 2;
		if(wrapped < 0) {
			wrapped += 2;
		}
		
		if (wrapped == 0) {
			if (val >= 1.0) {
				return 1.0;
			}
			else {
				return -1.0;
			}
		}
		
		return wrapped - 1;	
		
	}

}