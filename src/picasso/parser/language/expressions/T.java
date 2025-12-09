package picasso.parser.language.expressions;

import picasso.parser.language.ExpressionTreeNode;

/**
 * Represents t in a Picasso expression
 * 
 * @author Asya Yurkovskaya
 * @author Luis Coronel
 * 
 */
public class T extends ExpressionTreeNode {

    private static double tTime = 0;
    private static boolean hasTime = false;

	/**
	 * //FIXME
	 */
	@Override
	public RGBColor evaluate(double x, double y) {
		return new RGBColor(tTime, tTime, tTime);
	}

    public static void increaseTime() {
        tTime += 0.02;
    }

    public static void resetTime() {
        tTime = 0;
    }

    public static void setHasTime(boolean timeBoolean) {
        hasTime = timeBoolean;
    }

    public static boolean getHasTime() {
        return hasTime;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof T)) {
			return false;
		}
		return true;
	}

	/**
	 * Returns "t", the representation of this variable in Picasso expressions
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "t";
	}

}
