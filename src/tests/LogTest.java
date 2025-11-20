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
import picasso.parser.language.expressions.Floor;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.chars.LeftParenToken;
import picasso.parser.tokens.chars.RightParenToken;
import picasso.parser.tokens.functions.FloorToken;

import picasso.parser.language.expressions.Log;
import picasso.parser.language.expressions.Plus;
import picasso.parser.tokens.functions.LogToken;

/**
 * This organization doesn't take advantage of the @BeforeEach set up step, but
 * it may be easier to think about the tests this way. (Duplicates some tests
 * from other JUnit Test Classes.)
 * 
 * @author Abhishek Pradhan
 */
class LogTests{

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
	public void testLogEvaluation() {
		Log myTree = new Log(new X());

		//log(1)==0
		RGBColor c1 = myTree.evaluate(1.0, 0.0);
        assertEquals(0.0, c1.getRed(), 1e-10);
        assertEquals(0.0, c1.getGreen(), 1e-10);
        assertEquals(0.0, c1.getBlue(), 1e-10);
        
        //log(|-1|)==0
        RGBColor c2 = myTree.evaluate(-1.0, 0.0);
        assertEquals(0.0, c2.getRed(), 1e-10);
        assertEquals(0.0, c2.getGreen(), 1e-10);
        assertEquals(0.0, c2.getBlue(), 1e-10);

        double expected = Math.log(2.0);
        RGBColor c3 = myTree.evaluate(2.0, 0.0);
        assertEquals(expected, c3.getRed(), 1e-10);
        assertEquals(expected, c3.getGreen(), 1e-10);
        assertEquals(expected, c3.getBlue(), 1e-10);
        
	}
    @Test
    void testLogSymmetry() {
        Log logX = new Log(new X());

        double v = 0.5;
        double expected = Math.log(Math.abs(v));

        RGBColor pos = logX.evaluate(v, 0.0);
        RGBColor neg = logX.evaluate(-v, 0.0);

        assertEquals(expected, pos.getRed(), 1e-10);
        assertEquals(expected, neg.getRed(), 1e-10);
    }

    
	
	@Test
	public void LogFunctionTests() {
		ExpressionTreeNode e = parser.makeExpression("log(x)");
		assertEquals(new Log(new X()), e);

		e = parser.makeExpression("log(x + y)");
		assertEquals(new Log(new Plus(new X(), new Y())), e);
	}

	@Test
	public void testToString() {
		ExpressionTreeNode log = new Log(new Y());
		assertEquals("Log(y)", log.toString());
	}

	@Test
	public void testEquals() {
		ExpressionTreeNode log = new Log(new Y());
		ExpressionTreeNode logSame = new Log(new Y());
		ExpressionTreeNode logDifferent = new Log(new X());

		assertEquals(log, log);
		assertEquals(logSame, log);

		assertNotEquals(log, logDifferent);
		assertNotEquals(logDifferent, logSame);

	}

	@Test
	public void testTokenizeBasicFunctionExpression() {
		String expression = "log(x)";
		List<Token> tokens = tokenizer.parseTokens(expression);
		assertEquals(new LogToken(), tokens.get(0));
		assertEquals(new LeftParenToken(), tokens.get(1));
		assertEquals(new IdentifierToken("x"), tokens.get(2));
		assertEquals(new RightParenToken(), tokens.get(3));
	}

}
