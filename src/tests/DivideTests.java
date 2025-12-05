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
import picasso.parser.language.expressions.Divide;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.operations.DivideToken;

/**
 * Tests for the Divide Operator .
 * 
 * @author Asya Yurkovskaya
 */

public class DivideTests {
	
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
	public void testDivideEvaluationBasic() {

		Divide myTree = new Divide(new X(), new Y());

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
			double expected = xVal / yVal;

			assertEquals(expected, result.getRed(), EPSILON,
					"Red channel should be x / y");
			assertEquals(expected, result.getGreen(), EPSILON,
					"Green channel should be x / y");
			assertEquals(expected, result.getBlue(), EPSILON,
					"Blue channel should be x / y");
		}
	}

	@Test
	public void divideOperatorTests() {
		//"x / y" should build Divide(X, Y)
		ExpressionTreeNode e = parser.makeExpression("x / y");
		assertEquals(new Divide(new X(), new Y()), e);
	}

	@Test
	public void testToString() {
		ExpressionTreeNode divide = new Divide(new X(), new Y());
		assertEquals("Divide(x, y)", divide.toString());
	}

	@Test
	public void testEquals() {
		ExpressionTreeNode divide = new Divide(new X(), new Y());
		ExpressionTreeNode divideSame = new Divide(new X(), new Y());
		ExpressionTreeNode divideDifferentOrder = new Divide(new Y(), new X());
		ExpressionTreeNode divideDifferent = new Divide(new X(), new X());

		assertEquals(divide, divide);
		assertEquals(divideSame, divide);

		assertNotEquals(divide, divideDifferentOrder);
		assertNotEquals(divide, divideDifferent);

		// another expression type should not equal Divide
		ExpressionTreeNode other = new Floor(new Y());
		assertNotEquals(divide, other);
	}

	@Test
	public void testTokenizeBasicDivideExpression() {
		String expression = "x / y";
		List<Token> tokens = tokenizer.parseTokens(expression);

		assertEquals(new IdentifierToken("x"), tokens.get(0));
		assertEquals(new DivideToken(), tokens.get(1));
		assertEquals(new IdentifierToken("y"), tokens.get(2));
	}

	@Test
	public void testTokenizeMoreComplexExpression() {
		String expression = "x / y / x";
		List<Token> tokens = tokenizer.parseTokens(expression);

		// Should contain at least one DivideToken
		boolean foundDivide = tokens.stream().anyMatch(t -> t instanceof DivideToken);
		assertTrue(foundDivide, "Expression 'x / y / x' should contain at least one DivideToken");
	}

}
