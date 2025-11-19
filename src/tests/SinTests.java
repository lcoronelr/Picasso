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
import picasso.parser.language.expressions.Sin;
import picasso.parser.language.expressions.Floor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.chars.LeftParenToken;
import picasso.parser.tokens.chars.RightParenToken;
import picasso.parser.tokens.functions.SinToken;

/**
 * Tests for the Sin function.
 * 
 * @author Luis Coronel
 */
public class SinTests {

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
	public void testSinEvaluationBasic() {
		Sin myTree = new Sin(new X());

		// Test at x = 0
		RGBColor result0 = myTree.evaluate(0.0, 0.0);
		assertEquals(0.0, result0.getRed(), EPSILON);
		assertEquals(0.0, result0.getGreen(), EPSILON);
		assertEquals(0.0, result0.getBlue(), EPSILON);

		// Test at various x values
		double[] tests = { -0.5, -0.25, 0.25, 0.5, 1.0 };

		for (double xVal : tests) {
			RGBColor result = myTree.evaluate(xVal, 0.0);
			double expected = Math.sin(xVal);

			assertEquals(expected, result.getRed(), EPSILON,
					"Red channel should be sin(x)");
			assertEquals(expected, result.getGreen(), EPSILON,
					"Green channel should be sin(x)");
			assertEquals(expected, result.getBlue(), EPSILON,
					"Blue channel should be sin(x)");
		}
	}

	@Test
	public void testSinOddSymmetry() {
		// sin(-x) = -sin(x)
		Sin myTree = new Sin(new X());

		double xVal = 0.7;
		RGBColor positive = myTree.evaluate(xVal, 0.0);
		RGBColor negative = myTree.evaluate(-xVal, 0.0);

		assertEquals(-positive.getRed(), negative.getRed(), EPSILON,
				"Red channel should satisfy sin(-x) = -sin(x)");
		assertEquals(-positive.getGreen(), negative.getGreen(), EPSILON,
				"Green channel should satisfy sin(-x) = -sin(x)");
		assertEquals(-positive.getBlue(), negative.getBlue(), EPSILON,
				"Blue channel should satisfy sin(-x) = -sin(x)");
	}

	@Test
	public void sinFunctionTests() {
		ExpressionTreeNode e = parser.makeExpression("sin(x)");
		assertEquals(new Sin(new X()), e);
	}

	@Test
	public void testToString() {
		ExpressionTreeNode sin = new Sin(new Y());
		assertEquals("Sin(y)", sin.toString());
	}

	@Test
	public void testEquals() {
		ExpressionTreeNode sin = new Sin(new Y());
		ExpressionTreeNode sinSame = new Sin(new Y());
		ExpressionTreeNode sinDifferent = new Sin(new X());

		assertEquals(sin, sin);
		assertEquals(sinSame, sin);

		assertNotEquals(sin, sinDifferent);
		assertNotEquals(sinDifferent, sinSame);

		// another unary function should not equal Sin
		ExpressionTreeNode other = new Floor(new Y());
		assertNotEquals(sin, other);
	}

	@Test
	public void testTokenizeBasicFunctionExpression() {
		String expression = "sin(x)";
		List<Token> tokens = tokenizer.parseTokens(expression);
		assertEquals(new SinToken(), tokens.get(0));
		assertEquals(new LeftParenToken(), tokens.get(1));
		assertEquals(new IdentifierToken("x"), tokens.get(2));
		assertEquals(new RightParenToken(), tokens.get(3));
	}

	@Test
	public void testTokenizeMoreComplexExpression() {
		String expression = "x + sin(y)";
		List<Token> tokens = tokenizer.parseTokens(expression);

		// Should contain at least one SinToken
		boolean foundSin = tokens.stream().anyMatch(t -> t instanceof SinToken);
		assertTrue(foundSin, "Expression 'x + sin(y)' should contain a SinToken");
	}
}