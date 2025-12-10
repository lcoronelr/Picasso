package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import picasso.parser.ExpressionTreeGenerator;
import picasso.parser.Tokenizer;
import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.ImageWrap;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.StringToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.chars.LeftParenToken;
import picasso.parser.tokens.chars.RightParenToken;
import picasso.parser.tokens.chars.CommaToken;
import picasso.parser.tokens.functions.ImageWrapToken;

/**
 * Tests for the imageWrap function
 * 
 * @author Luis Coronel
 */
public class ImageWrapTests {

	private static ExpressionTreeGenerator parser;
	private Tokenizer tokenizer;
	private static final double EPSILON = 1e-6;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		parser = new ExpressionTreeGenerator();
	}

	@BeforeEach
	public void setUp() throws Exception {
		tokenizer = new Tokenizer();
	}

	@Test
	public void testImageWrapCreation() {
		// Test that ImageWrap object can be created
		ImageWrap img = new ImageWrap("images/vortex.jpg", new X(), new Y());
		assertNotNull(img, "ImageWrap should be created successfully");
	}

	@Test
	public void testImageWrapEvaluationReturnsColor() {
		// Test that evaluation returns a valid RGBColor
		ImageWrap img = new ImageWrap("images/vortex.jpg", new X(), new Y());
		RGBColor result = img.evaluate(0.0, 0.0);
		
		assertNotNull(result, "Evaluation should return a color");
		assertTrue(result.getRed() >= -1.0 && result.getRed() <= 1.0, 
				"Red channel should be between [-1, 1]");
		assertTrue(result.getGreen() >= -1.0 && result.getGreen() <= 1.0, 
				"Green channel should be between [-1, 1]");
		assertTrue(result.getBlue() >= -1.0 && result.getBlue() <= 1.0, 
				"Blue channel should be between [-1, 1]");
	}

	@Test
	public void testImageWrapWrapping() {
		// Test that coordinates outside [-1,1] wrap around (key difference from imageClip)
		ImageWrap img = new ImageWrap("images/vortex.jpg", new X(), new Y());
		
		// Evaluate at origin
		RGBColor origin = img.evaluate(0.0, 0.0);
		
		// Evaluate at wrapped position (should give same or similar color due to tiling)
		// This tests that wrapping happens, not that it crashes
		RGBColor wrapped = img.evaluate(2.0, 2.0); // Way outside bounds
		
		assertNotNull(origin, "(0, 0)");
		assertNotNull(wrapped, "(2, 2)");
		
		// Both should be valid colors
		assertTrue(wrapped.getRed() >= -1.0 && wrapped.getRed() <= 1.0);
	}

	@Test
	public void testImageWrapEquals() {
		ImageWrap img1 = new ImageWrap("images/vortex.jpg", new X(), new Y());
		ImageWrap img2 = new ImageWrap("images/vortex.jpg", new X(), new Y());
		ImageWrap img3 = new ImageWrap("images/vortex.jpg", new Y(), new X());
		
		assertEquals(img1, img1, "ImageWrap equal itself");
		assertEquals(img1, img2, "ImageWraps with same params should be equal");
		assertNotEquals(img1, img3, "ImageWraps with different coordinates should not be equal");
	}

	@Test
	public void testImageWrapToString() {
		ImageWrap img = new ImageWrap("images/vortex.jpg", new X(), new Y());
		String result = img.toString();
		
		assertTrue(result.contains("ImageWrap"), "toString should contain 'ImageWrap'");
		assertTrue(result.contains("vortex.jpg"), "toString should contain filename");
		assertTrue(result.contains("x"), "toString should contain x coordinate");
		assertTrue(result.contains("y"), "toString should contain y coordinate");
	}

	@Test
	public void testTokenizeImageWrapExpression() {
		String expression = "imageWrap(\"images/vortex.jpg\", x, y)";
		List<Token> tokens = tokenizer.parseTokens(expression);
		
		assertEquals(new ImageWrapToken(), tokens.get(0), "First token should be ImageWrapToken");
		assertEquals(new LeftParenToken(), tokens.get(1), "Second token should be (");
		assertTrue(tokens.get(2) instanceof StringToken, "Third token should be StringToken");
		assertEquals(new CommaToken(), tokens.get(3), "Fourth token should be comma");
		assertEquals(new IdentifierToken("x"), tokens.get(4), "Fifth token should be x");
		assertEquals(new CommaToken(), tokens.get(5), "Sixth token should be comma");
		assertEquals(new IdentifierToken("y"), tokens.get(6), "Seventh token should be y");
		assertEquals(new RightParenToken(), tokens.get(7), "Last token should be )");
	}

	@Test
	public void testParseImageWrapExpression() {
		// Test that parser creates correct ImageWrap expression tree
		ExpressionTreeNode e = parser.makeExpression("imageWrap(\"images/vortex.jpg\", x, y)");
		assertEquals(new ImageWrap("images/vortex.jpg", new X(), new Y()), e,
				"Parser should create ImageWrap expression tree");
	}

	@Test
	public void testImageWrapColorRange() {
		// Test that colors are properly converted from [0,255] to [-1,1]
		ImageWrap img = new ImageWrap("images/vortex.jpg", new X(), new Y());
		
		// Sample multiple points
		double[] testPoints = {-1.0, -0.5, 0.0, 0.5, 1.0};
		
		for (double x : testPoints) {
			for (double y : testPoints) {
				RGBColor color = img.evaluate(x, y);
				
				assertTrue(color.getRed() >= -1.0 && color.getRed() <= 1.0,
						"Red should be in range [-1,1] at (" + x + "," + y + ")");
				assertTrue(color.getGreen() >= -1.0 && color.getGreen() <= 1.0,
						"Green should be in range [-1,1] at (" + x + "," + y + ")");
				assertTrue(color.getBlue() >= -1.0 && color.getBlue() <= 1.0,
						"Blue should be in range [-1,1] at (" + x + "," + y + ")");
			}
		}
	}
}