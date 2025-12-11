package picasso.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * UI component that shows evaluated expressions and lets users re-use them.
 * Selecting an entry populates the expression input field and triggers the
 * provided evaluation action.
 */
@SuppressWarnings("serial")
public class ExpressionHistoryPanel extends JPanel {

	private final ExpressionHistory history;
	private final JTextField expressionField;
	private final DefaultListModel<String> model = new DefaultListModel<>();
	private final JList<String> list = new JList<>(model);
	private final Runnable evaluateAction;

	/**
	 * @param history          shared history model
	 * @param expressionField  target text field to populate
	 * @param evaluateAction   action to run after loading a selection (may be null)
	 */
	public ExpressionHistoryPanel(ExpressionHistory history, JTextField expressionField, Runnable evaluateAction) {
		super(new BorderLayout(4, 4));
		this.history = history;
		this.expressionField = expressionField;
		this.evaluateAction = evaluateAction;

		setBorder(BorderFactory.createTitledBorder("History"));
		list.setVisibleRowCount(6);
		list.setFixedCellWidth(220);

		add(new JLabel("Recent expressions:"), BorderLayout.NORTH);
		add(new JScrollPane(list), BorderLayout.CENTER);
		add(buildButtonBar(), BorderLayout.SOUTH);
		setPreferredSize(new Dimension(260, 160));

		history.addListener(this::refreshHistory);
		refreshHistory(history.snapshot());
		wireSelection();
	}

	private JPanel buildButtonBar() {
		JPanel buttons = new JPanel();
		JButton useButton = new JButton("Use Selected");
		useButton.addActionListener(e -> loadSelected());
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(e -> history.clear());
		buttons.add(useButton);
		buttons.add(clearButton);
		return buttons;
	}

	private void wireSelection() {
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					loadSelected();
				}
			}
		});
	}

	private void loadSelected() {
		String selected = list.getSelectedValue();
		if (selected != null) {
			expressionField.setText(selected);
			expressionField.requestFocusInWindow();
			expressionField.setCaretPosition(selected.length());
			if (evaluateAction != null) {
				evaluateAction.run();
			}
		}
	}

	private void refreshHistory(List<String> entries) {
		SwingUtilities.invokeLater(() -> {
			model.clear();
			entries.forEach(model::addElement);
		});
	}
}