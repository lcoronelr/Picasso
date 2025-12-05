package picasso.util;

/**
 * Interface for different types of error reporters.
 * 
 * @author Menilik Deneke
 **/

public interface ErrorReporter {
	
	void reportError(String message);
	
	void clearError();
}