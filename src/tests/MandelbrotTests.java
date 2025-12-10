package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import picasso.parser.ExpressionTreeGenerator;
import picasso.parser.Tokenizer;
import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.Constant;
import picasso.parser.language.expressions.ImageClip;
import picasso.parser.language.expressions.Mandelbrot;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.Y;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.StringToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.chars.CommaToken;
import picasso.parser.tokens.chars.LeftParenToken;
import picasso.parser.tokens.chars.RightParenToken;
import picasso.parser.tokens.functions.MandelbrotToken;

public class MandelbrotTests {
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
	public void testMandelbrotParseException() {
		// Test that parser creates correct Mandelbrot expression tree
		ExpressionTreeNode e = parser.makeExpression("mandelbrot(x, y)");
		assertEquals(new Mandelbrot(new X(), new Y()), e,
				"The Parser should be able to create ImageClip expression tree");
	}
	
	@Test
	public void testMandelbrotCreation() {
		Mandelbrot mandelbrot = new Mandelbrot(new X(), new Y());
		assertNotNull(mandelbrot);
	}
	
	@Test
	public void testTokenizeMandelbrotExpression() {
		String expression = "mandelbrot(x, y)";
		List<Token> tokens = tokenizer.parseTokens(expression);
		
		assertEquals(new MandelbrotToken(), tokens.get(0), "First token is MandelbrotToken");
		assertEquals(new LeftParenToken(), tokens.get(1), "Second token is (");
		assertEquals(new IdentifierToken("x"), tokens.get(2), "Third token is x");
		assertEquals(new CommaToken(), tokens.get(3), "Fourth token is comma");
		assertEquals(new IdentifierToken("y"), tokens.get(4), "Fifth token is y");
		assertEquals(new RightParenToken(), tokens.get(5), "Last token should be )");
	}
	
	@Test
	public void testParseMandelbrotExpression() {
		// Test that parser creates correct Mandelbrot expression tree
		ExpressionTreeNode e = parser.makeExpression("mandelbrot(x, y)");
		assertEquals(new Mandelbrot(new X(), new Y()), e,
				"The Parser should be able to create Mandelbrot expression tree");
	}
	
	@Test
	public void testMandelbrotEquals() {
		Mandelbrot mandelbrot1 = new Mandelbrot(new X(), new Y());
		Mandelbrot mandelbrot2 = new Mandelbrot(new X(), new Y());
		assertEquals(mandelbrot1, mandelbrot2, "Same parameters should be equal");
	}
	
	@Test
	public void testInteriorPointsAreBlack() {
		Mandelbrot mandelbrot = new Mandelbrot(new Constant(0), new Constant(0));
	    
	    double[][] interiorPoints = {
	        {0, 0}, 
	        {-1, 0},       
	        {-0.75, 0},
	        {0.25, 0}    
	    };
	    
	    for (double[] point : interiorPoints) {
	        double x = point[0];
	        double y = point[1];
	        RGBColor color = mandelbrot.evaluate(x, y);
	        
	        assertEquals(-1.0, color.getRed(), EPSILON,
	            "Point (" + x + "," + y + ") should be inside (black)");
	        assertEquals(color.getRed(), color.getGreen(), EPSILON);
	        assertEquals(color.getRed(), color.getBlue(), EPSILON);
	    }
	}

	@Test
	public void testMandelbrotGradient() {
	    Mandelbrot mandelbrot = new Mandelbrot(new X(), new Y());
	    
	    double[][] testPairs = {
	        //
	        {0.25, 0.26},    
	        {0.25, 0.5},     
	        {0.25, 1.0},    
	    };
	    
	    for (double[] pair : testPairs) {
	        double closer = pair[0];
	        double farther = pair[1];
	        
	        RGBColor colorCloser = mandelbrot.evaluate(closer, 0.0);
	        RGBColor colorFarther = mandelbrot.evaluate(farther, 0.0);
	        
	        assertTrue(colorFarther.getRed() > colorCloser.getRed(),
	            "Point (" + farther + ", 0) should be brighter than (" + closer + ", 0). " +
	            "Got: " + colorFarther.getRed() + " vs " + colorCloser.getRed());
	    }
	}
	

	@Test
	public void testEscapeDetection() {    
		Mandelbrot mandelbrot = new Mandelbrot(new X(), new Y());

	    RGBColor faster = mandelbrot.evaluate(2.0, 2.0);    
	    RGBColor slower = mandelbrot.evaluate(0.1, 0.1);
	    
	    assertTrue(faster.getRed() > slower.getRed(),
	    	    "(2.0, 2.0) should be brighter than (0.1, 0.1)");
	}
	
	@Test
	public void testMandelbrotColorRange() {
		Mandelbrot mandelbrot = new Mandelbrot(new X(), new Y());
	    
	    for (double x = -1; x <= 1; x += 0.25) {
	        for (double y = -1; y <= 1; y += 0.25) {
	            RGBColor color = mandelbrot.evaluate(x, y);
	            assertTrue(color.getRed() >= -1 && color.getRed() <= 1);
	            assertTrue(color.getGreen() >= -1 && color.getGreen() <= 1);
	            assertTrue(color.getBlue() >= -1 && color.getBlue() <= 1);
	        }
	    }
	}
	
	/**
	 * 
	 */
	
	@Test
	public void testParseMandelbrotErrorHandling() {
	    // Test that invalid syntax throws appropriate exceptions
	    assertThrows(Exception.class, () -> {
	        parser.makeExpression("mandelbrot(x)"); // Missing parameter
	    });
	    
	    assertThrows(Exception.class, () -> {
	        parser.makeExpression("mandelbrot(x, y, z)"); // Extra parameter
	    });
	}
	
	@Test
	public void testMandelbrotToString() {
	    Mandelbrot m = new Mandelbrot(new X(), new Y());
	    String str = m.toString();
	    
	    assertTrue(str.contains("Mandelbrot"), "Should contain 'Mandelbrot'");
	    assertTrue(str.contains("x"), "Should contain x parameter");
	    assertTrue(str.contains("y"), "Should contain y parameter");
	}

}