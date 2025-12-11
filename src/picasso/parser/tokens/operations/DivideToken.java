package picasso.parser.tokens.operations;

import picasso.parser.language.CharConstants;
import picasso.parser.tokens.chars.CharToken;
import picasso.parser.OperatorPrecedence;


/**
 * Represents the divide sign token
 */
public class DivideToken extends CharToken implements OperationInterface {
	public DivideToken() {
		super(CharConstants.SLASH);
	}

	@Override
	public int orderOfOperation(){
		return OperatorPrecedence.MULTIPLICATIVE;
	}
}
