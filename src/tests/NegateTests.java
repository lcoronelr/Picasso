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
import picasso.parser.language.expressions.Negate;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.X;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.operations.NegateToken;

/**
 * Tests for the Negate (!) operator.
 * 
 * @author Therese Elvira Mombou Gatsing
 */
public class NegateTests {

	private static ExpressionTreeGenerator parser;
	private Tokenizer tokenizer;
	private static final double EPSILON = 1e-9;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		parser = new ExpressionTreeGenerator();
	}

	@BeforeEach
	public void setUp() throws Exception {
		tokenizer = new Tokenizer();
	}

	@Test
	public void testNegateEvaluationBasic() {

		Negate myTree = new Negate(new X());

		double[] tests = { 0.0, 0.5, -0.5, -0.3, 1.0 };

		for (double xVal : tests) {

			RGBColor result = myTree.evaluate(xVal, 0.0);
			double expected = -xVal;

			assertEquals(expected, result.getRed(), EPSILON,
					"Red channel should be -x");
			assertEquals(expected, result.getGreen(), EPSILON,
					"Green channel should be -x");
			assertEquals(expected, result.getBlue(), EPSILON,
					"Blue channel should be -x");
		}
	}

	@Test
	public void negateOperatorTests() {
		// "!x" should build Negate(X)
		ExpressionTreeNode e = parser.makeExpression("!x");
		assertEquals(new Negate(new X()), e);
	}

	@Test
	public void testToString() {
		ExpressionTreeNode negate = new Negate(new X());
		assertEquals("Negate(x)", negate.toString());
	}

	@Test
	public void testEquals() {
		ExpressionTreeNode negate = new Negate(new X());
		ExpressionTreeNode negateSame = new Negate(new X());
		ExpressionTreeNode negateDifferent = new Negate(new Negate(new X()));

		assertEquals(negate, negate);
		assertEquals(negateSame, negate);

		assertNotEquals(negate, negateDifferent);

		// another expression type should not equal Negate
		ExpressionTreeNode other = new Floor(new X());
		assertNotEquals(negate, other);
	}

	@Test
	public void testTokenizeBasicNegateExpression() {
		String expression = "!x";
		List<Token> tokens = tokenizer.parseTokens(expression);

		assertEquals(new NegateToken(), tokens.get(0));
		assertEquals(new IdentifierToken("x"), tokens.get(1));
	}

	@Test
	public void testTokenizeMoreComplexExpression() {
		String expression = "x + !x";
		List<Token> tokens = tokenizer.parseTokens(expression);

		// Should contain at least one NegateToken
		boolean foundNegate = tokens.stream().anyMatch(t -> t instanceof NegateToken);
		assertTrue(foundNegate, "Expression 'x + !x' should contain at least one NegateToken");
	}
	
	@Test
    public void testNegateOfFloorEvaluation() {
        ExpressionTreeNode inner = new Floor(new X());   // floor(x)
        Negate neg = new Negate(inner);                  // -floor(x)

        double[] tests = { -0.9, -0.2, 0.0, 0.3, 0.9 };

        for (double xVal : tests) {
            RGBColor result = neg.evaluate(xVal, 0.0);
            double expected = -Math.floor(xVal);

            assertEquals(expected, result.getRed(),   EPSILON);
            assertEquals(expected, result.getGreen(), EPSILON);
            assertEquals(expected, result.getBlue(),  EPSILON);
        }
    }

    
    @Test
    public void testParserNegateWithAndWithoutParentheses() {
        ExpressionTreeNode withoutParens = parser.makeExpression("!x");
        ExpressionTreeNode withParens    = parser.makeExpression("!(x)");

        assertEquals(withoutParens, withParens,
                "Parser should treat !x and !(x) as the same expression");
    }

   
    @Test
    public void testParserNegateOfFunction() {
        ExpressionTreeNode parsed   = parser.makeExpression("!floor(x)");
        ExpressionTreeNode expected = new Negate(new Floor(new X()));

        assertEquals(expected, parsed,
                "Parser should create Negate(Floor(X)) for '!floor(x)'");
    }

    
    @Test
    public void testTokenizeNegateWithWhitespace() {
        String expression = "   !   x   ";
        List<Token> tokens = tokenizer.parseTokens(expression);

        assertEquals(2, tokens.size());
        assertEquals(new NegateToken(),       tokens.get(0));
        assertEquals(new IdentifierToken("x"),tokens.get(1));
    }

}
