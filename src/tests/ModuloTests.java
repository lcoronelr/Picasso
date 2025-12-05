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
import picasso.parser.language.expressions.Modulo;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.operations.ModuloToken;

/**
 * Tests for the Modulo Operator .
 * 
 * @author Asya Yurkovskaya
 */

public class ModuloTests {
	
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
	public void testModuloEvaluationBasic() {

		Modulo myTree = new Modulo(new X(), new Y());

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
			double expected = xVal % yVal;

			assertEquals(expected, result.getRed(), EPSILON,
					"Red channel should be x % y");
			assertEquals(expected, result.getGreen(), EPSILON,
					"Green channel should be x % y");
			assertEquals(expected, result.getBlue(), EPSILON,
					"Blue channel should be x % y");
		}
	}

	@Test
	public void moduloOperatorTests() {
		//"x % y" should build Modulo(X, Y)
		ExpressionTreeNode e = parser.makeExpression("x % y");
		assertEquals(new Modulo(new X(), new Y()), e);
	}

	@Test
	public void testToString() {
		ExpressionTreeNode modulo = new Modulo(new X(), new Y());
		assertEquals("Modulo(x, y)", modulo.toString());
	}

	@Test
	public void testEquals() {
		ExpressionTreeNode modulo = new Modulo(new X(), new Y());
		ExpressionTreeNode moduloSame = new Modulo(new X(), new Y());
		ExpressionTreeNode moduloDifferentOrder = new Modulo(new Y(), new X());
		ExpressionTreeNode moduloDifferent = new Modulo(new X(), new X());

		assertEquals(modulo, modulo);
		assertEquals(moduloSame, modulo);

		assertNotEquals(modulo, moduloDifferentOrder);
		assertNotEquals(modulo, moduloDifferent);

		// another expression type should not equal Modulo
		ExpressionTreeNode other = new Floor(new Y());
		assertNotEquals(modulo, other);
	}

	@Test
	public void testTokenizeBasicModuloExpression() {
		String expression = "x % y";
		List<Token> tokens = tokenizer.parseTokens(expression);

		assertEquals(new IdentifierToken("x"), tokens.get(0));
		assertEquals(new ModuloToken(), tokens.get(1));
		assertEquals(new IdentifierToken("y"), tokens.get(2));
	}

	@Test
	public void testTokenizeMoreComplexExpression() {
		String expression = "x % y % x";
		List<Token> tokens = tokenizer.parseTokens(expression);

		// Should contain at least one ModuloToken
		boolean foundModulo = tokens.stream().anyMatch(t -> t instanceof ModuloToken);
		assertTrue(foundModulo, "Expression 'x % y % x' should contain at least one ModuloToken");
	}

}
