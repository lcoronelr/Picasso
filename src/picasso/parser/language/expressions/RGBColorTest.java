/**
 * 
 */
package picasso.parser.language.expressions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author Sara Sprenkle
 */
class RGBColorTest {

	private RGBColor white;
	private RGBColor closeToWhite;

	private RGBColor orange;
	private RGBColor closeToOrange;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		white = new RGBColor(1, 1, 1);
		closeToWhite = new RGBColor(.999, 1.0001, 1);
		orange = new RGBColor(1, 0, -1);
		closeToOrange = new RGBColor(1.001, -.00001, -.9999);
	}

	@Test
	public void testEquals() {
		// test identity
		assertTrue(white.equals(closeToWhite));
		assertTrue(closeToWhite.equals(closeToWhite));

		// test close
		assertEquals(white, closeToWhite);
		assertEquals(closeToWhite, white);

		assertEquals(orange, closeToOrange);
		assertTrue(closeToOrange.equals(orange));

		// test not equal
		assertNotEquals(orange, white);
		assertNotEquals(closeToWhite, orange);
	}

	@Test
	public void testToString() {
		assertEquals(white.toString(), "Color: 1.0 1.0 1.0");
		assertEquals(closeToWhite.toString(), "Color: 0.999 1.0001 1.0");
		assertEquals(orange.toString(), "Color: 1.0 0.0 -1.0");
		assertEquals(closeToOrange.toString(), "Color: 1.001 -1.0E-5 -0.9999");
	}

}
