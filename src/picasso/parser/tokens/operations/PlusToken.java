package picasso.parser.tokens.operations;

import picasso.parser.language.CharConstants;
import picasso.parser.tokens.chars.CharToken;
import picasso.parser.OperatorPrecedence;

/**
 * Represents the plus sign token
 */
public class PlusToken extends CharToken implements OperationInterface {
	public PlusToken() {
		super(CharConstants.PLUS);
	}

	@Override
	public int orderOfOperation(){
		return OperatorPrecedence.ADDITIVE;
	}
}
