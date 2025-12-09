package picasso.parser.language.expressions;

import picasso.parser.language.ExpressionTreeNode;

/**
 * Represents the YCrCbToRGB in Picasso language
 * 
 * @author Robert C. Duvall
 * @author Abhishek Pradhan
 * Converts a YCrCb color to RGB, assuming that the input expressino stores (Y, Cb, Cr) in the RGB channels in the [-1,1] range using a formula
 * Y' (luma) is stored in the Red Channel
 * Cb (blue-difference) stored in the Green Channel
 * Cr (red-difference) stored in the Blue Channel
 * //**
 */

public class YCrCbToRGB extends UnaryFunction{

	/**
	 * 
	 */
	public YCrCbToRGB(ExpressionTreeNode param) {
		super(param);
	}
	
	@Override
	public RGBColor evaluate (double x, double y) {
				RGBColor ycbcr = param.evaluate(x, y);

		// unpack stored components from [-1,1] range
		double yPicasso = ycbcr.getRed();
		double cbPicasso = ycbcr.getGreen();
		double crPicasso = ycbcr.getBlue();

		// convert back to [0,1] domain expected by the BT.601 formulas
		double yPrime = (yPicasso + 1.0) / 2.0;
		double cb = cbPicasso * 0.492; // inverse of scaling used in RgbToYCrCb
		double cr = crPicasso * 0.877;

		// inverse transform from Y'CbCr to R'G'B' (BT.601)
		double rPrime = yPrime + 1.402 * cr;
		double gPrime = yPrime - 0.344136 * cb - 0.714136 * cr;
		double bPrime = yPrime + 1.772 * cb;

		// map back to Picasso's [-1,1]
		double rPicasso = (rPrime * 2.0) - 1.0;
		double gPicasso = (gPrime * 2.0) - 1.0;
		double bPicasso = (bPrime * 2.0) - 1.0;
				
				// Store (Y', Cb, Cr) in the (R, G, B) channels of a new RGBColor
				return new RGBColor(rPicasso, gPicasso, bPicasso);
	}

}