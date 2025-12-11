package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import picasso.parser.ExpressionTreeGenerator;
import picasso.parser.Tokenizer;
import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.Clamp;
import picasso.parser.language.expressions.Floor;
import picasso.parser.language.expressions.Plus;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.Wrap;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.chars.LeftParenToken;
import picasso.parser.tokens.chars.RightParenToken;
import picasso.parser.tokens.functions.ClampToken;
import picasso.parser.tokens.functions.WrapToken;

public class WrapTests {
	
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
    public void testWrapEvaluation() {
        Wrap wrap = new Wrap(new X());
        
        // Test values above range
        assertEquals(new RGBColor(-0.5, -0.5, -0.5), wrap.evaluate(1.5, -1));
        assertEquals(new RGBColor(0.0, 0.0, 0.0), wrap.evaluate(2.0, -1));
        assertEquals(new RGBColor(0.1, 0.1, 0.1), wrap.evaluate(2.1, -1));
        assertEquals(new RGBColor(-0.9, -0.9, -0.9), wrap.evaluate(1.1, -1));
        
        // Test values that need below range
        assertEquals(new RGBColor(0.5, 0.5, 0.5), wrap.evaluate(-1.5, -1));
        assertEquals(new RGBColor(0.0, 0.0, 0.0), wrap.evaluate(-2.0, -1));
        assertEquals(new RGBColor(-0.1, -0.1, -0.1), wrap.evaluate(-2.1, -1));
        assertEquals(new RGBColor(0.9, 0.9, 0.9), wrap.evaluate(-1.1, -1));
        
        // Test values within range
        assertEquals(new RGBColor(1.0, 1.0, 1.0), wrap.evaluate(1.0, -1));
        assertEquals(new RGBColor(-1.0, -1.0, -1.0), wrap.evaluate(-1.0, -1));
        assertEquals(new RGBColor(0.5, 0.5, 0.5), wrap.evaluate(0.5, -1));
        assertEquals(new RGBColor(-0.7, -0.7, -0.7), wrap.evaluate(-0.7, -1));
        assertEquals(new RGBColor(0.0, 0.0, 0.0), wrap.evaluate(0.0, 0));
        
        // Test boundary cases
        assertEquals(new RGBColor(1.0, 1.0, 1.0), wrap.evaluate(3.0, -1));
        assertEquals(new RGBColor(-1.0, -1.0, -1.0), wrap.evaluate(-3.0, -1));
        assertEquals(new RGBColor(1.0, 1.0, 1.0), wrap.evaluate(1.0, -1));
        
        // Test large values
        assertEquals(new RGBColor(0.5, 0.5, 0.5), wrap.evaluate(100.5, 0));
        assertEquals(new RGBColor(-0.5, -0.5, -0.5), wrap.evaluate(-100.5, 0));
        
        // Test multiple wraps
        assertEquals(new RGBColor(0.3, 0.3, 0.3), wrap.evaluate(4.3, 0));  // 4.3 wraps to 0.3
        assertEquals(new RGBColor(-0.3, -0.3, -0.3), wrap.evaluate(5.7, 0)); // 5.7 wraps to -0.3
    }
    
    @Test
	public void wrapFunctionTests() {
		ExpressionTreeNode e = parser.makeExpression("wrap( x )");
		assertEquals(new Wrap(new X()), e);

		e = parser.makeExpression("wrap( x + y )");
		assertEquals(new Wrap(new Plus(new X(), new Y())), e);
	}
    
    @Test
	public void testTokenizeBasicFunctionExpression() {
		String expression = "wrap(x)";
		List<Token> tokens = tokenizer.parseTokens(expression);
		assertEquals(new WrapToken(), tokens.get(0));
		assertEquals(new LeftParenToken(), tokens.get(1));
		assertEquals(new IdentifierToken("x"), tokens.get(2));
		assertEquals(new RightParenToken(), tokens.get(3));
	}

}