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
import picasso.parser.language.expressions.Multiply;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.operations.MultiplyToken;

/**
 * Tests for the Multiply Operator .
 * 
 * @author Asya Yurkovskaya
 */

public class MultiplyTests {
	
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
	public void testMultiplyEvaluationBasic() {

		Multiply myTree = new Multiply(new X(), new Y());

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
			double expected = xVal * yVal;

			assertEquals(expected, result.getRed(), EPSILON,
					"Red channel should be x * y");
			assertEquals(expected, result.getGreen(), EPSILON,
					"Green channel should be x * y");
			assertEquals(expected, result.getBlue(), EPSILON,
					"Blue channel should be x * y");
		}
	}

	@Test
	public void multiplyOperatorTests() {
		//"x * y" should build Multiply(X, Y)
		ExpressionTreeNode e = parser.makeExpression("x * y");
		assertEquals(new Multiply(new X(), new Y()), e);
	}

	@Test
	public void testToString() {
		ExpressionTreeNode multiply = new Multiply(new X(), new Y());
		assertEquals("Multiply(x, y)", multiply.toString());
	}

	@Test
	public void testEquals() {
		ExpressionTreeNode multiply = new Multiply(new X(), new Y());
		ExpressionTreeNode multiplySame = new Multiply(new X(), new Y());
		ExpressionTreeNode multiplyDifferentOrder = new Multiply(new Y(), new X());
		ExpressionTreeNode multiplyDifferent = new Multiply(new X(), new X());

		assertEquals(multiply, multiply);
		assertEquals(multiplySame, multiply);

		assertNotEquals(multiply, multiplyDifferentOrder);
		assertNotEquals(multiply, multiplyDifferent);

		// another expression type should not equal Multiply
		ExpressionTreeNode other = new Floor(new Y());
		assertNotEquals(multiply, other);
	}

	@Test
	public void testTokenizeBasicMultiplyExpression() {
		String expression = "x * y";
		List<Token> tokens = tokenizer.parseTokens(expression);

		assertEquals(new IdentifierToken("x"), tokens.get(0));
		assertEquals(new MultiplyToken(), tokens.get(1));
		assertEquals(new IdentifierToken("y"), tokens.get(2));
	}

	@Test
	public void testTokenizeMoreComplexExpression() {
		String expression = "x * y * x";
		List<Token> tokens = tokenizer.parseTokens(expression);

		// Should contain at least one MultiplyToken
		boolean foundMultiply = tokens.stream().anyMatch(t -> t instanceof MultiplyToken);
		assertTrue(foundMultiply, "Expression 'x * y * x' should contain at least one MultiplyToken");
	}

}
