package picasso.parser.tokens.operations;

import picasso.parser.language.CharConstants;
import picasso.parser.tokens.chars.CharToken;
import picasso.parser.OperatorPrecedence;

/**
 * Represents the exponent sign token
 */
public class ExponentToken extends CharToken implements OperationInterface {
	public ExponentToken() {
		super(CharConstants.CARET);
	}

	@Override
	public int orderOfOperation(){
		return OperatorPrecedence.EXPONENTIATION;
	}
}
