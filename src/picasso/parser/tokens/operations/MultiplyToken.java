package picasso.parser.tokens.operations;

import picasso.parser.language.CharConstants;
import picasso.parser.tokens.chars.CharToken;

/**
 * Represents the multiply sign token
 */
public class MultiplyToken extends CharToken implements OperationInterface {
	public MultiplyToken() {
		super(CharConstants.STAR);
	}

	@Override
	public int orderOfOperation(){
		return 2;
	}
}
