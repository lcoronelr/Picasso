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
 * @author Therese Elvira Mombou Gatsing
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
	public void testPrecedenceAddAndMulti() {

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
	public void testPrecedenceAddAndExp() {

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
    public void testPrecedenceMultiAndExp() {

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
    public void testPrecedenceExpAndMulti() {

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
    public void testPrecedenceExpAndExp() {

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
    
    @Test
    public void testNegatePrecedenceWithAddition() {
        ExpressionTreeNode parsed    = parser.makeExpression("!x + y");
        ExpressionTreeNode expected  = parser.makeExpression("(!x) + y");
        ExpressionTreeNode notWanted = parser.makeExpression("!(x + y)");

        assertEquals(expected, parsed);
        assertNotEquals(notWanted, parsed);
    }
    
    
    @Test
    public void testNegatePrecedenceWithMultiplication() {
        ExpressionTreeNode parsed    = parser.makeExpression("!x * y");
        ExpressionTreeNode expected  = parser.makeExpression("(!x) * y");
        ExpressionTreeNode notWanted = parser.makeExpression("!(x * y)");

        assertEquals(expected, parsed);
        assertNotEquals(notWanted, parsed);
    }
    
    @Test
    public void testNegatePrecedenceWithModulo() {
        ExpressionTreeNode parsed    = parser.makeExpression("!x % y");
        ExpressionTreeNode expected  = parser.makeExpression("(!x) % y");
        ExpressionTreeNode notWanted = parser.makeExpression("!(x % y)");

        assertEquals(expected, parsed);
        assertNotEquals(notWanted, parsed);
    }
    
    
    @Test
    public void testNegatePrecedenceWithExponent() {
        ExpressionTreeNode parsed    = parser.makeExpression("!x ^ y");
        ExpressionTreeNode expected  = parser.makeExpression("(!x) ^ y");
        ExpressionTreeNode notWanted = parser.makeExpression("!(x ^ y)");

        assertEquals(expected, parsed);
        assertNotEquals(notWanted, parsed);
    }
    
    @Test
    public void testNegateWithAllPrecedenceLevelsEvaluation() {
        ExpressionTreeNode parsed   = parser.makeExpression("!x + y * x ^ y");
        ExpressionTreeNode expected = parser.makeExpression("(!x) + (y * (x ^ y))");

        double x = 0.3;
        double y = -0.6;

        RGBColor parsedVal   = parsed.evaluate(x, y);
        RGBColor expectedVal = expected.evaluate(x, y);

        assertEquals(expectedVal.getRed(),   parsedVal.getRed(),   EPSILON);
        assertEquals(expectedVal.getGreen(), parsedVal.getGreen(), EPSILON);
        assertEquals(expectedVal.getBlue(),  parsedVal.getBlue(),  EPSILON);
    }
    
    @Test
    public void testPrecedenceAddAndModulo() {
        ExpressionTreeNode parsed    = parser.makeExpression("x + y % x");
        ExpressionTreeNode expected  = parser.makeExpression("x + (y % x)");
        ExpressionTreeNode notWanted = parser.makeExpression("(x + y) % x");

        assertEquals(expected, parsed);
        assertNotEquals(notWanted, parsed);
    }
    
    @Test
    public void testPrecedenceMultiplyAndModulo() {
        ExpressionTreeNode parsed    = parser.makeExpression("x * y % x");
        ExpressionTreeNode leftAssoc = parser.makeExpression("(x * y) % x");
        ExpressionTreeNode rightAssoc = parser.makeExpression("x * (y % x)");

        assertEquals(leftAssoc, parsed);
        assertNotEquals(rightAssoc, parsed);
    }
    
    @Test
    public void testAllThreePrecedenceLevels() {
        ExpressionTreeNode parsed   = parser.makeExpression("x + y * x ^ y");
        ExpressionTreeNode expected = parser.makeExpression("x + (y * (x ^ y))");
        ExpressionTreeNode wrong1   = parser.makeExpression("(x + y) * (x ^ y)");
        ExpressionTreeNode wrong2   = parser.makeExpression("x + ((y * x) ^ y)");

        assertEquals(expected, parsed);
        assertNotEquals(wrong1, parsed);
        assertNotEquals(wrong2, parsed);
    }
}
