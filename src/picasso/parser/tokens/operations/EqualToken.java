package picasso.parser.tokens.operations;

import picasso.parser.language.CharConstants;
import picasso.parser.tokens.chars.CharToken;

/**
 * 
 * 
 * @author Therese Elvira Mombou Gatsing
 */
public class EqualToken extends CharToken implements OperationInterface {
	
	public EqualToken() {
		super(CharConstants.EQUAL);
	}
	
	@Override
    public String toString() {
        return "EqualToken";
    }
	
	@Override
	public int orderOfOperation(){
		return 0;
	}
	
}
