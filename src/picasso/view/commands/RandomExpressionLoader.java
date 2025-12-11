package picasso.view.commands;

import java.io.File;
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
import picasso.util.ErrorReporter;

/**
 * Loads and evaluates a randomly generated Picasso expression.
 *
 * @author Therese Elvira Mombou Gatsing
 */
public class RandomExpressionLoader extends FileCommand<Pixmap> {

    private final JComponent view;
    private final JTextField expressionField;
    private final Random rand = new Random();
    private ErrorReporter errorReporter;

   
    private static final int MAX_DEPTH = 10;

    private static final List<String> BINARY_OPERATORS = List.of(
            "+", "-", "*", "/", "%", "^"
    );

    private final List<String> unaryFunctions = new ArrayList<>();

    private final List<String> zeroArgFunctions = new ArrayList<>();

    // Maps function name to required arity.
    private final Map<String, Integer> multiArgFunctions = new HashMap<>();

    private final List<String> imagePaths = new ArrayList<>();

    public RandomExpressionLoader(JComponent view, JTextField expressionField) {
        super(JFileChooser.OPEN_DIALOG);
        this.view = view;
        this.expressionField = expressionField;
        initializeFunctions();
        initializeImageFiles();
    }

    /**
     * Initialize unary, zero-arg, and multi-arg functions from functions.conf.
     */
    private void initializeFunctions() {
        List<String> allFunctions = BuiltinFunctionsReader.getFunctionsList();

        Map<String, Integer> knownMultiArgByName = Map.of(
                "perlinColor", 2,
                "perlinBW",    2,
                "imageWrap",   3,
                "imageClip",   3,
                "mandelbrot",  2  
        );

        for (String f : allFunctions) {
            if (knownMultiArgByName.containsKey(f)) {
                int arity = knownMultiArgByName.get(f);
                multiArgFunctions.put(f, arity);
            } else if ("random".equals(f) || "randomFunction".equals(f)) { 
                zeroArgFunctions.add(f);
            } else {
                unaryFunctions.add(f);
            }
        }
    }

    
    /**
     * Loads all image files from the "images" folder.
     */
    private void initializeImageFiles() {
        File imagesDir = new File("images");
        if (!imagesDir.isDirectory()) {
            return;
        }

        File[] files = imagesDir.listFiles(f -> {
            if (!f.isFile()) return false;
            String name = f.getName().toLowerCase();
            return name.endsWith(".png") || name.endsWith(".jpg")
                    || name.endsWith(".jpeg") || name.endsWith(".gif");
        });

        if (files == null) {
            return;
        }

        for (File f : files) {
            imagePaths.add("images/" + f.getName());
        }
    }
    

    @Override
    public void execute(Pixmap target) {
        String randomExpr = generateTopLevelExpression();
        expressionField.setText(randomExpr);

        Evaluator evaluator = new Evaluator(expressionField, errorReporter);
        evaluator.execute(target);
    }

    
    /**
     * Builds a top-level expression by combining 1–3 smaller expressions 
     * with random binary operators.
     */
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
     * @return a random expression
     */
    private String generateRandomExpression(int depth) {
        if (depth <= 0) {
            // At depth 0, sometimes generates random().
            if (!zeroArgFunctions.isEmpty() && rand.nextInt(5) == 0) {
                return generateZeroArgFunction();
            }
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
     * Leaf expression:
     *  - x
     *  - y
     *  - numeric constant in [-1,1]
     *  - random color literal [r,g,b]
     */
    private String generateLeaf() {
        
        int choice = rand.nextInt(4);

        switch (choice) {
            case 0:
                return "x";
            case 1:
                return "y";
            case 2: {
                double value = -1.0 + 2.0 * rand.nextDouble();
                return String.format("%.2f", value);
            }
            case 3:
            default:
                return generateRandomColorLiteral();
        }
    }

    /**
     * Generate random color literal
     */
    private String generateRandomColorLiteral() {
        double r = -1.0 + 2.0 * rand.nextDouble();
        double g = -1.0 + 2.0 * rand.nextDouble();
        double b = -1.0 + 2.0 * rand.nextDouble();
        return String.format("[%.2f, %.2f, %.2f]", r, g, b);
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
     * Generate a zero-argument function 
     */
    private String generateZeroArgFunction() {
        if (zeroArgFunctions.isEmpty()) {
            return generateLeaf();
        }
        String fn = zeroArgFunctions.get(rand.nextInt(zeroArgFunctions.size()));
        return fn + "()";
    }

    /**
     * Prevents the generation of "!!x" since it is not handle in the program
     */
    private String negate(String inner) {
        String trimmed = inner.trim();
        if (trimmed.startsWith("!")) {
            return inner;
        }
        return "!(" + inner + ")";
    }

    /**
     * Generate negate operator application.
     */
    private String generateNegateExpression(int depth) {
        String inner = generateRandomExpression(depth - 1);
        return negate(inner);
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
     * Multi-argument functions:
     *   perlinColor(expr, expr)
     *   perlinBW(expr, expr)
     *   imageWrap("file", coord, coord)
     *   imageClip("file", coord, coord)
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

        // perlinColor, perlinBW
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
        file = imagePaths.get(rand.nextInt(imagePaths.size()));
        builder.append("\"").append(file).append("\"");

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
            if (!zeroArgFunctions.isEmpty() && rand.nextInt(5) == 0) { 
                return generateZeroArgFunction();
            }
            return generateLeaf();
        }

        
        int choice = rand.nextInt(4);

        switch (choice) {
            case 0:
                return generateLeaf();
                
            case 1: {
                if (unaryFunctions.isEmpty()) {
                    return generateLeaf();
                }
                String fn = unaryFunctions.get(rand.nextInt(unaryFunctions.size()));
                return fn + "(" + generateCoordinateExpression(depth - 1) + ")";
            }
            
            case 2: {
                String left = generateCoordinateExpression(depth - 1);
                String right = generateCoordinateExpression(depth - 1);
                String op = BINARY_OPERATORS.get(rand.nextInt(BINARY_OPERATORS.size()));
                return "(" + left + " " + op + " " + right + ")";
            }
            
            case 3:
            default: {
                String inner = generateCoordinateExpression(depth - 1);
                return negate(inner);
            }
        }
    }
}
