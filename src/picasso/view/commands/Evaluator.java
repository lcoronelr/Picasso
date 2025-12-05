package picasso.view.commands;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JTextField;

import picasso.model.Pixmap;
import picasso.parser.ExpressionTreeGenerator;
import picasso.parser.ParseException;
import picasso.parser.language.ExpressionTreeNode;
import picasso.util.Command;
import picasso.util.ErrorReporter;

/**
 * Evaluate an expression for each pixel in a image.
 * 
 * @author Robert C Duvall
 * @author Sara Sprenkle
 * @author Luis Coronel
 * @author Therese Elvira Mombou Gatsing
 * @author Menilik Deneke
 */
public class Evaluator implements Command<Pixmap> {
	
	private final ExpressionTreeGenerator expTreeGen = new ExpressionTreeGenerator();
	
	public static final double DOMAIN_MIN = -1;
	public static final double DOMAIN_MAX = 1;
	
	private JTextField expressionField;
	private ErrorReporter errorReporter;
	
	/** 
	 * Constructor for the expression
	 */	
	public Evaluator(JTextField expressionField) {
		this.expressionField = expressionField;
		this.errorReporter = null;
	}
	
	/** 
	 * Constructor for the expression with error reporting
	 */	
	public Evaluator(JTextField expressionField, ErrorReporter errorReporter) {
		this.expressionField = expressionField;
		this.errorReporter = errorReporter;
	}
	
	/**
	 * Evaluate an expression for each point in the image.
	 */
	public void execute(Pixmap target) {
		try {
			if (errorReporter != null) {
				errorReporter.clearError();
			}
			
			ExpressionTreeNode expr = createExpression();
			
			Dimension size = target.getSize();
			for (int imageY = 0; imageY < size.height; imageY++) {
				double evalY = imageToDomainScale(imageY, size.height);
				for (int imageX = 0; imageX < size.width; imageX++) {
					double evalX = imageToDomainScale(imageX, size.width);
					Color pixelColor = expr.evaluate(evalX, evalY).toJavaColor();
					target.setColor(imageX, imageY, pixelColor);
				}
			}
		} catch (ParseException e) {
			String msg = e.getMessage();
			if (msg != null) {
				msg = msg.toLowerCase();
				if (msg.contains("unrecognized character") || msg.contains("no match")) {
					reportError("Invalid character in expression. Only use letters, numbers, and valid operators (+, -, *, /).");
				} else if (msg.contains("mismatched parenthes") || msg.contains("missing (")) {
					reportError("Mismatched parentheses. Make sure every '(' has a matching ')'.");
				} else if (msg.contains("extra operands")) {
					reportError("Too many values without operators. Check your expression structure.");
				} else if (msg.contains("not enough") || msg.contains("expected")) {
					reportError("Missing operand or argument. Check that functions have the right number of inputs.");
				} else {
					reportError("Cannot understand expression. Please check your syntax.");
				}
			} else {
				reportError("Cannot understand expression. Please check your syntax.");
			}
		} catch (ArithmeticException e) {
			reportError("Math error: division by zero or invalid calculation.");
		} catch (NullPointerException e) {
			reportError("Please enter an expression.");
		} catch (Exception e) {
			System.err.println("Unexpected error during evaluation: " + e.getMessage());
			e.printStackTrace();
			reportError("Unable to evaluate expression. Please try a different one.");
		}
	}
	
	/**
	 * Helper method to report errors (handles null errorReporter gracefully)
	 */
	private void reportError(String message) {
		if (errorReporter != null) {
			errorReporter.reportError(message);
		} else {
			System.err.println("Error: " + message);
		}
	}
	
	/**
	 * Convert from image space to domain space.
	 */
	protected double imageToDomainScale(int value, int bounds) {
		double range = DOMAIN_MAX - DOMAIN_MIN;
		return ((double) value / bounds) * range + DOMAIN_MIN;
	}
	
	/**
	 * Create expression tree from text field.
	 */
	private ExpressionTreeNode createExpression() {
		String expressionText = expressionField.getText();
		
		if (expressionText == null || expressionText.trim().isEmpty()) {
			throw new NullPointerException("Empty expression");
		}
		
		return expTreeGen.makeExpression(expressionText);
	}
}