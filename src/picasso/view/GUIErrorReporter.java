package picasso.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import picasso.util.ErrorReporter;

/**
 * Sets the details for the pop-up window and status bar for error status.
 *
 * @author __Menilik__ __Deneke__
 **/
public class GUIErrorReporter implements ErrorReporter {
	private Label errorLabel;
	private Panel statusPanel;
	private Frame parentFrame;

	public GUIErrorReporter(Panel parentPanel) {
		statusPanel = new Panel();
		errorLabel = new Label("Ready");
		errorLabel.setForeground(Color.RED);
		statusPanel.add(errorLabel);

		parentPanel.add(statusPanel, "South");
		this.parentFrame = null;
	}

	public GUIErrorReporter(Label statusLabel) {
		this.errorLabel = statusLabel;
		this.errorLabel.setForeground(Color.RED);
		this.statusPanel = null;
		this.parentFrame = null;
	}

	public GUIErrorReporter(Label statusLabel, Frame parentFrame) {
		this.errorLabel = statusLabel;
		this.errorLabel.setForeground(Color.RED);
		this.statusPanel = null;
		this.parentFrame = parentFrame;
	}

	@Override
	public void reportError(String message) {
		errorLabel.setText("Error: " + message);
		errorLabel.setVisible(true);

		if (parentFrame != null) {
			showErrorDialog(message);
		}

		System.err.println("GUI Error Reported: " + message);
	}

	@Override
	public void clearError() {
		errorLabel.setText("Ready");
		errorLabel.setVisible(true);
	}

	private void showErrorDialog(String message) {
		Dialog dialog = new Dialog(parentFrame, "Error", true);
		dialog.setLayout(new BorderLayout());

		Label messageLabel = new Label(message);
		messageLabel.setAlignment(Label.CENTER);
		dialog.add(messageLabel, BorderLayout.CENTER);

		Button okButton = new Button("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});

		Panel buttonPanel = new Panel();
		buttonPanel.add(okButton);
		dialog.add(buttonPanel, BorderLayout.SOUTH);

		dialog.setSize(675, 140);
		dialog.setLocationRelativeTo(parentFrame);
		dialog.setVisible(true);
	}
}