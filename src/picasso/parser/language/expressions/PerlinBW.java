package picasso.parser.language.expressions;

import picasso.model.ImprovedNoise;
import picasso.parser.language.ExpressionTreeNode;

/**
 * Represents the PerlinBW function - creates grayscale color based on 2D Perlin noise
 *
 * @author Luis Coronel
 */
public class PerlinBW extends ExpressionTreeNode {

	private ExpressionTreeNode xExpr;
	private ExpressionTreeNode yExpr;

	public PerlinBW(ExpressionTreeNode xExpr, ExpressionTreeNode yExpr) {
		this.xExpr = xExpr;
		this.yExpr = yExpr;
	}

	@Override
	public RGBColor evaluate(double x, double y) {
		RGBColor left = xExpr.evaluate(x, y);
		RGBColor right = yExpr.evaluate(x, y);

		double grey = ImprovedNoise.noise(
			left.getRed() + right.getRed(), 
			left.getGreen() + right.getGreen(),
			left.getBlue() + right.getBlue()
		);

		// Use same value for all three channels to create grayscale
		return new RGBColor(grey, grey, grey);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof PerlinBW)) return false;
		PerlinBW other = (PerlinBW) obj;
		return xExpr.equals(other.xExpr) && yExpr.equals(other.yExpr);
	}

	@Override
	public String toString() {
		return "perlinBW(" + xExpr + ", " + yExpr + ")";
	}
}