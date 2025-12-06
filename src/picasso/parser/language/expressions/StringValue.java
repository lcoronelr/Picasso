package picasso.parser.language.expressions;

import picasso.model.Pixmap;
import picasso.parser.language.ExpressionTreeNode;

/**
 * Represents a string valuee
 * 
 * @author Luis Coronel
 */
public class StringValue extends ExpressionTreeNode {
	
	private String value;
	private Pixmap image;
	
	public StringValue(String value) {
		this.value = value;
		
		// Load the image when string is created
			this.image = new Pixmap(value);
		
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public RGBColor evaluate(double x, double y) {
		// Map domain coordinates [-1,1] to image coordinates
		int imageWidth = image.getSize().width;
		int imageHeight = image.getSize().height;
		
		// Clamp to [-1, 1]
		x = Math.clamp(x, -1.0, 1.0);
		y = Math.clamp(y, -1.0, 1.0);
		
		// Convert to image coordinates
		int imageX = (int) Math.round(((x + 1.0) / 2.0) * (imageWidth - 1));
		int imageY = (int) Math.round(((y + 1.0) / 2.0) * (imageHeight - 1));
		
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
		if (!(obj instanceof StringValue)) return false;
		return value.equals(((StringValue) obj).value);
	}
	
	@Override
	public String toString() {
		return "\"" + value + "\"";
	}
}