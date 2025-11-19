/**
 * 
 */
package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import picasso.parser.ExpressionTreeGenerator;
import picasso.parser.Tokenizer;
import picasso.parser.language.ExpressionTreeNode;
//import picasso.parser.language.expressions.Plus;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.Tan;
import picasso.parser.language.expressions.Floor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.chars.LeftParenToken;
import picasso.parser.tokens.chars.RightParenToken;
import picasso.parser.tokens.functions.TanToken;

/**
 * Tests for the Tan function .
 * 
 * 
 * 
 * @author Therese Elvira Mombou Gatsing
 */
public class TanTests {

	private static ExpressionTreeGenerator parser;
	private Tokenizer tokenizer;
	private static final double EPSILON = 1e-9;

	/**
	 * 
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
	public void testTanEvaluationBasic() {
		
		Tan myTree = new Tan(new X());

		RGBColor result0 = myTree.evaluate(0.0, 0.0);
		assertEquals(0.0, result0.getRed(), EPSILON);
		assertEquals(0.0, result0.getGreen(), EPSILON);
		assertEquals(0.0, result0.getBlue(), EPSILON);

		double[] tests = { -0.5, -0.25, 0.25, 0.5, 1.0 };

		for (double xVal : tests) {
			RGBColor result = myTree.evaluate(xVal, 0.0);
			double expected = Math.tan(xVal);

			assertEquals(expected, result.getRed(), EPSILON,
					"Red channel should be tan(x)");
			assertEquals(expected, result.getGreen(), EPSILON,
					"Green channel should be tan(x)");
			assertEquals(expected, result.getBlue(), EPSILON,
					"Blue channel should be tan(x)");
		}
	}

	@Test
	public void testTanOddSymmetry() {
		// tan(-x) = -tan(x)
		Tan myTree = new Tan(new X());

		double xVal = 0.7;
		RGBColor positive = myTree.evaluate(xVal, 0.0);
		RGBColor negative = myTree.evaluate(-xVal, 0.0);

		assertEquals(-positive.getRed(), negative.getRed(), EPSILON,
				"Red channel should satisfy tan(-x) = -tan(x)");
		assertEquals(-positive.getGreen(), negative.getGreen(), EPSILON,
				"Green channel should satisfy tan(-x) = -tan(x)");
		assertEquals(-positive.getBlue(), negative.getBlue(), EPSILON,
				"Blue channel should satisfy tan(-x) = -tan(x)");
	}

	@Test
	public void tanFunctionTests() {
		ExpressionTreeNode e = parser.makeExpression("tan(x)");
		assertEquals(new Tan(new X()), e);

		//e = parser.makeExpression("tan( x + y )");
		//assertEquals(new Tangent(new Plus(new X(), new Y())), e);
	}

	@Test
	public void testToString() {
		ExpressionTreeNode tan = new Tan(new Y());
		assertEquals("Tan(y)", tan.toString());
	}

	@Test
	public void testEquals() {
		ExpressionTreeNode tan = new Tan(new Y());
		ExpressionTreeNode tanSame = new Tan(new Y());
		ExpressionTreeNode tanDifferent = new Tan(new X());

		assertEquals(tan, tan);
		assertEquals(tanSame, tan);

		assertNotEquals(tan, tanDifferent);
		assertNotEquals(tanDifferent, tanSame);

		// another unary function should not equal Tan
		ExpressionTreeNode other = new Floor(new Y());
		assertNotEquals(tan, other);
	}

	@Test
	public void testTokenizeBasicFunctionExpression() {
		String expression = "tan(x)";
		List<Token> tokens = tokenizer.parseTokens(expression);
		assertEquals(new TanToken(), tokens.get(0));
		assertEquals(new LeftParenToken(), tokens.get(1));
		assertEquals(new IdentifierToken("x"), tokens.get(2));
		assertEquals(new RightParenToken(), tokens.get(3));
	}

	@Test
	public void testTokenizeMoreComplexExpression() {
		String expression = "x + tan(y)";
		List<Token> tokens = tokenizer.parseTokens(expression);

		// Should contain at least one TanToken 
		boolean foundTan = tokens.stream().anyMatch(t -> t instanceof TanToken);
		assertTrue(foundTan, "Expression 'x + tan(y)' should contain a TanToken");
	}
}
