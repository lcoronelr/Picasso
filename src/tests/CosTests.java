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
import picasso.parser.language.expressions.Cos;
import picasso.parser.language.expressions.Floor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.chars.LeftParenToken;
import picasso.parser.tokens.chars.RightParenToken;
import picasso.parser.tokens.functions.CosToken;

/**
 * Tests for the Cos function.
 * 
 * @author Therese Elvira Mombou Gatsing
 */
public class CosTests {

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
    public void testCosEvaluationBasic() {
        Cos myTree = new Cos(new X());

        
        RGBColor result0 = myTree.evaluate(0.0, 0.0);
        assertEquals(1.0, result0.getRed(), EPSILON);
        assertEquals(1.0, result0.getGreen(), EPSILON);
        assertEquals(1.0, result0.getBlue(), EPSILON);

        double[] tests = { -0.5, -0.25, 0.25, 0.5, 1.0 };

        for (double xVal : tests) {
            RGBColor result = myTree.evaluate(xVal, 0.0);
            double expected = Math.cos(xVal);

            assertEquals(expected, result.getRed(), EPSILON,
                    "Red channel should be cos(x)");
            assertEquals(expected, result.getGreen(), EPSILON,
                    "Green channel should be cos(x)");
            assertEquals(expected, result.getBlue(), EPSILON,
                    "Blue channel should be cos(x)");
        }
    }

    @Test
    public void testCosEvenSymmetry() {
        // cos(-x) = cos(x)
        Cos myTree = new Cos(new X());

        double xVal = 0.7;
        RGBColor positive = myTree.evaluate(xVal, 0.0);
        RGBColor negative = myTree.evaluate(-xVal, 0.0);

        assertEquals(positive.getRed(), negative.getRed(), EPSILON,
                "Red channel should satisfy cos(-x) = cos(x)");
        assertEquals(positive.getGreen(), negative.getGreen(), EPSILON,
                "Green channel should satisfy cos(-x) = cos(x)");
        assertEquals(positive.getBlue(), negative.getBlue(), EPSILON,
                "Blue channel should satisfy cos(-x) = cos(x)");
    }

    @Test
    public void cosFunctionTests() {
        ExpressionTreeNode e = parser.makeExpression("cos(x)");
        assertEquals(new Cos(new X()), e);
    }

    @Test
    public void testToString() {
        ExpressionTreeNode cos = new Cos(new Y());
        assertEquals("Cos(y)", cos.toString());
    }

    @Test
    public void testEquals() {
        ExpressionTreeNode cos = new Cos(new Y());
        ExpressionTreeNode cosSame = new Cos(new Y());
        ExpressionTreeNode cosDifferent = new Cos(new X());

        assertEquals(cos, cos);
        assertEquals(cosSame, cos);

        assertNotEquals(cos, cosDifferent);
        assertNotEquals(cosDifferent, cosSame);

        // another unary function should not equal Cos
        ExpressionTreeNode other = new Floor(new Y());
        assertNotEquals(cos, other);
    }

    @Test
    public void testTokenizeBasicFunctionExpression() {
        String expression = "cos(x)";
        List<Token> tokens = tokenizer.parseTokens(expression);
        assertEquals(new CosToken(), tokens.get(0));
        assertEquals(new LeftParenToken(), tokens.get(1));
        assertEquals(new IdentifierToken("x"), tokens.get(2));
        assertEquals(new RightParenToken(), tokens.get(3));
    }

    @Test
    public void testTokenizeMoreComplexExpression() {
        String expression = "x + cos(y)";
        List<Token> tokens = tokenizer.parseTokens(expression);

        // Should contain at least one CosToken
        boolean foundCos = tokens.stream().anyMatch(t -> t instanceof CosToken);
        assertTrue(foundCos, "Expression 'x + cos(y)' should contain a CosToken");
    }
}
