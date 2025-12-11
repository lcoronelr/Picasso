/**
 * 
 */
package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.*;

/**
 * Tests of the evaluation of expression trees
 * 
 * @author Sara Sprenkle
 * @author Therese Elvira Mombou Gatsing
 */
public class EvaluatorTests {

	private static final double EPSILON = 1e-9;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
	}

	@Test
	public void testConstantEvaluation() {
		ExpressionTreeNode e = new RGBColor(1, -1, 1);
		for (int i = -1; i <= 1; i++) {
			assertEquals(new RGBColor(1, -1, 1), e.evaluate(i, i));
		}
	}

	@Test
	public void testXEvaluation() {
		X x = new X();
		for (int i = -1; i <= 1; i++) {
			assertEquals(new RGBColor(i, i, i), x.evaluate(i, i));
		}
	}

	@Test
	public void testFloorEvaluation() {
		Floor myTree = new Floor(new X());

		// some straightforward tests
		assertEquals(new RGBColor(0, 0, 0), myTree.evaluate(.4, -1));
		assertEquals(new RGBColor(0, 0, 0), myTree.evaluate(.999, -1));
		assertEquals(new RGBColor(-1, -1, -1), myTree.evaluate(-.7, -1));

		// test the ints; remember that y's value doesn't matter
		for (int i = -1; i <= 1; i++) {
			assertEquals(new RGBColor(i, i, i), myTree.evaluate(i, -i));
			assertEquals(new RGBColor(i, i, i), myTree.evaluate(i, i));
		}

		double[] tests = { -.7, -.00001, .000001, .5 };

		for (double testVal : tests) {
			double floorOfTestVal = Math.floor(testVal);
			assertEquals(new RGBColor(floorOfTestVal, floorOfTestVal, floorOfTestVal), myTree.evaluate(testVal, -1));
			assertEquals(new RGBColor(floorOfTestVal, floorOfTestVal, floorOfTestVal),
					myTree.evaluate(testVal, testVal));
		}
	}

	
	@Test
	public void testYEvaluation() {
		Y yExpr = new Y();
		for (int i = -1; i <= 1; i++) {
			assertEquals(new RGBColor(i, i, i), yExpr.evaluate(0.5, i),
					"Y expression should use the y-coordinate only");
		}
	}

	@Test
	public void testPlusEvaluation() {
		ExpressionTreeNode expr = new Plus(new X(), new Y());

		double x = 0.3;
		double y = -0.4;
		RGBColor result = expr.evaluate(x, y);

		double expected = x + y;
		assertEquals(expected, result.getRed(),   EPSILON);
		assertEquals(expected, result.getGreen(), EPSILON);
		assertEquals(expected, result.getBlue(),  EPSILON);
	}

	@Test
	public void testMinusEvaluation() {
		ExpressionTreeNode expr = new Minus(new X(), new Y());

		double x = 0.7;
		double y = -0.2;
		RGBColor result = expr.evaluate(x, y);

		double expected = x - y;
		assertEquals(expected, result.getRed(),   EPSILON);
		assertEquals(expected, result.getGreen(), EPSILON);
		assertEquals(expected, result.getBlue(),  EPSILON);
	}

	@Test
	public void testMultiplyEvaluation() {
		ExpressionTreeNode expr = new Multiply(new X(), new Y());

		double x = 0.5;
		double y = 0.4;
		RGBColor result = expr.evaluate(x, y);

		double expected = x * y;
		assertEquals(expected, result.getRed(),   EPSILON);
		assertEquals(expected, result.getGreen(), EPSILON);
		assertEquals(expected, result.getBlue(),  EPSILON);
	}

	@Test
	public void testDivideEvaluation() {
		ExpressionTreeNode expr = new Divide(new X(), new Y());

		double x = 0.6;
		double y = 0.2;
		RGBColor result = expr.evaluate(x, y);

		double expected = x / y;
		assertEquals(expected, result.getRed(),   EPSILON);
		assertEquals(expected, result.getGreen(), EPSILON);
		assertEquals(expected, result.getBlue(),  EPSILON);
	}

	@Test
	public void testModEvaluation() {
		ExpressionTreeNode expr = new Modulo(new X(), new Y());

		double x = 0.7;
		double y = 0.3;
		RGBColor result = expr.evaluate(x, y);

		double expected = x % y;
		assertEquals(expected, result.getRed(),   EPSILON);
		assertEquals(expected, result.getGreen(), EPSILON);
		assertEquals(expected, result.getBlue(),  EPSILON);
	}

	@Test
	public void testExpEvaluation() {
	    ExpressionTreeNode expr = new Exp(new X());

	    double x = 0.5;
	    double y = 0.0; 
	    RGBColor result = expr.evaluate(x, y);

	    double expected = Math.exp(x);
	    assertEquals(expected, result.getRed(),   EPSILON);
	    assertEquals(expected, result.getGreen(), EPSILON);
	    assertEquals(expected, result.getBlue(),  EPSILON);
	}
	

	@Test
	public void testAbsEvaluation() {
		ExpressionTreeNode expr = new Abs(new X());

		double x = -0.8;
		RGBColor result = expr.evaluate(x, 0.0);

		double expected = Math.abs(x);
		assertEquals(expected, result.getRed(),   EPSILON);
		assertEquals(expected, result.getGreen(), EPSILON);
		assertEquals(expected, result.getBlue(),  EPSILON);
	}

	@Test
	public void testCeilEvaluation() {
		ExpressionTreeNode expr = new Ceil(new X());

		double x = 0.2;
		RGBColor result = expr.evaluate(x, 0.0);

		double expected = Math.ceil(x);
		assertEquals(expected, result.getRed(),   EPSILON);
		assertEquals(expected, result.getGreen(), EPSILON);
		assertEquals(expected, result.getBlue(),  EPSILON);
	}

	@Test
	public void testSinEvaluation() {
		ExpressionTreeNode expr = new Sin(new X());

		double x = 0.5;
		RGBColor result = expr.evaluate(x, 0.0);

		double expected = Math.sin(x);
		assertEquals(expected, result.getRed(),   EPSILON);
		assertEquals(expected, result.getGreen(), EPSILON);
		assertEquals(expected, result.getBlue(),  EPSILON);
	}

	@Test
	public void testCosEvaluation() {
		ExpressionTreeNode expr = new Cos(new X());

		double x = 0.5;
		RGBColor result = expr.evaluate(x, 0.0);

		double expected = Math.cos(x);
		assertEquals(expected, result.getRed(),   EPSILON);
		assertEquals(expected, result.getGreen(), EPSILON);
		assertEquals(expected, result.getBlue(),  EPSILON);
	}

	@Test
	public void testClampEvaluation() {
		ExpressionTreeNode expr = new Clamp(new X());

		double[] tests = { -2.0, -1.0, -0.5, 0.0, 0.5, 1.0, 2.0 };

		for (double x : tests) {
			RGBColor result = expr.evaluate(x, 0.0);
			assertTrue(result.getRed()   >= -1.0 && result.getRed()   <= 1.0);
			assertTrue(result.getGreen() >= -1.0 && result.getGreen() <= 1.0);
			assertTrue(result.getBlue()  >= -1.0 && result.getBlue()  <= 1.0);
		}
	}

	

	@Test
	public void testNegateEvaluation() {
		ExpressionTreeNode expr = new Negate(new X());

		double x = 0.4;
		RGBColor result = expr.evaluate(x, 0.0);

		double expected = -x; 
		assertEquals(expected, result.getRed(),   EPSILON);
		assertEquals(expected, result.getGreen(), EPSILON);
		assertEquals(expected, result.getBlue(),  EPSILON);
	}

	@Test
	public void testRgbToYCrCbOutputRangeForRandomColor() {
	    RGBColor original = new RGBColor(0.2, -0.3, 0.4);
	    ExpressionTreeNode color = original;
	    RgbToYCrCb converter = new RgbToYCrCb(color);

	    RGBColor result = converter.evaluate(0.0, 0.0);

	    assertTrue(result.getRed()   >= -1.0 && result.getRed()   <= 1.0);
	    assertTrue(result.getGreen() >= -1.0 && result.getGreen() <= 1.0);
	    assertTrue(result.getBlue()  >= -1.0 && result.getBlue()  <= 1.0);
	}

}
