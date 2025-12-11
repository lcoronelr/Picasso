package tests;

import java.util.List;

// JUnit 5 imports
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import picasso.parser.ExpressionTreeGenerator;
import picasso.parser.Tokenizer;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.Random;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.chars.LeftParenToken;
import picasso.parser.tokens.chars.RightParenToken;
import picasso.parser.tokens.functions.RandomToken;

public class RandomTests {
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
	public void testTokenizeBasicRandomExpression() {
		String expression = "random()";
		List<Token> tokens = tokenizer.parseTokens(expression);
		
		assertEquals(3, tokens.size(), "Should have 3 tokens: random ()");
		assertEquals(new RandomToken(), tokens.get(0));
		assertEquals(new LeftParenToken(), tokens.get(1));
		assertEquals(new RightParenToken(), tokens.get(2));
	}
	
	@Test
	public void testEvaluateReturnsValidColor() {
		Random func = new Random();
		RGBColor color = func.evaluate(0.5, 0.5);
		
		assertNotNull(color, "Evaluate should return an RGBColor");
		
		assertTrue(color.getRed() >= -1 && color.getRed() <= 1, 
			"Red component should be in range [-1, 1] " + color.getRed());
		assertTrue(color.getGreen() >= -1 && color.getGreen() <= 1, 
			"Green component should be in range [-1, 1], got: " + color.getGreen());
		assertTrue(color.getBlue() >= -1 && color.getBlue() <= 1, 
			"Blue component should be in range [-1, 1], got: " + color.getBlue());
	}

}
