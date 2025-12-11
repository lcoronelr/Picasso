package picasso.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import picasso.model.Pixmap;
import picasso.util.ErrorReporter;
import picasso.util.ThreadedCommand;
import picasso.view.commands.*;

/**
 * Main container for the Picasso application
 *
 * @author Robert Duvall (rcd@cs.duke.edu)
 * @author Luis Coronel - added some components
 * @author Menilik Deneke - added some components for error reporting 
 * 
 */
@SuppressWarnings("serial")
public class Frame extends JFrame {
	private JTextField expression;
	private ErrorReporter errorReporter;
	private Label statusLabel;

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

		//input 
		JPanel inputPanel = new JPanel();
		JLabel label = new JLabel("Input Expression:");  // text label
		expression = new JTextField(30); // 30 characters , really wide for long expressions input
		inputPanel.add(label);
		inputPanel.add(expression);

		// add commands to test here
		ButtonPanel commands = new ButtonPanel(canvas);
		commands.add("Open", new Reader(canvas, expression)); // Updated to include errorReporter
		commands.add("Evaluate", new ThreadedCommand<Pixmap>(canvas, new Evaluator(expression, errorReporter)));
		commands.add("Save", new Writer(expression));
		commands.add("GenerateRandomExpression", new RandomExpressionLoader(canvas, expression ));

		
		// Add action listener so pressing Enter evaluates
		expression.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ThreadedCommand<Pixmap>(canvas, new Evaluator(expression, errorReporter)).execute(canvas.getPixmap());
				canvas.refresh();
			}
		});
		
		// Create a panel for the status bar
		JPanel statusPanel = new JPanel(new BorderLayout());
		statusPanel.add(statusLabel, BorderLayout.WEST);
			
		//puts together the input panel and the commands panel
		JPanel topPart = new JPanel(new BorderLayout());
		topPart.add(inputPanel, BorderLayout.NORTH);
		topPart.add(commands, BorderLayout.SOUTH);

		// puts together the canvas and the top part (contains input panel and commands)
		getContentPane().add(canvas, BorderLayout.CENTER);
		getContentPane().add(topPart, BorderLayout.NORTH);
		getContentPane().add(statusPanel, BorderLayout.SOUTH);
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
