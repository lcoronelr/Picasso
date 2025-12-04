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
import picasso.parser.language.expressions.Ceil;
import picasso.parser.language.expressions.Floor;
import picasso.parser.language.expressions.Plus;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.chars.LeftParenToken;
import picasso.parser.tokens.chars.RightParenToken;
import picasso.parser.tokens.functions.CeilToken;

/**
 * 
 * @author Therese Elvira Mombou Gatsing
 */

public class CeilTests {
	
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
	public void testCeilEvaluation() {
		Ceil myTree = new Ceil(new X());

		
		assertEquals(new RGBColor(1, 1, 1), myTree.evaluate(.4, -1));
	    assertEquals(new RGBColor(1, 1, 1), myTree.evaluate(.999, -1));
	    assertEquals(new RGBColor(0, 0, 0), myTree.evaluate(-.7, -1));

	    
	    for (int i = -1; i <= 1; i++) {
	        assertEquals(new RGBColor(i, i, i), myTree.evaluate(i, -i));
	        assertEquals(new RGBColor(i, i, i), myTree.evaluate(i, i));
	    }

	   
	    double[] tests = { -.7, -.00001, .000001, .5 };

	    for (double testVal : tests) {
	        double ceilOfTestVal = Math.ceil(testVal);
	        RGBColor expected = new RGBColor(ceilOfTestVal, ceilOfTestVal, ceilOfTestVal);

	        assertEquals(expected, myTree.evaluate(testVal, -1));
	        assertEquals(expected, myTree.evaluate(testVal, testVal));
	    }
	}

	@Test
	public void ceilFunctionTests() {
		ExpressionTreeNode e = parser.makeExpression("ceil( x )");
		assertEquals(new Ceil(new X()), e);

		e = parser.makeExpression("ceil( x + y )");
		assertEquals(new Ceil(new Plus(new X(), new Y())), e);
	}

	@Test
	public void testToString() {
		ExpressionTreeNode ceil = new Ceil(new Y());
		assertEquals("Ceil(y)", ceil.toString());
	}

	@Test
	public void testEquals() {
		ExpressionTreeNode ceil = new Ceil(new Y());
		ExpressionTreeNode ceilSame = new Ceil(new Y());
		ExpressionTreeNode ceilDifferent = new Ceil(new X());

		assertEquals(ceil, ceil);
		assertEquals(ceilSame, ceil);

		assertNotEquals(ceil, ceilDifferent);
		assertNotEquals(ceilDifferent, ceilSame);

		ExpressionTreeNode other = new Floor(new Y());
		assertNotEquals(ceil, other);
	}

	@Test
	public void testTokenizeBasicFunctionExpression() {
		String expression = "ceil(x)";
		List<Token> tokens = tokenizer.parseTokens(expression);
		assertEquals(new CeilToken(), tokens.get(0));
		assertEquals(new LeftParenToken(), tokens.get(1));
		assertEquals(new IdentifierToken("x"), tokens.get(2));
		assertEquals(new RightParenToken(), tokens.get(3));
	}


}
