package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.Test;

import picasso.parser.ExpressionTreeGenerator;
import picasso.parser.language.ExpressionTreeNode;
import picasso.parser.language.expressions.RGBColor;
import picasso.parser.language.expressions.RgbToYCrCb;
import picasso.parser.language.expressions.X;
import picasso.parser.language.expressions.YCrCbToRGB;

/**
 * Tests for the YCrCbToRGB unary function.
 * 
 * @author Abhishek Pradhan
 */
public class YCrCbToRGBTest {

    private static ExpressionTreeGenerator parser;
    private static final double EPSILON = 1e-9;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        parser = new ExpressionTreeGenerator();
    }

    @Test
    public void roundTripPrimaryColors() {
        assertRoundTrip(new RGBColor(-1, -1, -1));   // black
        assertRoundTrip(new RGBColor(1, 1, 1));      // white
        assertRoundTrip(new RGBColor(1, -1, -1));    // red
        assertRoundTrip(new RGBColor(-1, 1, -1));    // green
        assertRoundTrip(new RGBColor(-1, -1, 1));    // blue
        assertRoundTrip(new RGBColor(0.2, 0.2, 0.2)); // grey
    }

    private void assertRoundTrip(RGBColor original) {
        RgbToYCrCb toYCrCb = new RgbToYCrCb(original);
        YCrCbToRGB backToRgb = new YCrCbToRGB(toYCrCb);

        RGBColor result = backToRgb.evaluate(0, 0);
        assertEquals(original.getRed(), result.getRed(), EPSILON);
        assertEquals(original.getGreen(), result.getGreen(), EPSILON);
        assertEquals(original.getBlue(), result.getBlue(), EPSILON);
        assertTrue(result.getRed() >= -1 - EPSILON && result.getRed() <= 1 + EPSILON, "Red should remain in range");
        assertTrue(result.getGreen() >= -1 - EPSILON && result.getGreen() <= 1 + EPSILON, "Green should remain in range");
        assertTrue(result.getBlue() >= -1 - EPSILON && result.getBlue() <= 1 + EPSILON, "Blue should remain in range");
    }
}
