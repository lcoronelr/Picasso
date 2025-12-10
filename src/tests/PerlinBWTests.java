package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import picasso.parser.ExpressionTreeGenerator;
import picasso.parser.Tokenizer;
import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.PerlinBW;
import picasso.parser.language.expressions.Plus;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.chars.CommaToken;
import picasso.parser.tokens.chars.LeftParenToken;
import picasso.parser.tokens.chars.RightParenToken;
import picasso.parser.tokens.functions.PerlinBWToken;

/**
 * Tests for the perlinBW function 
 * 
 * @author Luis Coronel
 */
public class PerlinBWTests {

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
	public void testPerlinBWCreation() {
		// Test that PerlinBW object can be created
		PerlinBW perlin = new PerlinBW(new X(), new Y());
		assertNotNull(perlin, "PerlinBW is created successfully");
	}

	@Test
	public void testPerlinBWEvaluationReturnsGrayscale() {
		// Test that evaluation returns a grayscale color (all channels equal)
		PerlinBW perlin = new PerlinBW(new X(), new Y());
		RGBColor result = perlin.evaluate(0.5, 0.5);
		
		assertNotNull(result, "Evaluation should return a color");
		
		// For grayscale, all channels should be equal
		assertEquals(result.getRed(), result.getGreen(), EPSILON,
				"Red and Green are equal for grayscale");
		assertEquals(result.getGreen(), result.getBlue(), EPSILON,
				"Green and Blue aere equal for grayscale");
		assertEquals(result.getRed(), result.getBlue(), EPSILON,
				"Red and Blue are equal for grayscale");
	}

	@Test
	public void testPerlinBWSameOutput() {
		// Test that same input produces same output
		PerlinBW perlin = new PerlinBW(new X(), new Y());
		
		RGBColor result1 = perlin.evaluate(0.3, 0.7);
		RGBColor result2 = perlin.evaluate(0.3, 0.7);
		
		assertEquals(result1.getRed(), result2.getRed(), EPSILON,
				"Same input should produce same grayscale value");
	}

	@Test
	public void testPerlinBWDifferentInputs() {
		// Test that different inputs produce different outputs
		PerlinBW perlin = new PerlinBW(new X(), new Y());
		
		RGBColor result1 = perlin.evaluate(0.0, 0.0);
		RGBColor result2 = perlin.evaluate(0.5, 0.5);
		
		assertNotEquals(result1.getRed(), result2.getRed(), 
				"Different inputs have different grayscale values");
	}

	@Test
	public void testPerlinBWEquals() {
		PerlinBW perlin1 = new PerlinBW(new X(), new Y());
		PerlinBW perlin2 = new PerlinBW(new X(), new Y());
		PerlinBW perlin3 = new PerlinBW(new Y(), new X());
		
		assertEquals(perlin1, perlin1, "PerlinBW == itself");
		assertEquals(perlin1, perlin2, "PerlinBWs with same params should be the same");
		assertNotEquals(perlin1, perlin3, "PerlinBWs with different params should not be teh same ");
	}


	@Test
	public void testTokenizePerlinBWExpression() {
		String expression = "perlinBW(x, y)";
		List<Token> tokens = tokenizer.parseTokens(expression);
		
		assertEquals(new PerlinBWToken(), tokens.get(0), "First token should be PerlinBWToken");
		assertEquals(new LeftParenToken(), tokens.get(1), "Second token should be (");
		assertEquals(new IdentifierToken("x"), tokens.get(2), "Third token should be x");
		assertEquals(new CommaToken(), tokens.get(3), "Fourth token should be comma");
		assertEquals(new IdentifierToken("y"), tokens.get(4), "Fifth token should be y");
		assertEquals(new RightParenToken(), tokens.get(5), "Last token should be )");
	}

	@Test
	public void testParsePerlinBWExpression() {
		// Test that parser creates correct PerlinBW expression tree
		ExpressionTreeNode e = parser.makeExpression("perlinBW(x, y)");
		assertEquals(new PerlinBW(new X(), new Y()), e,
				"Parser should create PerlinBW expression tree");
	}

	@Test
	public void testPerlinBWGrayscaleConsistency() {
		// Test that all RGB channels remain equal across multiple points
		PerlinBW perlin = new PerlinBW(new X(), new Y());
		
		double[] testPoints = {-1.0, -0.5, 0.0, 0.5, 1.0};
		
		for (double x : testPoints) {
			for (double y : testPoints) {
				RGBColor color = perlin.evaluate(x, y);
				
				assertEquals(color.getRed(), color.getGreen(), EPSILON,
						"Red and Green must be equal at (" + x + "," + y + ")");
				assertEquals(color.getGreen(), color.getBlue(), EPSILON,
						"Green and Blue must be equal at (" + x + "," + y + ")");
			}
		}
	}
}