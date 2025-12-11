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
 * Loads an expression from a file into the expression field and renders it.
 * @author Abhishek Pradhan
 * @author Menilik Deneke - added some components for error reporting
 */
public class ExpressionFileLoader extends FileCommand<Pixmap> {
	private final JComponent view;
    private final JTextField expressionField;
	private final ErrorReporter errorReporter; 

	public ExpressionFileLoader(JComponent view, JTextField expressionField, ErrorReporter errorReporter) { // Updated
        super(JFileChooser.OPEN_DIALOG);
		this.view = view;
        this.expressionField = expressionField;
		this.errorReporter = errorReporter;
    }
	
	// Backward compatibility constructor
	public ExpressionFileLoader(JComponent view, JTextField expressionField) {
		this(view, expressionField, null);
	}

    @Override
    public void execute(Pixmap target) {
        String fileName = getFileName();
        if (fileName == null) {
			return; // user cancelled dialog
        }
        try {
			List<String> lines = Files.readAllLines(Paths.get(fileName));
			StringBuilder builder = new StringBuilder();
				for (String line : lines) {
					int commentStart = line.indexOf("//");
					String withoutComment = (commentStart >= 0) ? line.substring(0, commentStart) : line;
					String trimmed = withoutComment.trim();
					if (trimmed.isEmpty()) {
						continue; // skip blank lines and comments
					}
					if (builder.length() > 0) {
						builder.append(' ');
					}
					builder.append(trimmed);
				}
			String expr = builder.toString();
			expressionField.setText(expr);
			if (!expr.isEmpty()) {
				Evaluator evaluator;
				evaluator = new Evaluator(expressionField, errorReporter);
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
}