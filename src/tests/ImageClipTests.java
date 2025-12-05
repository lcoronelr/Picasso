package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import picasso.parser.ExpressionTreeGenerator;
import picasso.parser.Tokenizer;
import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.ImageClip;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.StringToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.chars.LeftParenToken;
import picasso.parser.tokens.chars.RightParenToken;
import picasso.parser.tokens.chars.CommaToken;
import picasso.parser.tokens.functions.ImageClipToken;

/**
 * Tests for the imageClip function.
 * 
 * @author Luis Coronel
 */
public class ImageClipTests {

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
	public void testImageClipCreation() {
		// Test that ImageClip object can be created
		ImageClip img = new ImageClip("images/vortex.jpg", new X(), new Y());
		assertNotNull(img, "ImageClip is created successfully");
	}

	@Test
	public void testImageClipEvaluationReturnsColor() {
		// Test that evaluation returns a valid RGBColor
		ImageClip img = new ImageClip("images/vortex.jpg", new X(), new Y());
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
	public void testImageClipClamping() {
		// Test that coordinates outside [-1,1] are clamped
		ImageClip img = new ImageClip("images/vortex.jpg", new X(), new Y());
		
		// Evaluate at corner (should clamp to edges)
		RGBColor corner1 = img.evaluate(-1.0, -1.0);
		RGBColor corner2 = img.evaluate(1.0, 1.0);
		
		// shouldn't crash when we get the colors on the edges 
		assertNotNull(corner1, "Should handle corner at (-1, -1)");
		assertNotNull(corner2, "Should handle corner at (1, 1)");
	}

	@Test
	public void testImageClipEquals() {
		ImageClip img1 = new ImageClip("images/vortex.jpg", new X(), new Y());
		ImageClip img2 = new ImageClip("images/vortex.jpg", new X(), new Y());
		ImageClip img3 = new ImageClip("images/vortex.jpg", new Y(), new X());
		
		assertEquals(img1, img1, "ImageClip should equal itself");
		assertEquals(img1, img2, "ImageClips with same params should be equal");
		assertNotEquals(img1, img3, "ImageClips with different coordinates should not be equal");
	}

	@Test
	public void testImageClipToString() {
		ImageClip img = new ImageClip("images/vortex.jpg", new X(), new Y());
		String result = img.toString();
		
		assertTrue(result.contains("imageClip"), "toString have 'imageClip'");
		assertTrue(result.contains("vortex.jpg"), "toString have filename");
		assertTrue(result.contains("x"), "toString have the x coordinate");
		assertTrue(result.contains("y"), "toString have the y coordinate");
	}

	@Test
	public void testTokenizeImageClipExpression() {
		String expression = "imageClip(\"images/vortex.jpg\", x, y)";
		List<Token> tokens = tokenizer.parseTokens(expression);
		
		assertEquals(new ImageClipToken(), tokens.get(0), "First token is ImageClipToken");
		assertEquals(new LeftParenToken(), tokens.get(1), "Second token is (");
		assertTrue(tokens.get(2) instanceof StringToken, "Third token is StringToken");
		assertEquals(new CommaToken(), tokens.get(3), "Fourth token is comma");
		assertEquals(new IdentifierToken("x"), tokens.get(4), "Fifth token is x");
		assertEquals(new CommaToken(), tokens.get(5), "Sixth token is comma");
		assertEquals(new IdentifierToken("y"), tokens.get(6), "Seventh token is y");
		assertEquals(new RightParenToken(), tokens.get(7), "Last token should be parenthesis");
	}

	@Test
	public void testParseImageClipExpression() {
		// Test that parser creates correct ImageClip expression tree
		ExpressionTreeNode e = parser.makeExpression("imageClip(\"images/vortex.jpg\", x, y)");
		assertEquals(new ImageClip("images/vortex.jpg", new X(), new Y()), e,
				"The Parser should be able to create ImageClip expression tree");
	}

	@Test
	public void testImageClipColorRange() {
		// Test that colors are properly converted from [0,255] to [-1,1]
		ImageClip img = new ImageClip("images/vortex.jpg", new X(), new Y());
		
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