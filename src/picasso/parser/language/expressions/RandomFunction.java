package picasso.parser.language.expressions;

import picasso.parser.language.ExpressionTreeNode;

import java.util.Random;

public class RandomFunction extends ExpressionTreeNode {

	@Override
	public RGBColor evaluate(double x, double y) {
		Random random = new Random();
		
		double red = random.nextDouble(2) - 1;
		double green = random.nextDouble(2) - 1;
		double blue = random.nextDouble(2) - 1;
		
		return new RGBColor(red, green, blue);
	}

}
