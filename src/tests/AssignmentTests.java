package tests;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Stack;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import picasso.parser.ExpressionTreeGenerator;
import picasso.parser.ParseException;
import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.*;
import picasso.parser.tokens.IdentifierToken;
import picasso.parser.tokens.Token;
import picasso.parser.tokens.operations.PlusToken;

/**
 * Tests for the assignment logic
 * 
 * 
 * 
 * @author Therese Elvira Mombou Gatsing
 */
public class AssignmentTests {
	
	private static ExpressionTreeGenerator parser;
	private static final double EPSILON = 1e-9;
	
	
	/**
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		parser = new ExpressionTreeGenerator();
	}

	@Test
	public void testAssignmentBehavesLikeRightHandSide() {
	  
	    ExpressionTreeNode assigned = parser.makeExpression("a = x + y");

	    
	    ExpressionTreeNode rhs = parser.makeExpression("x + y");

	        
	    ExpressionTreeNode lhs = parser.makeExpression("a");

	    double x = 0.3;
	    double y = -0.2;

	    RGBColor from_rhs      = rhs.evaluate(x, y);
	    RGBColor from_assigned = assigned.evaluate(x, y);
	    RGBColor from_lhs    = lhs.evaluate(x, y);

	    // "x + y" evaluates like "a = x + y" 
	    assertEquals(from_rhs.getRed(),   from_assigned.getRed(),   EPSILON);
	    assertEquals(from_rhs.getGreen(), from_assigned.getGreen(), EPSILON);
	    assertEquals(from_rhs.getBlue(),  from_assigned.getBlue(),  EPSILON);

	    // "a" evaluates like "x + y"
	    assertEquals(from_rhs.getRed(),   from_lhs.getRed(),   EPSILON);
	    assertEquals(from_rhs.getGreen(), from_lhs.getGreen(), EPSILON);
	    assertEquals(from_rhs.getBlue(),  from_lhs.getBlue(),  EPSILON);
	    }


	@Test
	public void testAssignedVariableInExpression() {

	    parser.makeExpression("sine = sin(x)");
	    ExpressionTreeNode direct = parser.makeExpression("sin(x) + x");
	    ExpressionTreeNode viaVar = parser.makeExpression("sine + x");

	    double x = 0.2;
	    double y = 0.0;

	    RGBColor expected = direct.evaluate(x, y);
	    RGBColor actual   = viaVar.evaluate(x, y);

	    assertEquals(expected.getRed(),   actual.getRed(),   EPSILON);
	    assertEquals(expected.getGreen(), actual.getGreen(), EPSILON);
	    assertEquals(expected.getBlue(),  actual.getBlue(),  EPSILON);
	}
	
	@Test
	public void testCannotAssignToX() {
	    assertThrows(ParseException.class, () -> {
	        parser.makeExpression("x = y + 1");
	    });
	}

	@Test
	public void testCannotAssignToY() {
	    assertThrows(ParseException.class, () -> {
	        parser.makeExpression("y = x");
	    });
	}

	@Test
	public void testLhsMustBeSingleIdentifier() {
	    assertThrows(ParseException.class, () -> {
	        parser.makeExpression("x + y = x");
	    });

	    assertThrows(ParseException.class, () -> {
	        parser.makeExpression("3a = x");
	    });
	}
	
	@Test
	public void testLhsMustNotBeEmpty() {
		 assertThrows(ParseException.class, () -> {
		        parser.makeExpression(" = x");
		  });
		
	}
	
	@Test
	public void testValidAssignmentNameAllowed() {
	    assertDoesNotThrow(() -> {
	        parser.makeExpression("foo1 = x + y");
	    });
	}
}
