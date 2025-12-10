package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import picasso.parser.ExpressionTreeGenerator;
import picasso.parser.Tokenizer;
import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.PerlinColor;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.chars.CommaToken;
import picasso.parser.tokens.chars.LeftParenToken;
import picasso.parser.tokens.chars.RightParenToken;
import picasso.parser.tokens.functions.PerlinColorToken;

/**
 * Tests for the perlinColor function
 * 
 * @author Luis Coronel
 */
public class PerlinColorTests {

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
	public void testPerlinColorCreation() {
		// Test that PerlinColor object can be created
		PerlinColor perlin = new PerlinColor(new X(), new Y());
		assertNotNull(perlin, "PerlinColor is created sucessfully");
	}

	@Test
	public void testPerlinColorEvaluationReturnsColor() {
		// Test that evaluation returns a valid RGBColor
		PerlinColor perlin = new PerlinColor(new X(), new Y());
		RGBColor result = perlin.evaluate(0.5, 0.5);
		
		assertNotNull(result, "Evaluation should return a color");
		assertTrue(result.getRed() >= -1.0 && result.getRed() <= 1.0, 
				"Red channel should be between [-1, 1]");
		assertTrue(result.getGreen() >= -1.0 && result.getGreen() <= 1.0, 
				"Green channel should be between [-1, 1]");
		assertTrue(result.getBlue() >= -1.0 && result.getBlue() <= 1.0, 
				"Blue channel should be between [-1, 1]");
	}

	@Test
	public void testPerlinColorSameOutput() {
		// Test that same input produces same output
		PerlinColor perlin = new PerlinColor(new X(), new Y());
		
		RGBColor result1 = perlin.evaluate(0.3, 0.7);
		RGBColor result2 = perlin.evaluate(0.3, 0.7);
		
		assertEquals(result1.getRed(), result2.getRed(), EPSILON,
				"Same input should have same red value");
		assertEquals(result1.getGreen(), result2.getGreen(), EPSILON,
				"Same input should have same green value");
		assertEquals(result1.getBlue(), result2.getBlue(), EPSILON,
				"Same input should have same blue value");
	}

	@Test
	public void testPerlinColorDifferentInputs() {
		// Test that different inputs produce different outputs
		PerlinColor perlin = new PerlinColor(new X(), new Y());
		
		RGBColor result1 = perlin.evaluate(0.0, 0.0);
		RGBColor result2 = perlin.evaluate(0.5, 0.5);
		
		// At least one channel should be different
		boolean isDifferent = 
			Math.abs(result1.getRed() - result2.getRed()) > EPSILON ||
			Math.abs(result1.getGreen() - result2.getGreen()) > EPSILON ||
			Math.abs(result1.getBlue() - result2.getBlue()) > EPSILON;
		
		assertTrue(isDifferent, "Different inputts will have  different colors");
	}

	@Test
	public void testPerlinColorEquals() {
		PerlinColor perlin1 = new PerlinColor(new X(), new Y());
		PerlinColor perlin2 = new PerlinColor(new X(), new Y());
		PerlinColor perlin3 = new PerlinColor(new Y(), new X());
		
		assertEquals(perlin1, perlin1, "PerlinColor == itselfd");
		assertEquals(perlin1, perlin2, "PerlinColors same params should be the same.");
		assertNotEquals(perlin1, perlin3, "PerlinColors that have different params will not be the same ");
	}

	@Test
	public void testTokenizePerlinColorExpression() {
		String expression = "perlinColor(x, y)";
		List<Token> tokens = tokenizer.parseTokens(expression);
		
		assertEquals(new PerlinColorToken(), tokens.get(0), "First tshould be PerlinColorToken");
		assertEquals(new LeftParenToken(), tokens.get(1), "Second token should be (");
		assertEquals(new IdentifierToken("x"), tokens.get(2), "Third token should be x");
		assertEquals(new CommaToken(), tokens.get(3), "Fourth token should be comma");
		assertEquals(new IdentifierToken("y"), tokens.get(4), "Fifth token should be y");
		assertEquals(new RightParenToken(), tokens.get(5), "Last token should be )");
	}

	@Test
	public void testParsePerlinColorExpression() {
		// Test that parser creates correct PerlinColor expression tree
		ExpressionTreeNode e = parser.makeExpression("perlinColor(x, y)");
		assertEquals(new PerlinColor(new X(), new Y()), e,
				"Parser should create PerlinColor expression tree");
	}

	@Test
	public void testPerlinColorMultiplePoints() {
		// Test evaluation at multiple points to ensure variety
		PerlinColor perlin = new PerlinColor(new X(), new Y());
		
		double[] testPoints = {-1.0, -0.5, 0.0, 0.5, 1.0};
		
		for (double x : testPoints) {
			for (double y : testPoints) {
				RGBColor color = perlin.evaluate(x, y);
				
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