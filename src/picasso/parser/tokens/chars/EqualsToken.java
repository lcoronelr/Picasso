package picasso.parser.tokens.chars;

import picasso.parser.language.CharConstants;


/**
 * Represents the '=' character 
 * 
 * @author Therese Elvira Mombou Gatsing
 */

public class EqualsToken extends CharToken {
	public EqualsToken() {
		super(CharConstants.EQUAL);
	}
	
	
	@Override
    public String toString() {
        return "EqualsToken";
    }
	
	

}
