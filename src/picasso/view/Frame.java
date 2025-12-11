package picasso.view;

		import java.awt.BorderLayout;
		import java.awt.Dimension;
		import java.awt.Label;
		import java.awt.event.ActionEvent;
		import java.awt.event.ActionListener;
		import java.nio.file.Path;
		import java.nio.file.Paths;

		import javax.swing.JFrame;
		import javax.swing.JLabel;
		import javax.swing.JPanel;
		import javax.swing.JTextField;

		import picasso.model.Pixmap;
		import picasso.util.ErrorReporter;
		import picasso.util.ThreadedCommand;
		import picasso.view.commands.Evaluator;
		import picasso.view.commands.Reader;
		import picasso.view.commands.Writer;
		import picasso.view.commands.*;
		/**
		 * Main container for the Picasso application.
		 * Adds an expression history panel that records successful evaluations and
		 * lets users re-load past expressions. History is persisted to a text file
		 * under the expressions directory and auto-loaded on startup.
		 * 
		 * @author Robert Duvall
		 * @author Luis Coronel - added some components
		 * @author Menilik Deneke - added some components for error reporting
		 * @author Abhishek Pradhan - added some components
		 */
		@SuppressWarnings("serial")
		public class Frame extends JFrame {
			private JTextField expression;
			private ErrorReporter errorReporter;
			private Label statusLabel;
			private ExpressionHistory history;

			public Frame(Dimension size) {
				setDefaultCloseOperation(EXIT_ON_CLOSE);

				// create GUI components
				Canvas canvas = new Canvas(this);
				canvas.setSize(size);

				// Create status label for error reporting
				statusLabel = new Label("Ready");
				statusLabel.setPreferredSize(new Dimension(600, 22));

				// Create error reporter
				errorReporter = new GUIErrorReporter(statusLabel, this);

				// Initialize history and wire persistence
				history = new ExpressionHistory();
				Path historyFile = Paths.get("expressions", "history.exp");
				ExpressionHistoryStorage.load(historyFile, history, errorReporter);
				ExpressionHistoryStorage.attachAutoSave(historyFile, history, errorReporter);

				// input
				JPanel inputPanel = new JPanel();
				JLabel label = new JLabel("Input Expression:"); // text label
				expression = new JTextField(30); // 30 characters , really wide for long expressions input
				inputPanel.add(label);
				inputPanel.add(expression);

				// history panel
				ExpressionHistoryPanel historyPanel = new ExpressionHistoryPanel(
					history,
					expression,
					() -> new ThreadedCommand<Pixmap>(
							canvas,
							new Evaluator(expression, errorReporter, history))
							.execute(canvas.getPixmap())
				);

				// add commands to test here
				ButtonPanel commands = new ButtonPanel(canvas);
				commands.add("Open", new Reader(canvas, expression, errorReporter, history)); // Updated to include errorReporter and history
				commands.add("Evaluate", new ThreadedCommand<Pixmap>(canvas, new Evaluator(expression, errorReporter, history)));
				commands.add("Save", new Writer(expression));
				commands.add("GenerateRandomExpression", new RandomExpressionLoader(canvas, expression ));

				// Add action listener so pressing Enter evaluates
				expression.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new ThreadedCommand<Pixmap>(canvas, new Evaluator(expression, errorReporter, history))
								.execute(canvas.getPixmap());
						canvas.refresh();
					}
				});

				// Create a panel for the status bar
				JPanel statusPanel = new JPanel(new BorderLayout());
				statusPanel.add(statusLabel, BorderLayout.WEST);

				// puts together the input panel and the commands panel
				JPanel topPart = new JPanel(new BorderLayout());
				topPart.add(inputPanel, BorderLayout.NORTH);
				topPart.add(commands, BorderLayout.SOUTH);

				// puts together the canvas and the top part (contains input panel and commands)
				getContentPane().add(canvas, BorderLayout.CENTER);
				getContentPane().add(topPart, BorderLayout.NORTH);
				getContentPane().add(statusPanel, BorderLayout.SOUTH);
				getContentPane().add(historyPanel, BorderLayout.EAST);
				pack();

			}

			// Add getter for error reporter for unit tests
			public ErrorReporter getErrorReporter() {
				return errorReporter;
			}

			// Add getter for expression field if needed
			public JTextField getExpressionField() {
				return expression;
			}
		}