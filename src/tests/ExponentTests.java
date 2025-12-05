package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import picasso.parser.ExpressionTreeGenerator;
import picasso.parser.Tokenizer;
import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.Floor;
import picasso.parser.language.expressions.Exponent;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.operations.ExponentToken;

/**
 * Tests for the Exponent Operator .
 * 
 * @author Asya Yurkovskaya
 */

public class ExponentTests {
	
	private static ExpressionTreeGenerator parser;
	private Tokenizer tokenizer;
	private static final double EPSILON = 1e-9;

	/**
	 * 
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
	public void testExponentEvaluationBasic() {

		Exponent myTree = new Exponent(new X(), new Y());

		double[][] tests = {
				{ 0.0, 0.0 },
				{ 0.5, 0.25 },
				{ -0.5, 0.25 },
				{ -0.3, -0.2 },
				{ 1.0, -1.0 }
		};

		for (double[] pair : tests) {
			double xVal = pair[0];
			double yVal = pair[1];

			RGBColor result = myTree.evaluate(xVal, yVal);
			double expected = Math.pow(xVal, yVal);

			assertEquals(expected, result.getRed(), EPSILON,
					"Red channel should be x ^ y");
			assertEquals(expected, result.getGreen(), EPSILON,
					"Green channel should be x ^ y");
			assertEquals(expected, result.getBlue(), EPSILON,
					"Blue channel should be x ^ y");
		}
	}

	@Test
	public void exponentOperatorTests() {
		//"x ^ y" should build Exponent(X, Y)
		ExpressionTreeNode e = parser.makeExpression("x ^ y");
		assertEquals(new Exponent(new X(), new Y()), e);
	}

	@Test
	public void testToString() {
		ExpressionTreeNode exponent = new Exponent(new X(), new Y());
		assertEquals("Exponent(x, y)", exponent.toString());
	}

	@Test
	public void testEquals() {
		ExpressionTreeNode exponent = new Exponent(new X(), new Y());
		ExpressionTreeNode exponentSame = new Exponent(new X(), new Y());
		ExpressionTreeNode exponentDifferentOrder = new Exponent(new Y(), new X());
		ExpressionTreeNode exponentDifferent = new Exponent(new X(), new X());

		assertEquals(exponent, exponent);
		assertEquals(exponentSame, exponent);

		assertNotEquals(exponent, exponentDifferentOrder);
		assertNotEquals(exponent, exponentDifferent);

		// another expression type should not equal Exponent
		ExpressionTreeNode other = new Floor(new Y());
		assertNotEquals(exponent, other);
	}

	@Test
	public void testTokenizeBasicExponentExpression() {
		String expression = "x ^ y";
		List<Token> tokens = tokenizer.parseTokens(expression);

		assertEquals(new IdentifierToken("x"), tokens.get(0));
		assertEquals(new ExponentToken(), tokens.get(1));
		assertEquals(new IdentifierToken("y"), tokens.get(2));
	}

	@Test
	public void testTokenizeMoreComplexExpression() {
		String expression = "x ^ y ^ x";
		List<Token> tokens = tokenizer.parseTokens(expression);

		// Should contain at least one ExponentToken
		boolean foundExponent = tokens.stream().anyMatch(t -> t instanceof ExponentToken);
		assertTrue(foundExponent, "Expression 'x ^ y ^ x' should contain at least one ExponentToken");
	}

}
