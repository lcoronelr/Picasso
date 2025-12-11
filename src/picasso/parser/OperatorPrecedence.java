package picasso.parser;

/**
 * Defines the order of operation and precedence
 * 
 * @author Asya Yurkovskaya
 *
 */
public class OperatorPrecedence {
    public static final int ASSIGNMENT = 1;
    public static final int ADDITIVE = 2;
    public static final int MULTIPLICATIVE = 3;
    public static final int EXPONENTIATION = 4;
    public static final int NEGATION = 5;
}