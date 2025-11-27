package tests;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Stack;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import picasso.parser.ExpressionTreeGenerator;
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


}
