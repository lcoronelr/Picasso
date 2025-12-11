[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/n4JhxjZ1)
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

### Mandelbrot
The iterate fractals extension is used by inputting `Mandelbrot(x, y)` into the GUI. To be able to view the Mandelbrot fractals in their entirety, ensure proper zoom and iteration settings.

### Animator
- Type an expression that includes t in the input field
Click "Evaluate"
- The expression will automatically animate, with t varying from 0 to 1

The **Animator** extension lets you animate any mathematical expression that uses the variable `t`.

**How to use:**
1. Enter an expression that includes `t` in the input field.  
   *Example:* `sin(t) * x`
2. Click **Evaluate** or simply press **Enter**.
3. The expression will animate automatically for 50 frames, with `t` varying from `0` to `1`.

### Random Expressions

The Random Expressions extension lets you automatically generate and display a random Picasso expression.

**How to use**:

1. Click the **GenerateRandomExpression** button.
2. A random expression will appear in the input field.
3. The expression is evaluated and drawn automatically.  
   You can edit the expression if you want and click **Evaluate** to see the updated image.

### Iterate Fractals
The **Mandelbrot** extension lets you create the Mandelbrot fractals
with a given x and y.

**How to use:**
1. Enter an expression with two parameters - ideally x and y coordinates.
   *Example 1:* `mandelbrot(x, y)` - generates classic Mandelbrot set,
   *Example 2:* `mandelbrot(x+x, y+y)` - creates a scaled version of fractals,
   *Example 3:* `mandelbrot(x*0.5, y*0.5)` - zooms into the fractal
2. Click **Evaluate** or simply press **Enter**.
3. The expression will generate detailed fractal patterns particularly
at the boundaries.
