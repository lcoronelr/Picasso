package picasso.parser.language.expressions;

import picasso.parser.language.ExpressionTreeNode;

/**
 * Represents the Mandelbrot function that computes iterations for the Mandelbrot set
 * @author Menilik Deneke
 */
public class Mandelbrot extends ExpressionTreeNode {
	private static final int MAX_ITER = 80;
	
	private ExpressionTreeNode real;
	private ExpressionTreeNode imag;

	/**
	 * Creates a Mandelbrot function with real and imaginary parameters
	 * 
	 * @param real the real part of the complex number
	 * @param imag the imaginary part of the complex number
	 */
	public Mandelbrot(ExpressionTreeNode real, ExpressionTreeNode imag) {
		this.real = real;
		this.imag = imag;
	}

	/**
	 * Evaluates the Mandelbrot function at the given x, y coordinates
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return color based on iteration count
	 */
	@Override
	public RGBColor evaluate(double x, double y) {
		RGBColor realResult = real.evaluate(x, y);
		RGBColor imagResult = imag.evaluate(x, y);

		double cReal = realResult.getRed();
		double cImag = imagResult.getRed();
						
		double zReal = 0;
		double zImag = 0;
		double zMod = Math.sqrt(zReal*zReal + zImag*zImag);
		int n = 0;
		while(zMod <= 2 && n < MAX_ITER) {
			double newZReal = zReal*zReal - zImag*zImag + cReal;
			double newZImag = 2*zReal*zImag + cImag;
			zReal = newZReal;
			zImag = newZImag;
			zMod = Math.sqrt(zReal*zReal + zImag*zImag);
			n++;
		}
		
		//maps number of iterations to a value between -1 and 1
		double value = 1.0 - (2.0 * n / MAX_ITER);
		
		return new RGBColor(value, value, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof Mandelbrot)) return false;
		Mandelbrot other = (Mandelbrot) obj;
		return real.equals(other.real) && imag.equals(other.imag);
	}

	@Override
	public String toString() {
		return "Mandelbrot(" + real + ", " + imag + ")";
	}
}