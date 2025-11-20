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
import picasso.parser.language.expressions.Plus;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.Exp;
import picasso.parser.language.expressions.Floor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.chars.LeftParenToken;
import picasso.parser.tokens.chars.RightParenToken;
import picasso.parser.tokens.functions.ExpToken;

/**
 * Tests for the Exp function .
 * 
 * 
 * 
 * @author Therese Elvira Mombou Gatsing
 */

public class ExpTests {
	
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
	public void testExpEvaluationBasic() {

		Exp myTree = new Exp(new X());

		RGBColor result0 = myTree.evaluate(0.0, 0.0);
		assertEquals(1.0, result0.getRed(), EPSILON);
		assertEquals(1.0, result0.getGreen(), EPSILON);
		assertEquals(1.0, result0.getBlue(), EPSILON);

		double[] tests = { -1.0, -0.5, -0.25, 0.25, 0.5, 1.0 };

		for (double xVal : tests) {
			RGBColor result = myTree.evaluate(xVal, 0.0);
			double expected = Math.exp(xVal);

			assertEquals(expected, result.getRed(), EPSILON,
					"Red channel should be exp(x)");
			assertEquals(expected, result.getGreen(), EPSILON,
					"Green channel should be exp(x)");
			assertEquals(expected, result.getBlue(), EPSILON,
					"Blue channel should be exp(x)");
		}
	}

	@Test
	public void testExpIsAlwaysPositive() {
		Exp myTree = new Exp(new X());

		double[] tests = { -3.0, -1.0, -0.5, 0.0, 0.5, 1.0, 2.0 };

		for (double xVal : tests) {
			RGBColor result = myTree.evaluate(xVal, 0.0);

			assertTrue(result.getRed() > 0.0,
					"Red channel of exp(x) should be > 0");
			assertTrue(result.getGreen() > 0.0,
					"Green channel of exp(x) should be > 0");
			assertTrue(result.getBlue() > 0.0,
					"Blue channel of exp(x) should be > 0");
		}
	}

	@Test
	public void expFunctionTests() {
		ExpressionTreeNode e = parser.makeExpression("exp(x)");
		assertEquals(new Exp(new X()), e);

		
		 e = parser.makeExpression("exp( x + y )");
		assertEquals(new Exp(new Plus(new X(), new Y())), e);
	}

	@Test
	public void testToString() {
		ExpressionTreeNode exp = new Exp(new Y());
		assertEquals("Exp(y)", exp.toString());
	}

	@Test
	public void testEquals() {
		ExpressionTreeNode exp = new Exp(new Y());
		ExpressionTreeNode expSame = new Exp(new Y());
		ExpressionTreeNode expDifferent = new Exp(new X());

		assertEquals(exp, exp);
		assertEquals(expSame, exp);

		assertNotEquals(exp, expDifferent);
		assertNotEquals(expDifferent, expSame);

		// another unary function should not equal Exp
		ExpressionTreeNode other = new Floor(new Y());
		assertNotEquals(exp, other);
	}

	@Test
	public void testTokenizeBasicFunctionExpression() {
		String expression = "exp(x)";
		List<Token> tokens = tokenizer.parseTokens(expression);
		assertEquals(new ExpToken(), tokens.get(0));
		assertEquals(new LeftParenToken(), tokens.get(1));
		assertEquals(new IdentifierToken("x"), tokens.get(2));
		assertEquals(new RightParenToken(), tokens.get(3));
	}

	@Test
	public void testTokenizeMoreComplexExpression() {
		String expression = "x + exp(y)";
		List<Token> tokens = tokenizer.parseTokens(expression);

		// Should contain at least one ExpToken
		boolean foundExp = tokens.stream().anyMatch(t -> t instanceof ExpToken);
		assertTrue(foundExp, "Expression 'x + exp(y)' should contain an ExpToken");
	}

}
