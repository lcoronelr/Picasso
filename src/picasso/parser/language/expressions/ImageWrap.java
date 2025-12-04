package picasso.parser.language.expressions;

import picasso.model.Pixmap;
import picasso.parser.language.ExpressionTreeNode;

/**
 * Represents the imageWrap function - imports an image and wraps it
 *
 * @author Luis Coronel
 */
public class ImageWrap extends ExpressionTreeNode {

	private String filename;
	private ExpressionTreeNode xCoord;
	private ExpressionTreeNode yCoord;
	private Pixmap image;

	public ImageWrap(String filename, ExpressionTreeNode xCoord, ExpressionTreeNode yCoord) {
		this.filename = filename;
		this.xCoord = xCoord;
		this.yCoord = yCoord;

		try {
			this.image = new Pixmap(filename);
		} catch (Exception e) {
			System.err.println("Error loading image: " + filename);
			e.printStackTrace();
			// Create default image
			this.image = new Pixmap(100, 100);
		}
	}

	@Override
	public RGBColor evaluate(double x, double y) {
		// Evaluate the coordinate expressions
		RGBColor xResult = xCoord.evaluate(x, y);
		RGBColor yResult = yCoord.evaluate(x, y);

		// Use red component as coordinate value
		double xVal = xResult.getRed();
		double yVal = yResult.getRed();

		// Clamp to [-1, 1]
		xVal = Math.max(-1.0, Math.min(1.0, xVal));
		yVal = Math.max(-1.0, Math.min(1.0, yVal));

		// Convert domain [-1,1] to image coordinates [0, width/height]
		int imageWidth = image.getSize().width;
		int imageHeight = image.getSize().height;

		int imageX = (int) Math.round(((xVal + 1.0) / 2.0) * (imageWidth - 1));
		int imageY = (int) Math.round(((yVal + 1.0) / 2.0) * (imageHeight - 1));

		// Get color from image
		java.awt.Color pixelColor = image.getColor(imageX, imageY);

		// Convert [0,255] to [-1,1]
		double red = (pixelColor.getRed() / 255.0) * 2.0 - 1.0;
		double green = (pixelColor.getGreen() / 255.0) * 2.0 - 1.0;
		double blue = (pixelColor.getBlue() / 255.0) * 2.0 - 1.0;

		return new RGBColor(red, green, blue);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof ImageWrap)) return false;
		ImageWrap other = (ImageWrap) obj;
		return filename.equals(other.filename) &&
		       xCoord.equals(other.xCoord) &&
		       yCoord.equals(other.yCoord);
	}

	@Override
	public String toString() {
		return "imageWrap(\"" + filename + "\", " + xCoord + ", " + yCoord + ")";
	}
}