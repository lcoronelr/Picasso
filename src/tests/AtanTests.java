
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
// import picasso.parser.language.expressions.Addition;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.Atangent;
import picasso.parser.language.expressions.Floor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.chars.LeftParenToken;
import picasso.parser.tokens.chars.RightParenToken;
import picasso.parser.tokens.functions.AtanToken;

/**
 * Tests for the arctangent (atan) function.
 * 
 * 
 * 
 * @author Therese Elvira Mombou Gatsing
 */
public class AtanTests {

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
	public void testAtanEvaluationBasic() {

		Atangent myTree = new Atangent(new X());

		RGBColor result0 = myTree.evaluate(0.0, 0.0);
		assertEquals(0.0, result0.getRed(), EPSILON);
		assertEquals(0.0, result0.getGreen(), EPSILON);
		assertEquals(0.0, result0.getBlue(), EPSILON);

		double[] tests = { -1.0, -0.5, -0.25, 0.25, 0.5, 1.0 };

		for (double xVal : tests) {
			RGBColor result = myTree.evaluate(xVal, 0.0);
			double expected = Math.atan(xVal);

			assertEquals(expected, result.getRed(), EPSILON,
					"Red channel should be atan(x)");
			assertEquals(expected, result.getGreen(), EPSILON,
					"Green channel should be atan(x)");
			assertEquals(expected, result.getBlue(), EPSILON,
					"Blue channel should be atan(x)");
		}
	}

	@Test
	public void testAtanOddSymmetry() {
		// atan(-x) = -atan(x)
		Atangent myTree = new Atangent(new X());

		double xVal = 0.7;
		RGBColor positive = myTree.evaluate(xVal, 0.0);
		RGBColor negative = myTree.evaluate(-xVal, 0.0);

		assertEquals(-positive.getRed(), negative.getRed(), EPSILON,
				"Red channel should satisfy atan(-x) = -atan(x)");
		assertEquals(-positive.getGreen(), negative.getGreen(), EPSILON,
				"Green channel should satisfy atan(-x) = -atan(x)");
		assertEquals(-positive.getBlue(), negative.getBlue(), EPSILON,
				"Blue channel should satisfy atan(-x) = -atan(x)");
	}

	@Test
	public void atanFunctionTests() {
		ExpressionTreeNode e = parser.makeExpression("atan(x)");
		assertEquals(new Atangent(new X()), e);

		
		// e = parser.makeExpression("atan( x + y )");
		// assertEquals(new Atangent(new Addition(new X(), new Y())), e);
	}

	@Test
	public void testToString() {
		ExpressionTreeNode atan = new Atangent(new Y());
		assertEquals("Atangent(y)", atan.toString());
	}

	@Test
	public void testEquals() {
		ExpressionTreeNode atan = new Atangent(new Y());
		ExpressionTreeNode atanSame = new Atangent(new Y());
		ExpressionTreeNode atanDifferent = new Atangent(new X());

		assertEquals(atan, atan);
		assertEquals(atanSame, atan);

		assertNotEquals(atan, atanDifferent);
		assertNotEquals(atanDifferent, atanSame);

		// another unary function should not equal Atangent
		ExpressionTreeNode other = new Floor(new Y());
		assertNotEquals(atan, other);
	}

	@Test
	public void testTokenizeBasicFunctionExpression() {
		String expression = "atan(x)";
		List<Token> tokens = tokenizer.parseTokens(expression);
		assertEquals(new AtanToken(), tokens.get(0));
		assertEquals(new LeftParenToken(), tokens.get(1));
		assertEquals(new IdentifierToken("x"), tokens.get(2));
		assertEquals(new RightParenToken(), tokens.get(3));
	}

	@Test
	public void testTokenizeMoreComplexExpression() {
		String expression = "x + atan(y)";
		List<Token> tokens = tokenizer.parseTokens(expression);

		// Should contain at least one AtanToken
		boolean foundAtan = tokens.stream().anyMatch(t -> t instanceof AtanToken);
		assertTrue(foundAtan, "Expression 'x + atan(y)' should contain an AtanToken");
	}
}
