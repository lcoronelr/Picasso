package picasso.parser.tokens.operations;

/**
 * A marker interface
 * 
 */
public interface OperationInterface {
    
    /**
	 * Returns the order of operation of the token:
     * 1: addition or subtraction
     * 2: multiplication, division, or modulo
     * 3: exponent
	 * 
	 * @return int representing the Token's order of operation
	 */
	public abstract int orderOfOperation();
}
