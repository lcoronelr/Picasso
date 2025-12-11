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

/**
 * Opens either an image or expression file. Images are read into the pixmap;
 * expression files populate the input field and are evaluated. 
 * 
 * @author Robert C Duvall
 */
public class Reader extends FileCommand<Pixmap> {

	private final JComponent view;
	private final JTextField expressionField;
	private final ErrorReporter errorReporter; // Added field

	/**
	 * Creates a Reader object, which prompts users for image or expression files to open.
	 */
	public Reader(JComponent view, JTextField expressionField, ErrorReporter errorReporter) { // Updated constructor
		super(JFileChooser.OPEN_DIALOG);
		this.view = view;
		this.expressionField = expressionField;
		this.errorReporter = errorReporter;
	}
	
	// Keep backward compatibility constructor for tests
	public Reader(JComponent view, JTextField expressionField) {
		this(view, expressionField, null);
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
			String expr = readExpression(fileName);
			expressionField.setText(expr);
			if (!expr.isEmpty()) {
				// Use error reporter if available, otherwise use the basic Evaluator
				if (errorReporter != null) {
					new ThreadedCommand<Pixmap>(view, new Evaluator(expressionField, errorReporter)).execute(target);
				}
			}
		} catch (IOException e) {
			// Report error if errorReporter is available
			if (errorReporter != null) {
				errorReporter.reportError("Error reading file: " + e.getMessage());
			} else {
				// keep UI silent on errors; log to console for debugging
				e.printStackTrace();
			}
		}
	}

	private boolean isImageFile(String fileName) {
		String lower = fileName.toLowerCase();
		return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png") || lower.endsWith(".gif")
				|| lower.endsWith(".bmp");
	}

	private String readExpression(String fileName) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(fileName));
		StringBuilder builder = new StringBuilder();
		for (String line : lines) {
			int commentStart = line.indexOf("//");
			String withoutComment = commentStart >= 0 ? line.substring(0, commentStart) : line;
			String trimmed = withoutComment.trim();
			if (trimmed.isEmpty()) {
				continue;
			}
			if (builder.length() > 0) {
				builder.append('\n');
			}
			builder.append(trimmed);
		}
		return builder.toString();
	}
}
