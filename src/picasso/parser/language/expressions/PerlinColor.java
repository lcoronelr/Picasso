package picasso.parser.language.expressions;

import picasso.model.ImprovedNoise;
import picasso.parser.language.ExpressionTreeNode;

/**
 * Represents the PerlinColor function - creates random col r based on 2d noise
 *
 * @author Luis Coronel
 */
public class PerlinColor extends ExpressionTreeNode {

	private ExpressionTreeNode xExpr;
	private ExpressionTreeNode yExpr;

	public PerlinColor(ExpressionTreeNode xExpr, ExpressionTreeNode yExpr) {
		this.xExpr = xExpr;
		this.yExpr = yExpr;
	}

	@Override
	public RGBColor evaluate(double x, double y) {
		RGBColor xResult = xExpr.evaluate(x, y);
		RGBColor yResult = yExpr.evaluate(x, y);
		
		double xVal = xResult.getRed();
		double yVal = yResult.getRed();

		// Generate Perlin noise gathered from References
		double red = ImprovedNoise.noise(xVal + 0.3, yVal + 0.3, 0);
		double green = ImprovedNoise.noise(xVal - 0.8, yVal - 0.8, 0);
		double blue = ImprovedNoise.noise(xVal + 0.1, yVal + 0.1, 0);

		return new RGBColor(red, green, blue);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof PerlinColor)) return false;
		PerlinColor other = (PerlinColor) obj;
		return xExpr.equals(other.xExpr) && yExpr.equals(other.yExpr);
	}

	@Override
	public String toString() {
		return "perlinColor(" + xExpr + ", " + yExpr + ")";
	}
}