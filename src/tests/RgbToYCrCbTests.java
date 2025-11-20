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
import picasso.parser.language.expressions.Floor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.chars.LeftParenToken;
import picasso.parser.tokens.chars.RightParenToken;
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
 * 
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
		//White  : [1,1,1]
				ExpressionTreeNode color = new RGBColor(1,-1, -1);
				RgbToYCrCb converter = new RgbToYCrCb(color);
				RGBColor result = converter.evaluate(0,0);
				
				//red should have a Cr more than 0 (positive)
				assertTrue(result.getRed() > -1.0, "Y (luma) should be positive for red");
				assertTrue(result.getBlue() > 0.0, "Cr should be positive for red");		
	}
	@Test
	public void testRgbToYCrCbPureBlue() {
		//White  : [1,1,1]
				ExpressionTreeNode color = new RGBColor(-1,-1, 1);
				RgbToYCrCb converter = new RgbToYCrCb(color);
				RGBColor result = converter.evaluate(0,0);
				
				assertTrue(result.getRed() > -1.0, "Y should be gpositive for red");
				assertTrue(result.getGreen() > 0.0, "Cb the green channel, should be positive for blue");
				}
	@Test
	public void testRgbToYCrCbPureGreen() {
		// Pure green: RGB = (-1, 1, -1)
		ExpressionTreeNode color = new RGBColor(-1, 1, -1);
		RgbToYCrCb converter = new RgbToYCrCb(color);
		RGBColor result = converter.evaluate(0, 0);
		assertTrue(result.getRed() > 0.0, "Y (should be high for green");
	}


}

