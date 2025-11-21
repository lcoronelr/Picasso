package tests;



import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import picasso.parser.ExpressionTreeGenerator;
import picasso.parser.Tokenizer;
import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.RgbToYCrCb;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.functions.RgbToYCrCbToken;

/**
 * Tests for the RgbToYCrCb function.
 * 
 * 
 * Red channel = Y 
 * Green channel = Cb  blue difference
 * Blue channel  = Cr  red difference
 * 
 * @author Luis Coronel
 * @author Abhishek Pradhan
 * @author Asya Yurkovskaya
 * 
 */

public class RgbToYCrCbTests {
	
	private static ExpressionTreeGenerator parser;
	private Tokenizer tokenizer;
	private static final double EPSILON = 1e-9;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		parser = new ExpressionTreeGenerator();
	}
	
	@BeforeEach
	public void setUp() throws Exception {
		tokenizer = new Tokenizer();
	}

	@Test
	public void basicTestsRgbToYCrCb() {
		ExpressionTreeNode e = parser.makeExpression("rgbToYCrCb(x)");
		assertEquals(new RgbToYCrCb(new X()), e);

		String expression = "rgbToYCrCb(x)";
		List<Token> tokens = tokenizer.parseTokens(expression);
		assertEquals(new RgbToYCrCbToken(), tokens.get(0));
	}

	@Test
	public void testRgbToYCrCbPureBlack() {
		//black  : [-1,-1,-1]
		ExpressionTreeNode color = new RGBColor(-1, -1, -1);
		RgbToYCrCb converter = new RgbToYCrCb(color);
		RGBColor result = converter.evaluate(0,0);
		assertEquals(-1.0, result.getRed(), EPSILON, "Y should be -1 for black");
		assertEquals(0.0, result.getGreen(), EPSILON, "Cb should be 0 for black");
		assertEquals(0.0, result.getBlue(), EPSILON, "Cr should be 0 for black");
	}
	
	@Test
	public void testRgbToYCrCbPureWhite() {
		//White  : [1,1,1]
				ExpressionTreeNode color = new RGBColor(1, 1, 1);
				RgbToYCrCb converter = new RgbToYCrCb(color);
				RGBColor result = converter.evaluate(0,0);
				assertEquals(1.0, result.getRed(), EPSILON, "Y should be 1 for white");
				assertEquals(0.0, result.getGreen(), EPSILON, "Cb should be 0 for white");
				assertEquals(0.0, result.getBlue(), EPSILON, "Cr should be 0 for white");			
	}
	@Test
	public void testRgbToYCrCbPureRed() {
		//Red  : [1,-1,-1]
				ExpressionTreeNode color = new RGBColor(1,-1, -1);
				RgbToYCrCb converter = new RgbToYCrCb(color);
				RGBColor result = converter.evaluate(0,0);
				
				//red should have a Cr more than 0 (positive)
				assertTrue(result.getRed() > -1.0, "the Y should be positive for red");
				assertTrue(result.getBlue() > 0.0, "Cr should be positive for red");		
	}
	@Test
	public void testRgbToYCrCbPureBlue() {
		//Blue  : [-1,-1,1]
				ExpressionTreeNode color = new RGBColor(-1,-1, 1);
				RgbToYCrCb converter = new RgbToYCrCb(color);
				RGBColor result = converter.evaluate(0,0);
				
				assertTrue(result.getRed() > -1.0, "Y should be gpositive for red");
				assertTrue(result.getGreen() > 0.0, "Cb should be positive for blue");
				}
	@Test
	public void testRgbToYCrCbPureGreen() {
		//  green: [-1, 1, -1]
		ExpressionTreeNode color = new RGBColor(-1, 1, -1);
		RgbToYCrCb converter = new RgbToYCrCb(color);
		RGBColor result = converter.evaluate(0, 0);
		assertTrue(result.getRed() > 0.0, "Y should be high for green");
	}

	/**
	 * Test to see if the RGB to YCrCb image is in greyscale, or if the Cr and Cb values are equal to zero, resulting 
	 * in only the luminance varying 
	 */
	@Test
	public void testRgbToYCrCbGreyscale() {
		RGBColor grey = new RGBColor (0.2, 0.2, 0.2);
		RgbToYCrCb converter = new RgbToYCrCb(grey);
		
		RGBColor result = converter.evaluate(0,0);
		
		//Y (Red Channel) should be between -1 and 1 (within range) when the RgbToYCrCb returns the value
		assertTrue(result.getRed() >=-1 && result.getRed()<=1, "Y should be in [-1,1] range for greyscale");
		
		//Cb (Green Channel) should be 0 for greyscale
		assertEquals (0.0, result.getGreen(), EPSILON, "Cb should be zero for any greyscale color");
		
		//Cr (Blue Channel) should be 0
		assertEquals (0.0, result.getBlue(), EPSILON, "Cr should be zero for any greyscale color");
		
	}
	/**
	 * Tests that the brightness (Luminance) or the Y' value increases or not
	 */
	@Test
	public void testLuminanceChange() {
	    RGBColor dark = new RGBColor(-1, -1, -1);
	    RGBColor mid  = new RGBColor( 0,  0,  0);
	    RGBColor bright = new RGBColor(1, 1, 1);

	    RgbToYCrCb convertDark = new RgbToYCrCb(dark);
	    RgbToYCrCb convertMid  = new RgbToYCrCb(mid);
	    RgbToYCrCb convertBright = new RgbToYCrCb(bright);

	    double Ydark = convertDark.evaluate(0,0).getRed();
	    double Ymid = convertMid.evaluate(0,0).getRed();
	    double Ybright = convertBright.evaluate(0,0).getRed();

	    assertTrue(Ydark < Ymid, "Dark should have lower luminance than grey");
	    assertTrue(Ymid < Ybright, "Grey should have lower luminance than white");
	}

}
