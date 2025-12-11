package picasso.parser.tokens.operations;

import picasso.parser.language.CharConstants;
import picasso.parser.tokens.chars.CharToken;

/**
 * 
 * 
 * @author Therese Elvira Mombou Gatsing
 */
public class EqualToken extends CharToken implements OperationInterface {
	
	/**
	 * Creates a token for the '=' character.
	 */
	public EqualToken() {
		super(CharConstants.EQUAL);
	}
	
	/**
	 * Returns the string representation of this token.
	 */
	@Override
    public String toString() {
        return "EqualToken";
    }
	
	/**
	 * Returns the precedence level for the '=' operator.
	 */
	@Override
	public int orderOfOperation(){
		return 0;
	}
	
}
