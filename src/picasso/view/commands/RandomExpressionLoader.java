package picasso.view.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import picasso.model.Pixmap;
import picasso.parser.language.BuiltinFunctionsReader;
import picasso.util.FileCommand;

/**
 *
 * @author Therese Elvira Mombou Gatsing
 */
public class RandomExpressionLoader extends FileCommand<Pixmap> {

    private final JComponent view;
    private final JTextField expressionField;
    private final Random rand = new Random();

    private static final int MAX_DEPTH = 10;

    private static final List<String> BINARY_OPERATORS = List.of(
            "+", "-", "*", "/", "%", "^"
    );

    // loaded from Picasso's functions.conf
    private final List<String> unaryFunctions = new ArrayList<>();

    // Maps function name to required arity.
    private final Map<String, Integer> multiArgFunctions = new HashMap<>();

    private static final List<String> IMAGE_FILES = List.of(
        "AmoebaMorris.png",
        "beholder.jpg",
        "birthofcolor.jpg",
        "BlackStripeNock.png",
        "BlackWhiteTomlinson.png",
        "BrightThompson.png",
        "ColorsNock.png",
        "deepspiral.jpg",
        "DiscoMorris.png",
        "flyingcarpet.jpg",
        "foo.jpg",
        "hyperspace.jpg",
        "microbes.jpg",
        "Mirror.png",
        "northcross.jpg",
        "Oil.png",
        "PinkSmoke.png",
        "Psych.png",
        "ReflectionMcLaughin.png",
        "SmallReflectMcLaughlin.png",
        "Smear.png",
        "thread.jpg",
        "vortex.jpg",
        "Waves.png"
    );

    public RandomExpressionLoader(JComponent view, JTextField expressionField) {
        super(JFileChooser.OPEN_DIALOG);
        this.view = view;
        this.expressionField = expressionField;
        initializeFunctions();
    }

   
    private void initializeFunctions() {
        List<String> allFunctions = BuiltinFunctionsReader.getFunctionsList();

        Map<String, Integer> knownMultiArgByName = Map.of(
                "perlinColor", 2,
                "perlinBW",    2,
                "imageWrap",   3,
                "imageClip",   3
        );

        for (String f : allFunctions) {
            if (knownMultiArgByName.containsKey(f)) {
                int arity = knownMultiArgByName.get(f);
                multiArgFunctions.put(f, arity);
            } else {
                unaryFunctions.add(f);
            }
        }
    }

    @Override
    public void execute(Pixmap target) {
 
        String randomExpr = generateTopLevelExpression();
        expressionField.setText(randomExpr);
        Evaluator evaluator = new Evaluator(expressionField);
        evaluator.execute(target);
      
    }
    

  
    private String generateTopLevelExpression() {
    	// 3 here is just a design choice so expressions aren’t too simple or too huge.
        int termCount = 1 + rand.nextInt(3);

        String expr = generateRandomExpression(MAX_DEPTH);

        for (int i = 1; i < termCount; i++) {
            String op = BINARY_OPERATORS.get(rand.nextInt(BINARY_OPERATORS.size()));
            String next = generateRandomExpression(MAX_DEPTH);
            expr = "(" + expr + " " + op + " " + next + ")";
        }

        return expr;
    }
    

    /**
     * Recursively generate a random expression string.
     *
     * @param depth remaining depth allowed for nesting
     * @return a random expression
     */
    private String generateRandomExpression(int depth) {
        if (depth <= 0) {
            return generateLeaf();
        }


        int choice = rand.nextInt(5);

        switch (choice) {
            case 0:
                return generateLeaf();

            case 1:
                return generateUnaryFunction(depth);

            case 2:
                return generateBinaryOperation(depth);

            case 3:
                return generateMultiArgFunction(depth);

            case 4:
            default:
                return generateNegateExpression(depth);
        }
    }

    /**
     * Generate a leaf expression: x, y, or a constant.
     */
    private String generateLeaf() {
        int choice = rand.nextInt(3);

        switch (choice) {
            case 0:
                return "x";
            case 1:
                return "y";
            case 2:
            default:
                // random constant in [-1, 1] with 2 decimal places
                double value = -1.0 + 2.0 * rand.nextDouble();
                return String.format("%.2f", value);
        }
    }
    

    /**
     * Generate a unary function application
     */
    private String generateUnaryFunction(int depth) {
        if (unaryFunctions.isEmpty()) {
            return generateLeaf();
        }

        String fn = unaryFunctions.get(rand.nextInt(unaryFunctions.size()));
        String inner = generateRandomExpression(depth - 1);

        return fn + "(" + inner + ")";
    }

    
    /**
     * Generate negate operator application
     */
    private String generateNegateExpression(int depth) {
        String inner = generateRandomExpression(depth - 1);
        return "!(" + inner + ")";
    }
    

    /**
     * Generate a binary operation application
     */
    private String generateBinaryOperation(int depth) {
        String left = generateRandomExpression(depth - 1);
        String right = generateRandomExpression(depth - 1);
        String op = BINARY_OPERATORS.get(rand.nextInt(BINARY_OPERATORS.size()));
        return "(" + left + " " + op + " " + right + ")";
    }
    

    /**
     * Generate a multi-argument function application
     */
    private String generateMultiArgFunction(int depth) {
        if (multiArgFunctions.isEmpty()) {
            return generateUnaryFunction(depth);
        }

        List<String> names = new ArrayList<>(multiArgFunctions.keySet());
        String fn = names.get(rand.nextInt(names.size()));
        int arity = multiArgFunctions.get(fn);

        if ("imageClip".equals(fn) || "imageWrap".equals(fn)) {
            return generateImageFunction(fn, depth, arity);
        }

        // For perlinColor, perlinBW
        StringBuilder builder = new StringBuilder();
        builder.append(fn).append("(");

        for (int i = 0; i < arity; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(generateRandomExpression(depth - 1));
        }

        builder.append(")");
        return builder.toString();
    }
    
    

    /**
     * Generate imageClip/imageWrap with a filename string and coordinate expressions
     */
    private String generateImageFunction(String fn, int depth, int arity) {
        StringBuilder builder = new StringBuilder();
        builder.append(fn).append("(");

        // First argument: "images/<file>"
        String file;   
        file = IMAGE_FILES.get(rand.nextInt(IMAGE_FILES.size()));
        builder.append("\"images/").append(file).append("\"");

        // Remaining arguments
        for (int i = 1; i < arity; i++) {
            builder.append(", ");
            builder.append(generateCoordinateExpression(depth - 1));
        }

        builder.append(")");
        return builder.toString();
    }
    

    /**
     * Generate coordinate expression for imageClip/imageWrap:
     * uses everything except multi-arg functions to avoid deep nesting
     */
    private String generateCoordinateExpression(int depth) {
        if (depth <= 0) {
            return generateLeaf();
        }

        int choice = rand.nextInt(4);

        switch (choice) {
            case 0:
                return generateLeaf();

            case 1:
                if (unaryFunctions.isEmpty()) {
                    return generateLeaf();
                }
                String fn = unaryFunctions.get(rand.nextInt(unaryFunctions.size()));
                return fn + "(" + generateCoordinateExpression(depth - 1) + ")";

            case 2:
                String left = generateCoordinateExpression(depth - 1);
                String right = generateCoordinateExpression(depth - 1);
                String op = BINARY_OPERATORS.get(rand.nextInt(BINARY_OPERATORS.size()));
                return "(" + left + " " + op + " " + right + ")";

            case 3:
            default:
                return "!(" + generateCoordinateExpression(depth - 1) + ")";
        }
    }
}
