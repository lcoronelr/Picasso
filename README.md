
# Picasso

Authors: 
- Luis Coronel
- Menilik Deneke
- Therese Elvira Mombou Gatsing
- Abhishek Pradhan
- Asya Yurkovskaya

An application that allows the user to create expressions that
evaluate to colors and then eventually to images.

The given code base is a good start, but it is sparsely documented
(document each method/part after you understand what it's doing), and,
as your application grows, you may need to refactor.

See the specification for Picasso on the course web site.

## Running Picasso

To run Picasso, run `picasso.Main`

## Project Organization

`src` - the source code for the project

`conf` - the configuration files for the project

`images` - contains some sample images generated from Picasso. Some of the expressions for these images can be found in the `expressions` directory.

## Code Base History

This code base originated as a project in a course at Duke University.  The professors realized that the code could be designed better and refactored.  This code base has some code leftover from the original version.

## Extensions

### Iterate Fractals (Mandelbrot)
The **Mandelbrot** extension lets you create the Mandelbrot fractals
with a given x and y

**How to use:**
1. Enter an expression with two parameters - ideally x and y coordinates

   *Example 1:* `mandelbrot(x, y)` - generates classic Mandelbrot set
   
   *Example 2:* `mandelbrot(x+x, y+y)` - zooms out to display full fractal 	pattern by scaling coordinates
   
   *Example 3:* `mandelbrot(x*0.5, y*0.5)` - zooms into the fractal
2. Click **Evaluate** or simply press **Enter**
3. The expression will generate detailed fractal patterns particularly
at the boundaries

### Animator

The **Animator** extension lets you animate any mathematical expression that uses the variable `t`

**How to use:**
1. Enter an expression that includes `t` in the input field 
   *Example:* `sin(t) * x`
2. Click **Evaluate** or simply press **Enter**
3. The expression will animate automatically for 50 frames, with `t` varying from `0` to `1`

### Random Expressions

The Random Expressions extension lets you automatically generate and display a random Picasso expression

**How to use**:

1. Click the **Generate Random Expression** button
2. A random expression will appear in the input field
3. The expression is evaluated, drawn, and saved to history automatically
4. You can edit the expression if you want and click **Evaluate** to see the updated image

*Note: Random expressions omit animation due to complexity, but you can manually add `t` later if you want animation*
   
### History
The **History** keeps tracks all of the exprresions that have been evaluated

**How to use**:

1. Whenever you evaluate an expression or generate a random one, it will appear in the History panel on the side
2. You can clear the saved expressions to reset the history
3. To reuse a past expression, either double-click it or select it and click **Use Selected**