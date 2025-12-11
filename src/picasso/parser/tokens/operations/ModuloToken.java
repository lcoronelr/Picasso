package picasso.parser.tokens.operations;

import picasso.parser.language.CharConstants;
import picasso.parser.tokens.chars.CharToken;
import picasso.parser.OperatorPrecedence;

/**
 * Represents the modulo sign token
 */
public class ModuloToken extends CharToken implements OperationInterface {
	public ModuloToken() {
		super(CharConstants.MOD);
	}

	@Override
	public int orderOfOperation(){
		return OperatorPrecedence.MULTIPLICATIVE;
	}
}
