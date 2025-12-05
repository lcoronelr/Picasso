package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import picasso.parser.ExpressionTreeGenerator;
import picasso.parser.Tokenizer;
import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.operations.MultiplyToken;

/**
 * Tests for the Multiply Operator .
 * 
 * @author Asya Yurkovskaya
 */

public class OrderOfOperationTests {
	
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
	public void testPrecedence1and2() {

        // x+y*x
        // (x+y)*x
        // x-y*x
        // (x-y)*x

        ExpressionTreeNode test1 = parser.makeExpression("x + y * x");
        ExpressionTreeNode test1Good = parser.makeExpression("x + (y * x)");
        ExpressionTreeNode test1Bad = parser.makeExpression("(x + y) * x");
        
        ExpressionTreeNode test2 = parser.makeExpression("x - y * x");
        ExpressionTreeNode test2Good = parser.makeExpression("x - (y * x)");
        ExpressionTreeNode test2Bad = parser.makeExpression("(x - y) * x");

        assertEquals(test1, test1Good);
        assertNotEquals(test1, test1Bad);

        assertEquals(test2, test2Good);
        assertNotEquals(test2, test2Bad);
    }
		
	@Test
	public void testPrecedence1and3() {

        // x+y^x
        // (x+y)^x
        // x-y^x
        // (x-y)^x

        ExpressionTreeNode test1 = parser.makeExpression("x + y ^ x");
        ExpressionTreeNode test1Good = parser.makeExpression("x + (y ^ x)");
        ExpressionTreeNode test1Bad = parser.makeExpression("(x + y) ^ x");
        
        ExpressionTreeNode test2 = parser.makeExpression("x - y ^ x");
        ExpressionTreeNode test2Good = parser.makeExpression("x - (y ^ x)");
        ExpressionTreeNode test2Bad = parser.makeExpression("(x - y) ^ x");

        assertEquals(test1, test1Good);
        assertNotEquals(test1, test1Bad);

        assertEquals(test2, test2Good);
        assertNotEquals(test2, test2Bad);

    }

    @Test
    public void testPrecedence2and3() {

        // x*y^x
        // x/y^x
        // (x*y)^x
        // (x/y)^x

        ExpressionTreeNode test1 = parser.makeExpression("x * y ^ x");
        ExpressionTreeNode test1Good = parser.makeExpression("x * (y ^ x)");
        ExpressionTreeNode test1Bad = parser.makeExpression("(x * y) ^ x");

        ExpressionTreeNode test2 = parser.makeExpression("x / y ^ x");
        ExpressionTreeNode test2Good = parser.makeExpression("x / (y ^ x)");
        ExpressionTreeNode test2Bad = parser.makeExpression("(x / y) ^ x");

        assertEquals(test1, test1Good);
        assertNotEquals(test1, test1Bad);

        assertEquals(test2, test2Good);
        assertNotEquals(test2, test2Bad);


    }

    @Test
    public void testPrecedence3and2() {

        // x^x*y
        // x^x/y
        // x^(x*y)
        // x^(x/y)

        ExpressionTreeNode test1 = parser.makeExpression("x ^ x * y");
        ExpressionTreeNode test1Good = parser.makeExpression("(x ^ x) * y");
        ExpressionTreeNode test1Bad = parser.makeExpression("x ^ (x * y)");
        
        ExpressionTreeNode test2 = parser.makeExpression("x ^ x / y");
        ExpressionTreeNode test2Good = parser.makeExpression("(x ^ x) / y");
        ExpressionTreeNode test2Bad = parser.makeExpression("x ^ (x / y)");

        assertEquals(test1, test1Good);
        assertNotEquals(test1, test1Bad);

        assertEquals(test2, test2Good);
        assertNotEquals(test2, test2Bad);

    }

    @Test
    public void testPrecedence3and3() {

        // x^x^y
        // x^(x^y)
        // (x^x)^y

        ExpressionTreeNode test1 = parser.makeExpression("x ^ x ^ y");
        ExpressionTreeNode test1Good = parser.makeExpression("x ^ (x ^ y)");
        ExpressionTreeNode test1Bad = parser.makeExpression("(x ^ x) ^ y");

        assertEquals(test1, test1Good);
        assertNotEquals(test1, test1Bad);

    }

    @Test
    public void testPrecedence2and2() {

        // x*x/y
        // (x*x)/y
        // x*(x/y)

        // x/x*y
        // (x/x)*y
        // x/(x*y)

        ExpressionTreeNode test1 = parser.makeExpression("x * x / y");
        ExpressionTreeNode test1Good = parser.makeExpression("(x * x) / y");
        ExpressionTreeNode test1Good2 = parser.makeExpression("x * (x / y)");

        ExpressionTreeNode test2 = parser.makeExpression("x / x * y");
        ExpressionTreeNode test2Good = parser.makeExpression("(x / x) * y");
        ExpressionTreeNode test2Bad = parser.makeExpression("x / (x * y)");

        assertEquals(test1, test1Good);
        assertEquals(test1, test1Good2);

        assertEquals(test2, test2Good);
        assertNotEquals(test2, test2Bad);
    }
}
