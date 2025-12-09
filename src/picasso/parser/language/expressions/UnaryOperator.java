package picasso.parser.language.expressions;
import picasso.parser.language.ExpressionTreeNode;

/**
 * 
 * 
 * @author Therese Elvira Mombou Gatsing
 */
public abstract class UnaryOperator  extends ExpressionTreeNode{
	
	ExpressionTreeNode param;
	
	/**
	 * 
	 * @param param
	 */
	public UnaryOperator(ExpressionTreeNode param) {
		this.param = param;        
	}
	
	/**
	 * Returns the string representation of the function in the format "<ClassName>:
	 * <param>"
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String classname = this.getClass().getName();
		return classname.substring(classname.lastIndexOf(".") + 1) + "(" + param + ")";
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (!(o instanceof UnaryOperator)) {
			return false;
		}

		// Make sure the objects are the same type
		if (o.getClass() != this.getClass()) {
			return false;
		}

		UnaryOperator uf = (UnaryOperator) o;

		// check if their parameters are equal
		if (!this.param.equals(uf.param)) {
			return false;
		}
		return true;
	}

}
