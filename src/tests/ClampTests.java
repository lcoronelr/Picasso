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
import picasso.parser.SemanticAnalyzer;
import picasso.parser.Tokenizer;
import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.Clamp;
import picasso.parser.language.expressions.Floor;
import picasso.parser.language.expressions.Plus;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.chars.LeftParenToken;
import picasso.parser.tokens.chars.RightParenToken;
import picasso.parser.tokens.functions.ClampToken;

/**
 * Tests for the Clamp function
 * 
 * @author Menilik Deneke
 * @author Therese Elvira Mombou Gatsing
 */
class ClampTests {

	private static ExpressionTreeGenerator parser;
	private Tokenizer tokenizer;

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
	public void testClampEvaluation() {
		Clamp myTree = new Clamp(new X());
		
		// Test above range
	    assertEquals(new RGBColor(1, 1, 1), myTree.evaluate(2.5, -1));
	    assertEquals(new RGBColor(1, 1, 1), myTree.evaluate(1.0001, -1));

	    // Test below range  
	    assertEquals(new RGBColor(-1, -1, -1), myTree.evaluate(-2.5, -1));
	    assertEquals(new RGBColor(-1, -1, -1), myTree.evaluate(-1.0001, -1));

	    // Test exact boundaries
	    assertEquals(new RGBColor(1, 1, 1), myTree.evaluate(1.0, -1));
	    assertEquals(new RGBColor(-1, -1, -1), myTree.evaluate(-1.0, -1));

	    // Test within range
	    assertEquals(new RGBColor(0.5, 0.5, 0.5), myTree.evaluate(0.5, -1));
	    assertEquals(new RGBColor(-0.7, -0.7, -0.7), myTree.evaluate(-0.7, -1));
	    assertEquals(new RGBColor(0, 0, 0), myTree.evaluate(0, 0));

	    // Test edge cases
	    assertEquals(new RGBColor(1, 1, 1), myTree.evaluate(Double.MAX_VALUE, 0));
	    assertEquals(new RGBColor(-1, -1, -1), myTree.evaluate(-Double.MAX_VALUE, 0));
	    
	}

	@Test
	public void clampFunctionTests() {
		ExpressionTreeNode e = parser.makeExpression("clamp( x )");
		assertEquals(new Clamp(new X()), e);

		e = parser.makeExpression("clamp( x + y )");
		assertEquals(new Clamp(new Plus(new X(), new Y())), e);
	}

	@Test
	public void testToString() {
		ExpressionTreeNode clamp = new Clamp(new Y());
		assertEquals("Clamp(y)", clamp.toString());
	}

	@Test
	public void testEquals() {
		ExpressionTreeNode clamp = new Clamp(new X());
		ExpressionTreeNode clampSame = new Clamp(new X());
		ExpressionTreeNode clampDifferent = new Clamp(new Y());

		assertEquals(clamp, clamp);
		assertEquals(clampSame, clamp);

		assertNotEquals(clamp, clampDifferent);
		assertNotEquals(clampDifferent, clampSame);
		
		ExpressionTreeNode floor = new Floor(new X());
		assertNotEquals(
			    clamp.evaluate(0.7, 0),  // Clamp(0.7) = 0.7
			    floor.evaluate(0.7, 0)   // Floor(0.7) = 0
			);

		assertNotEquals(
			   clamp.evaluate(-0.3, 0),  // Clamp(-0.3) = -0.3  
			   floor.evaluate(-0.3, 0)   // Floor(-0.3) = -1
			);
	}

	@Test
	public void testTokenizeBasicFunctionExpression() {
		String expression = "clamp(x)";
		List<Token> tokens = tokenizer.parseTokens(expression);
		assertEquals(new ClampToken(), tokens.get(0));
		assertEquals(new LeftParenToken(), tokens.get(1));
		assertEquals(new IdentifierToken("x"), tokens.get(2));
		assertEquals(new RightParenToken(), tokens.get(3));
	}
	
	
	@Test
    public void testClampMatchesChildWithinRange() {
        Clamp clamp = new Clamp(new X());
        X child = new X();

        double[] tests = { -1.0, -0.5, 0.0, 0.5, 0.9, 1.0 };

        for (double xVal : tests) {
            RGBColor clamped = clamp.evaluate(xVal, 0.0);
            RGBColor direct  = child.evaluate(xVal, 0.0);

            assertEquals(direct, clamped,
                    "Clamp(x) should not change values already in [-1,1]");
        }
    }

    
    @Test
    public void testClampConstantColorIgnoresCoordinates() {
        ExpressionTreeNode constant = new RGBColor(0.3, -0.4, 0.8);
        Clamp clamp = new Clamp(constant);

        RGBColor c1 = clamp.evaluate(-1.0, 0.5);
        RGBColor c2 = clamp.evaluate(0.9, -0.7);

        assertEquals(c1, c2, "Clamp of a constant color should be coordinate-independent");
    }

   
    @Test
    public void testParserNestedClampExpression() {
        ExpressionTreeNode parsed   = parser.makeExpression("clamp(clamp(x))");
        ExpressionTreeNode expected = new Clamp(new Clamp(new X()));

        assertEquals(expected, parsed,
                "Parser should create nested Clamp(Clamp(X)) for 'clamp(clamp(x))'");
    }

    
    @Test
    public void testParserClampWithComplexInnerExpression() {
        ExpressionTreeNode parsed   = parser.makeExpression("clamp(x + y)");
        ExpressionTreeNode expected = new Clamp(new Plus(new X(), new Y()));

        // Structural equality
        assertEquals(expected, parsed);

        double x = 0.3;
        double y = -0.2;
        RGBColor parsedVal   = parsed.evaluate(x, y);
        RGBColor expectedVal = expected.evaluate(x, y);

        assertEquals(expectedVal, parsedVal,
                "Parsed Clamp(x + y) should evaluate the same as manually built tree");
    }

   
    @Test
    public void testClampEqualsNullAndDifferentType() {
        ExpressionTreeNode clamp = new Clamp(new X());

        assertNotEquals(clamp, null, "Clamp expression should not equal null");
        assertNotEquals(clamp, "clamp(x)",
                "Clamp expression should not equal an arbitrary non-expression object");
    }

    
    @Test
    public void testTokenizeClampWithWhitespaceAndExpression() {
        String expression = "   clamp   (   x + y   )  ";
        List<Token> tokens = tokenizer.parseTokens(expression);

        assertFalse(tokens.isEmpty(), "Tokenizer should produce tokens for clamp expression");
        assertEquals(new ClampToken(),    tokens.get(0), "First token should be ClampToken");
        assertEquals(new LeftParenToken(),tokens.get(1), "Second token should be '('");

        // There should be an x and a y identifier somewhere in the middle
        assertTrue(tokens.stream().anyMatch(t -> t.equals(new IdentifierToken("x"))),
                "Tokens should contain identifier 'x'");
        assertTrue(tokens.stream().anyMatch(t -> t.equals(new IdentifierToken("y"))),
                "Tokens should contain identifier 'y'");

        assertEquals(new RightParenToken(), tokens.get(tokens.size() - 1),
                "Last token should be ')'");
    }

}
