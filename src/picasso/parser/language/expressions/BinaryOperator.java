package picasso.parser.language.expressions;

import picasso.parser.language.ExpressionTreeNode;

/**
 * Represents a function that takes one argument.
 * 
 * @author Robert C. Duvall
 * @author Asya Yurkovskaya
 *
 */
public abstract class BinaryOperator extends ExpressionTreeNode {

	ExpressionTreeNode left;
	ExpressionTreeNode right;

	/**
	 * 
	 * @param param
	 */
	public BinaryOperator(ExpressionTreeNode left, ExpressionTreeNode right) {
		this.left = left;
        this.right = right;
	}

	/**
	 * Returns the string representation of the function in the format "<ClassName>:
	 * <left>, <right>"
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String classname = this.getClass().getName();
		return classname.substring(classname.lastIndexOf(".") + 1) + "(" + left + ", " + right + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (!(o instanceof BinaryOperator)) {
			return false;
		}

		// Make sure the objects are the same type

		if (o.getClass() != this.getClass()) {
			return false;
		}

		BinaryOperator uf = (BinaryOperator) o;

		// check if their parameters are equal
		if (!this.left.equals(uf.left) | !this.right.equals(uf.right)) {
			return false;
		}
		return true;
	}

}
}
