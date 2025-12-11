package picasso.view.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import picasso.model.Pixmap;
import picasso.util.ErrorReporter;
import picasso.util.FileCommand;
import picasso.util.ThreadedCommand;
import picasso.view.ExpressionHistory;

/**
 * Opens either an image or expression file. Images are read into the pixmap;
 * expression files populate the input field and trigger evaluation for each
 * non-blank, non-comment line in the file. Inline comments after // are
 * removed before evaluation.
 * 
 * @author Robert C Duvall
 */
public class Reader extends FileCommand<Pixmap> {

	private final JComponent view;
	private final JTextField expressionField;
	private final ErrorReporter errorReporter;
	private final ExpressionHistory history;

	public Reader(JComponent view, JTextField expressionField, ErrorReporter errorReporter, ExpressionHistory history) {
		super(JFileChooser.OPEN_DIALOG);
		this.view = view;
		this.expressionField = expressionField;
		this.errorReporter = errorReporter;
		this.history = history;
	}

	// Backward compatibility constructors
	public Reader(JComponent view, JTextField expressionField, ErrorReporter errorReporter) {
		this(view, expressionField, errorReporter, null);
	}

	public Reader(JComponent view, JTextField expressionField) {
		this(view, expressionField, null, null);
	}

	/**
	 * Displays the image file on the given target or evaluates an expression file.
	 */
	public void execute(Pixmap target) {
		String fileName = getFileName();
		if (fileName == null) {
			return; // user cancelled
		}

		if (isImageFile(fileName)) {
			expressionField.setText("");
			target.read(fileName);
			return;
		}

		try {
			List<String> lines = Files.readAllLines(Paths.get(fileName));
		for (String line : lines) {
				String expr = stripInlineComment(line).trim();
				if (expr.isEmpty()) {
					continue; // skip blank or comment-only lines
				}
				expressionField.setText(expr);
				Evaluator evaluator = (errorReporter != null)
						? new Evaluator(expressionField, errorReporter, history)
						: new Evaluator(expressionField, null, history);
				new ThreadedCommand<Pixmap>(view, evaluator).execute(target);
			}

		} catch (IOException e) {
			if (errorReporter != null) {
				errorReporter.reportError("Error reading file: " + e.getMessage());
			} else {
				e.printStackTrace();
			}
		}
	}

	private boolean isImageFile(String fileName) {
		String lower = fileName.toLowerCase();
		return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png") || lower.endsWith(".gif")
				|| lower.endsWith(".bmp");
	}

	private String stripInlineComment(String line) {
		if (line == null) {
			return "";
		}
		int commentStart = line.indexOf("//");
		return (commentStart >= 0) ? line.substring(0, commentStart) : line;
	}
}