package picasso.parser.tokens;

/**
 * Represents a string literal token (for image filenames)
 * 
 * @author Luis Coronel
 */
public class StringToken extends Token {
	
	private String value;
	
	public StringToken(String value) {
		super("String:" + value);
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public boolean isConstant() {
		return true;  
	}
	
	@Override
	public boolean isFunction() {
		return false;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof StringToken)) return false;
		return value.equals(((StringToken) o).value);
	}
}