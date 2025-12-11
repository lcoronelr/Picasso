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
 * @author Asya Yurkovskaya
 */
public class Evaluator implements Command<Pixmap> {
	
	private final ExpressionTreeGenerator expTreeGen = new ExpressionTreeGenerator();
	
	public static final double DOMAIN_MIN = -1;
	public static final double DOMAIN_MAX = 1;
	
	private JTextField expressionField;
	private ErrorReporter errorReporter;
	
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
		ExpressionTreeNode expr = null;
		
		try {
			if (errorReporter != null) {
				errorReporter.clearError();
			}

			expr = createExpression();

		} catch (ParseException e) {
			String msg = e.getMessage();
			if (msg != null && !msg.trim().isEmpty()) {
				msg = cleanErrorMessage(msg);
				
				String expressionText = expressionField.getText();
				if (expressionText != null && msg.contains("Extra operands")) {
					String suggestion = suggestFunctionName(expressionText);
					if (suggestion != null) {
						msg = suggestion;
					}
				}
				
				reportError(msg);
			} else {
				reportError("Invalid expression syntax. Please check your input.");
			}
			System.err.println("ParseException occurred:");
			e.printStackTrace();
			return;
		}
		
		try {
			Dimension size = target.getSize();

			for (int imageY = 0; imageY < size.height; imageY++) {
				double evalY = imageToDomainScale(imageY, size.height);
				for (int imageX = 0; imageX < size.width; imageX++) {
					double evalX = imageToDomainScale(imageX, size.width);
					Color pixelColor = expr.evaluate(evalX, evalY).toJavaColor();
					target.setColor(imageX, imageY, pixelColor);
				}
			}
		} catch (ArithmeticException e) {
			reportError("Math error: division by zero or invalid calculation.");
			System.err.println("ArithmeticException during evaluation:");
			e.printStackTrace();
		} catch (NullPointerException | ClassCastException e) {
			System.err.println("Programming error during evaluation:");
			e.printStackTrace();
			reportError("An internal error occurred. Please report this issue.");
		} catch (Exception e) {
			System.err.println("Unexpected error during evaluation:");
			e.printStackTrace();
			reportError("Unable to evaluate expression. Please try a different one.");
		}
	}
	
	/**
	 * Cleans up error messages to be more user-friendly
	 */
	private String cleanErrorMessage(String msg) {
		msg = msg.replaceAll("(?i)ParseException:\\s*", "");
		msg = msg.replaceAll("java\\.lang\\.\\w+:\\s*", "");
		msg = msg.replaceAll("line \\d+:\\d+\\s*", "");
		
		msg = msg.replace("at input", "with");
		msg = msg.replace("extraneous input", "unexpected");
		msg = msg.replace("mismatched input", "unexpected");
		
		msg = msg.trim();
		
		if (!msg.isEmpty()) {
			msg = Character.toUpperCase(msg.charAt(0)) + msg.substring(1);
		}
		
		return msg;
	}
	
	/**
	 * Detects potential misspelled function names and suggests corrections
	 */
	private String suggestFunctionName(String expression) {
		String[][] commonFunctions = {
			{"perlinbw", "perlinBW"},
			{"perlincolor", "perlinColor"},
			{"imagewrap", "imageWrap"},
			{"imageclip", "imageClip"},
			{"rgbtoycc", "rgbToYCrCb"},
			{"yccbtorgb", "yCrCbtoRGB"}
		};
		
		String lowerExpr = expression.toLowerCase();
		
		for (String[] pair : commonFunctions) {
			if (lowerExpr.contains(pair[0] + "(")) {
				return "Unknown function '" + pair[0] + "'. Did you mean '" + pair[1] + "'?";
			}
		}
		
		if (expression.matches(".*\\b[a-zA-Z][a-zA-Z0-9]*\\s*\\(.*")) {
			return "Unknown function name. Check spelling and capitalization.";
		}
		
		return null;
	}
	
	/**
	 * Helper method to report errors (handles null errorReporter gracefully)
	 */
	private void reportError(String message) {
		if (errorReporter != null) {
			errorReporter.reportError(message);
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
	 * @throws ParseException if the expression cannot be parsed
	 */
	private ExpressionTreeNode createExpression() throws ParseException {
		String expressionText = expressionField.getText();
		
		if (expressionText == null || expressionText.trim().isEmpty()) {
			throw new ParseException("Please enter an expression.");
		}
		
		return expTreeGen.makeExpression(expressionText);
	}
}