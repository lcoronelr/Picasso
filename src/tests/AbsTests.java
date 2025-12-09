package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import picasso.parser.ExpressionTreeGenerator;
import picasso.parser.Tokenizer;
import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.Abs;
import picasso.parser.language.expressions.Floor;
import picasso.parser.language.expressions.Plus;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.chars.LeftParenToken;
import picasso.parser.tokens.chars.RightParenToken;
import picasso.parser.tokens.functions.AbsToken;

/**
 * 
 * @author Therese Elvira Mombou Gatsing
 */

public class AbsTests {

    private static ExpressionTreeGenerator parser;
    private Tokenizer tokenizer;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        parser = new ExpressionTreeGenerator();
    }

    @BeforeEach
    public void setUp() throws Exception {
        tokenizer = new Tokenizer();
    }

    @Test
    public void testAbsEvaluation() {
        Abs myTree = new Abs(new X());

        assertEquals(new RGBColor(0.4, 0.4, 0.4), myTree.evaluate(0.4, -1));
        assertEquals(new RGBColor(0.999, 0.999, 0.999), myTree.evaluate(0.999, -1));
        assertEquals(new RGBColor(0.7, 0.7, 0.7), myTree.evaluate(-0.7, -1));

        
        for (int i = -1; i <= 1; i++) {
            double absVal = Math.abs(i);
            RGBColor expected = new RGBColor(absVal, absVal, absVal);

            assertEquals(expected, myTree.evaluate(i, -i));
            assertEquals(expected, myTree.evaluate(i, i));
        }

        
        double[] tests = { -0.7, -0.00001, 0.0, 0.000001, 0.5 };

        for (double testVal : tests) {
            double absOfTestVal = Math.abs(testVal);
            RGBColor expected = new RGBColor(absOfTestVal, absOfTestVal, absOfTestVal);

            assertEquals(expected, myTree.evaluate(testVal, -1));
            assertEquals(expected, myTree.evaluate(testVal, testVal));
        }
    }

    @Test
    public void absFunctionTests() {
        ExpressionTreeNode e = parser.makeExpression("abs( x )");
        assertEquals(new Abs(new X()), e);

        e = parser.makeExpression("abs( x + y )");
        assertEquals(new Abs(new Plus(new X(), new Y())), e);
    }

    @Test
    public void testToString() {
        ExpressionTreeNode abs = new Abs(new Y());
        assertEquals("Abs(y)", abs.toString());
    }

    @Test
    public void testEquals() {
        ExpressionTreeNode abs = new Abs(new Y());
        ExpressionTreeNode absSame = new Abs(new Y());
        ExpressionTreeNode absDifferent = new Abs(new X());

        assertEquals(abs, abs);
        assertEquals(absSame, abs);

        assertNotEquals(abs, absDifferent);
        assertNotEquals(absDifferent, absSame);

        ExpressionTreeNode other = new Floor(new Y());
        assertNotEquals(abs, other);
    }

    @Test
    public void testTokenizeBasicFunctionExpression() {
        String expression = "abs(x)";
        List<Token> tokens = tokenizer.parseTokens(expression);
        assertEquals(new AbsToken(), tokens.get(0));
        assertEquals(new LeftParenToken(), tokens.get(1));
        assertEquals(new IdentifierToken("x"), tokens.get(2));
        assertEquals(new RightParenToken(), tokens.get(3));
    }
}
