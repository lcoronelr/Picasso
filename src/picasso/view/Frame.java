package picasso.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

import picasso.model.Pixmap;
import picasso.util.ThreadedCommand;
import picasso.view.commands.*;

import javax.swing.*;


/**
 * Main container for the Picasso application
 *
 * @author Robert Duvall (rcd@cs.duke.edu)
 * @author Luis Coronel - added some components 
 * 
 */
@SuppressWarnings("serial")
public class Frame extends JFrame {
	private JTextField expression;

	public Frame(Dimension size) {
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// create GUI components
		Canvas canvas = new Canvas(this);
		canvas.setSize(size);


		//input 
		JPanel inputPanel = new JPanel();
		JLabel label = new JLabel("Input Expression:");  // text label
		expression = new JTextField(30); // 30 characters , really wide for long expressions input
		inputPanel.add(label);
		inputPanel.add(expression);

		// add commands to test here
		ButtonPanel commands = new ButtonPanel(canvas);
		commands.add("Open", new Reader());
		commands.add("Evaluate", new ThreadedCommand<Pixmap>(canvas, new Evaluator()));
		commands.add("Save", new Writer());

		//puts together the input panel and the commands panel
		JPanel topPart = new JPanel(new BorderLayout());
		topPart.add(inputPanel, BorderLayout.NORTH);
		topPart.add(commands, BorderLayout.SOUTH);

		// puts together the canvas and the top part (contains input panel and commands)
		getContentPane().add(canvas, BorderLayout.CENTER);
		getContentPane().add(topPart, BorderLayout.NORTH);
		pack();

	}
}
