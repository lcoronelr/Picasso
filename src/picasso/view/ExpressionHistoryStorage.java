package picasso.view;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import picasso.util.ErrorReporter;

/**
 * Persists expression history to a plain text file (one expression per line).
 * Newest entries are stored first. On load, entries are added to the provided
 * history model in most-recent-first order.
 */
public final class ExpressionHistoryStorage {

	private ExpressionHistoryStorage() {
		// utility class
	}

	/**
	 * Loads history from the given file into the history model.
	 *
	 * @param file         path to the history file
	 * @param history      the history model to populate
	 * @param errorReporter error reporter for IO issues
	 */
	public static void load(Path file, ExpressionHistory history, ErrorReporter errorReporter) {
		if (file == null || history == null) {
			return;
		}
		if (!Files.exists(file)) {
			return;
		}
		try {
			List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
			// file is stored newest-first; add in reverse so recency is preserved
			Collections.reverse(lines);
			for (String line : lines) {
				history.add(line);
			}
		} catch (IOException e) {
			report(errorReporter, "Unable to load expression history: " + e.getMessage());
		}
	}

	/**
	 * Registers an auto-save listener on the history and writes the initial state.
	 *
	 * @param file         path to the history file
	 * @param history      history to persist
	 * @param errorReporter error reporter for IO issues
	 */
	public static void attachAutoSave(Path file, ExpressionHistory history, ErrorReporter errorReporter) {
		if (file == null || history == null) {
			return;
		}
		history.addListener(entries -> save(file, entries, errorReporter));
		save(file, history.snapshot(), errorReporter);
	}

	private static void save(Path file, List<String> entries, ErrorReporter errorReporter) {
		try {
			if (file.getParent() != null) {
				Files.createDirectories(file.getParent());
			}
			Files.write(file, entries, StandardCharsets.UTF_8);
		} catch (IOException e) {
			report(errorReporter, "Unable to save expression history: " + e.getMessage());
		}
	}

 	private static void report(ErrorReporter reporter, String message) {
		if (reporter != null) {
			reporter.reportError(message);
		} else {
			System.err.println(message);
		}
	}
}