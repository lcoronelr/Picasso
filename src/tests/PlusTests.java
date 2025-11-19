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
import picasso.parser.language.expressions.Plus;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.operations.PlusToken;

/**
 * Tests for the Plus Operator .
 * 
 * 
 * 
 * @author Therese Elvira Mombou Gatsing
 */

public class PlusTests {
	
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
	public void testPlusEvaluationBasic() {

		Plus myTree = new Plus(new X(), new Y());

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
			double expected = xVal + yVal;

			assertEquals(expected, result.getRed(), EPSILON,
					"Red channel should be x + y");
			assertEquals(expected, result.getGreen(), EPSILON,
					"Green channel should be x + y");
			assertEquals(expected, result.getBlue(), EPSILON,
					"Blue channel should be x + y");
		}
	}

	@Test
	public void plusOperatorTests() {
		//"x + y" should build Plus(X, Y)
		ExpressionTreeNode e = parser.makeExpression("x + y");
		assertEquals(new Plus(new X(), new Y()), e);
	}

	@Test
	public void testToString() {
		ExpressionTreeNode plus = new Plus(new X(), new Y());
		assertEquals("Plus(x, y)", plus.toString());
	}

	@Test
	public void testEquals() {
		ExpressionTreeNode plus = new Plus(new X(), new Y());
		ExpressionTreeNode plusSame = new Plus(new X(), new Y());
		ExpressionTreeNode plusDifferentOrder = new Plus(new Y(), new X());
		ExpressionTreeNode plusDifferent = new Plus(new X(), new X());

		assertEquals(plus, plus);
		assertEquals(plusSame, plus);

		assertNotEquals(plus, plusDifferentOrder);
		assertNotEquals(plus, plusDifferent);

		// another expression type should not equal Plus
		ExpressionTreeNode other = new Floor(new Y());
		assertNotEquals(plus, other);
	}

	@Test
	public void testTokenizeBasicPlusExpression() {
		String expression = "x + y";
		List<Token> tokens = tokenizer.parseTokens(expression);

		assertEquals(new IdentifierToken("x"), tokens.get(0));
		assertEquals(new PlusToken(), tokens.get(1));
		assertEquals(new IdentifierToken("y"), tokens.get(2));
	}

	@Test
	public void testTokenizeMoreComplexExpression() {
		String expression = "x + y + x";
		List<Token> tokens = tokenizer.parseTokens(expression);

		// Should contain at least one PlusToken
		boolean foundPlus = tokens.stream().anyMatch(t -> t instanceof PlusToken);
		assertTrue(foundPlus, "Expression 'x + y + x' should contain at least one PlusToken");
	}
	

}
