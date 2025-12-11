package picasso.view;



import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

/**
 * Bounded, most-recent-first history of evaluated expressions.
 * Notifies listeners when the history changes so UIs can stay in sync.
 */
public class ExpressionHistory {
    private final Deque<String> expressions = new ArrayDeque<>();
    private final int maxEntries;
    private final List<Consumer<List<String>>> listeners = new ArrayList<>();

    public ExpressionHistory() {
        this(50);
    }

    public ExpressionHistory(int maxEntries) {
        if (maxEntries <= 0) {
            throw new IllegalArgumentException("maxEntries must be positive");
        }
        this.maxEntries = maxEntries;
    }

    /**
     * Adds the given expression to the front of history, keeping entries unique and bounded.
     * Blank or null expressions are ignored.
     *
     * @param expression user-entered expression to store
     */
    public synchronized void add(String expression) {
        if (expression == null) {
            return;
        }
        String trimmed = expression.trim();
        if (trimmed.isEmpty()) {
            return;
        }
        expressions.remove(trimmed);
        expressions.addFirst(trimmed);
        trimToMax();
        notifyListeners();
    }

    /**
     * Removes all expressions from history.
     */
    public synchronized void clear() {
        expressions.clear();
        notifyListeners();
    }

    /**
     * Returns a snapshot of history in most-recent-first order.
     *
     * @return immutable copy of stored expressions
     */
    public synchronized List<String> snapshot() {
        return new ArrayList<>(expressions);
    }

    /**
     * Registers a listener that will receive the full snapshot when history changes.
     *
     * @param listener callback invoked on history updates
     */
    public void addListener(Consumer<List<String>> listener) {
        listeners.add(listener);
    }

    private void trimToMax() {
        while (expressions.size() > maxEntries) {
            expressions.removeLast();
        }
    }

    private void notifyListeners() {
        List<String> copy = snapshot();
        for (Consumer<List<String>> listener : listeners) {
            listener.accept(copy);
        }
    }
}