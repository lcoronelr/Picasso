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
 * This organization doesn't take advantage of the @BeforeEach set up step, but
 * it may be easier to think about the tests this way. (Duplicates some tests
 * from other JUnit Test Classes.)
 * 
 * @author Menilik Deneke
 */
class ClampTests {

	private static ExpressionTreeGenerator parser;
	private Tokenizer tokenizer;

	/**
	 * Just need to get the semantic analyzer once. Calling getInstance again
	 * doesn't get a new/fresh instance.
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

}
