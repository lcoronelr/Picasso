package picasso.parser.language.expressions;

import picasso.parser.language.ExpressionTreeNode;

/**
 * Represents the rgbToYCrCb in Picasso language
 * 
 * @author Robert C. Duvall
 * @author Abhishek Pradhan
 *//**
 */

public class RgbToYCrCb extends UnaryFunction{

	/**
	 * Create a floor expression tree that takes as a parameter the given expression
	 * 
	 * @param param the expression to rgbToYCrCb
	 */
	public RgbToYCrCb(ExpressionTreeNode param) {
		super(param);
	}
	
	/**
	 * Evaluates this expression at the given x, y coordinate by evaluating the parameter
	 * and converting its color from RGB to YCrCb using a formula
	 * Y' (luma) is stored in the Red Channel
	 * Cb (blue-difference) stored in the Green Channel
	 * Cr (red-difference) stored in the Blue Channel
	 * 
	 * @param x
	 * @param y
	 * @return the color from evaluating the (Y, Cb, Cr) values
	 */
	@Override
	public RGBColor evaluate (double x, double y) {
		RGBColor result = param.evaluate(x, y);
		
		// getting Picasso's [-1,1] values
		double red = result.getRed();
		double green = result.getGreen();
		double blue = result.getBlue();
		
		//convert from [-1,1] range to the formula's to [0,1] range
		double red_prime = (red +1)/2.0;
		double green_prime = (green+1)/2.0;
		double blue_prime = (blue+1)/2.0;
		
		//apply BT.601 conversion formulas given on Wikipedia
		//for Y' = 0.299R' + 0.587G' + 0.114B'
		double Y_prime = 0.299 * red_prime + 0.587 * green_prime + 0.114 * blue_prime;
		
		// U = 0.492(B' - Y') This is Cb, or blue-differenc
		double cb_val = 0.492 * (blue_prime - Y_prime);
		
		// V = 0.877(R' - Y') This is Cr, or red-difference
		double cr_val = 0.877 * (red_prime - Y_prime);
		
		//change the range back to [-1,1] range and store them
		
		// Y_picasso = (Y_prime * 2) - 1
				double y_picasso = (Y_prime * 2.0) - 1.0;

				// Cb (U) is roughly in [-0.492, 0.492]
				double cb_picasso = cb_val / 0.492;

				// Cr (V) is roughly in [-0.877, 0.877]
				double cr_picasso = cr_val / 0.877;
				
				// Store (Y', Cb, Cr) in the (R, G, B) channels of a new RGBColor
				return new RGBColor(y_picasso, cb_picasso, cr_picasso);
	}

}