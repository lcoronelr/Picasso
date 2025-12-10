package picasso.parser.language.expressions;

import picasso.parser.language.ExpressionTreeNode;

import java.util.Random;

public class RandomFunction extends ExpressionTreeNode {
	private final double red;
	private final double green;
	private final double blue;
	
	public RandomFunction() {
		Random random = new Random();
		this.red = random.nextDouble(2) - 1;
		this.green = random.nextDouble(2) - 1;
		this.blue = random.nextDouble(2) - 1;
	}
	
	@Override
	public RGBColor evaluate(double x, double y) {
		return new RGBColor(red, green, blue);
	}

}
