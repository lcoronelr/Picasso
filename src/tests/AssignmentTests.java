package tests;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import picasso.parser.ExpressionTreeGenerator;
import picasso.parser.ParseException;
import picasso.parser.SemanticAnalyzer;
import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.RGBColor;

/**
 * Tests for the assignment logic
 *
 * @author Therese Elvira Mombou Gatsing
 */
public class AssignmentTests {
	
	private static ExpressionTreeGenerator parser;
	private static final double EPSILON = 1e-9;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		parser = new ExpressionTreeGenerator();
	}

	
	@BeforeEach
	void resetVariables() {
		SemanticAnalyzer.getInstance().setVariables(new HashMap<>());
	}
	
	@Test
	public void testAssignmentBehavesLikeRightHandSide() {

	    
	    parser.makeExpression("a = x");
	    ExpressionTreeNode rhs = parser.makeExpression("x");

	    ExpressionTreeNode stored = SemanticAnalyzer.getInstance().getVariable("a");

	    assertNotNull(stored, "Variable 'a' should be stored in the SemanticAnalyzer");
	    assertEquals(rhs, stored, "Assignment 'a = x' should store the RHS expression for 'a'");
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
	public void testReassignmentUsesLatestDefinition() {
	    parser.makeExpression("a = x");
	    ExpressionTreeNode firstUse = parser.makeExpression("a");

	    parser.makeExpression("a = y");
	    ExpressionTreeNode secondUse = parser.makeExpression("a");

	    double x = 0.4;
	    double y = -0.1;

	    RGBColor first  = firstUse.evaluate(x, y);   // should behave like x
	    RGBColor second = secondUse.evaluate(x, y);  // should behave like y

	    assertEquals(x, first.getRed(),   EPSILON);
	    assertEquals(x, first.getGreen(), EPSILON);
	    assertEquals(x, first.getBlue(),  EPSILON);

	    assertEquals(y, second.getRed(),   EPSILON);
	    assertEquals(y, second.getGreen(), EPSILON);
	    assertEquals(y, second.getBlue(),  EPSILON);
	}

	
	
	@Test
	public void testVariableUsageMatchesSubstitution() {

	    
	    parser.makeExpression("b = x");

	    
	    ExpressionTreeNode substituted = parser.makeExpression("x * 0.5");

	    
	    ExpressionTreeNode viaVar      = parser.makeExpression("b * 0.5");

	    double x = 0.6;
	    double y = 0.1;  

	    RGBColor expected = substituted.evaluate(x, y);
	    RGBColor actual   = viaVar.evaluate(x, y);

	    assertEquals(expected.getRed(),   actual.getRed(),   EPSILON);
	    assertEquals(expected.getGreen(), actual.getGreen(), EPSILON);
	    assertEquals(expected.getBlue(),  actual.getBlue(),  EPSILON);
	}

	
	
	@Test
	public void testLhsMustBeSingleIdentifier() {
		assertThrows(ParseException.class, () -> {
	        parser.makeExpression("3 = x");
	    });
	}
	
	
	@Test
	public void testLhsMustNotBeEmpty() {
	    assertThrows(RuntimeException.class, () -> {
	        parser.makeExpression(" = x");
	    });
	}
	
	
	
	@Test
	public void testValidAssignmentNameAllowed() {
	    assertDoesNotThrow(() -> {
	        parser.makeExpression("foo_1 = x + y");
	    });
	}
	
	@Test
	public void testMultipleVariablesStored() {
		SemanticAnalyzer analyzer = SemanticAnalyzer.getInstance();

		parser.makeExpression("a = x");
		parser.makeExpression("b = y");
		parser.makeExpression("c = x + y");

		Map<String, ExpressionTreeNode> vars = analyzer.getVariables();
		assertEquals(3, vars.size(), "There should be three variables stored (a, b, c).");
		assertTrue(vars.containsKey("a"));
		assertTrue(vars.containsKey("b"));
		assertTrue(vars.containsKey("c"));
	}
	
	
	@Test
	public void testAssignmentUsingPreviouslyDefinedVariable() {

	    parser.makeExpression("base = x + y");
	    parser.makeExpression("half = base * 0.5");

	    ExpressionTreeNode viaVar   = parser.makeExpression("half");
	    ExpressionTreeNode direct   = parser.makeExpression("(x + y) * 0.5");

	    double x = -0.3;
	    double y = 0.8;

	    RGBColor expected = direct.evaluate(x, y);
	    RGBColor actual   = viaVar.evaluate(x, y);

	    assertEquals(expected.getRed(),   actual.getRed(),   EPSILON);
	    assertEquals(expected.getGreen(), actual.getGreen(), EPSILON);
	    assertEquals(expected.getBlue(),  actual.getBlue(),  EPSILON);
	}
	
	@Test
	public void testAssignmentWithWhitespace() {

	    parser.makeExpression("   foo   =    x + 1   ");
	    ExpressionTreeNode viaVar = parser.makeExpression("foo");
	    ExpressionTreeNode direct = parser.makeExpression("x + 1");

	    double x = 0.25;
	    double y = 0.0;

	    RGBColor expected = direct.evaluate(x, y);
	    RGBColor actual   = viaVar.evaluate(x, y);

	    assertEquals(expected.getRed(),   actual.getRed(),   EPSILON);
	    assertEquals(expected.getGreen(), actual.getGreen(), EPSILON);
	    assertEquals(expected.getBlue(),  actual.getBlue(),  EPSILON);
	}
	
	@Test
	public void testVariablesClearedBetweenUses() {

		parser.makeExpression("a = x");
		assertNotNull(SemanticAnalyzer.getInstance().getVariable("a"));

		//clears
		SemanticAnalyzer.getInstance().setVariables(new HashMap<>());

		assertNull(SemanticAnalyzer.getInstance().getVariable("a"),
				"Variable 'a' should not exist after resetting variables.");
	}
	
	
}
