package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import picasso.parser.ParseException;
import picasso.parser.Tokenizer;
import picasso.parser.tokens.*;
import picasso.parser.tokens.chars.*;
import picasso.parser.tokens.functions.*;
import picasso.parser.tokens.operations.*;

/**
 * Tests that the tokenizer tokens as expected.
 * 
 * @author Sara Sprenkle
 * @author Therese Elvira Mombou Gatsing
 */
public class TokenizerTest {

	private Tokenizer tokenizer;
	private List<Token> tokens;

	@BeforeEach
	public void setUp() throws Exception {
		// create a new tokenizer so you don't have any old stuff in the Tokenizer
		tokenizer = new Tokenizer();
	}

	/**
	 * Test that parsing an expression with a comment works
	 */
	@Test
	public void testTokenizeComment() {
		String expression = "x // this is a comment";
		List<Token> tokens = tokenizer.parseTokens(expression);
		assertEquals(new IdentifierToken("x"), tokens.get(0));
		assertEquals(1, tokens.size());

		expression = "// everything is a comment";
		tokens = tokenizer.parseTokens(expression);
		assertEquals(0, tokens.size());
	}

	/**
	 * Test that parsing a constant works
	 */
	@Test
	public void testTokenizeConstant() {
		String expression = ".324";
		tokens = tokenizer.parseTokens(expression);
		assertEquals(new NumberToken(.324), tokens.get(0));

		expression = "-1";
		tokens = tokenizer.parseTokens(expression);
		assertEquals(new NumberToken(-1), tokens.get(0));

		// No problems here; problem will be in next step (Semantic Analysis)
		expression = "-1.2";
		tokens = tokenizer.parseTokens(expression);
		assertEquals(new NumberToken(-1.2), tokens.get(0));
	}

	@Test
	public void testTokenizeColor() {
		String expression = "[1, 1, 1]";
		tokens = tokenizer.parseTokens(expression);
		assertEquals(new ColorToken(1, 1, 1), tokens.get(0));

		expression = "[-1, 0, .5]";
		tokens = tokenizer.parseTokens(expression);
		assertEquals(new ColorToken(-1, 0, .5), tokens.get(0));
	}

	@Test
	public void testTokenizeInvalidColor() {
		String expression = "[1, 1.0001, 1]";

		assertThrows(ParseException.class, () -> {
			tokens = tokenizer.parseTokens(expression);
		});
	}

	@Test
	public void testTokenizeBasicFunctionExpression() {
		String expression = "floor(x)";
		tokens = tokenizer.parseTokens(expression);
		assertEquals(new FloorToken(), tokens.get(0));
		assertEquals(new LeftParenToken(), tokens.get(1));
		assertEquals(new IdentifierToken("x"), tokens.get(2));
		assertEquals(new RightParenToken(), tokens.get(3));
	}

	@Test
	public void testTokenizeCombinedFunctionExpression() {
		String expression = "perlinColor(floor(x), y)";
		tokens = tokenizer.parseTokens(expression);
		
		assertEquals(new PerlinColorToken(), tokens.get(0));
		assertEquals(new LeftParenToken(),    tokens.get(1));
		assertEquals(new FloorToken(),        tokens.get(2));
		assertEquals(new LeftParenToken(),    tokens.get(3));
		assertEquals(new IdentifierToken("x"),tokens.get(4));
		assertEquals(new RightParenToken(),   tokens.get(5));
		assertEquals(new CommaToken(),        tokens.get(6));
		assertEquals(new IdentifierToken("y"),tokens.get(7));
		assertEquals(new RightParenToken(),   tokens.get(8));
		assertEquals(9, tokens.size());
		
		expression = "sin(perlinColor(x, y))";
		tokens = tokenizer.parseTokens(expression);
		
		assertEquals(new SinToken(),          tokens.get(0));
		assertEquals(new LeftParenToken(),    tokens.get(1));
		assertEquals(new PerlinColorToken(),  tokens.get(2));
		assertEquals(new LeftParenToken(),    tokens.get(3));
		assertEquals(new IdentifierToken("x"),tokens.get(4));
		assertEquals(new CommaToken(),        tokens.get(5));
		assertEquals(new IdentifierToken("y"),tokens.get(6));
		assertEquals(new RightParenToken(),   tokens.get(7));
		assertEquals(new RightParenToken(),   tokens.get(8));
		assertEquals(9, tokens.size());
		
	}

	
	
	@Test
	public void testTokenizeSimpleArithmeticExpression() {
		String expression = "x + y";
		tokens = tokenizer.parseTokens(expression);

		assertEquals(new IdentifierToken("x"), tokens.get(0));
		assertEquals(new PlusToken(),          tokens.get(1));
		assertEquals(new IdentifierToken("y"), tokens.get(2));
		assertEquals(3, tokens.size());
	}

	
	@Test
	public void testTokenizeMixedArithmeticExpression() {
		String expression = "x + y * 3";
		tokens = tokenizer.parseTokens(expression);

		assertEquals(new IdentifierToken("x"), tokens.get(0));
		assertEquals(new PlusToken(),          tokens.get(1));
		assertEquals(new IdentifierToken("y"), tokens.get(2));
		assertEquals(new MultiplyToken(),      tokens.get(3));
		assertEquals(new NumberToken(3),       tokens.get(4));
		assertEquals(5, tokens.size());
	}

	
	@Test
	public void testTokenizeParenthesizedArithmeticExpression() {
		String expression = "(x - 1) / y";
		tokens = tokenizer.parseTokens(expression);

		assertEquals(new LeftParenToken(),     tokens.get(0));
		assertEquals(new IdentifierToken("x"), tokens.get(1));
		assertEquals(new MinusToken(),         tokens.get(2));
		assertEquals(new NumberToken(1),       tokens.get(3));
		assertEquals(new RightParenToken(),    tokens.get(4));
		assertEquals(new DivideToken(),        tokens.get(5));
		assertEquals(new IdentifierToken("y"), tokens.get(6));
		assertEquals(7, tokens.size());
	}

}
