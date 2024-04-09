package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.World.World;
import edu.princeton.cs.algs4.In;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import ucb.test.TimeLimitedTests;

public class Engine {
    static TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww".) The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, running both of these:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        if (input.charAt(0) != 'N' && input.charAt(0) != 'n') {
            return null;
        }
        if (input.charAt(input.length() - 1) != 'S' && input.charAt(input.length() - 1) != 's') {
            return null;
        }
        int seed = Integer.parseInt(input.substring(1, input.length() - 1));

        int dist = 2;

        World world = new World(WIDTH, HEIGHT, dist, seed);
        world.worldGenerator();

        return world.getWorld();
    }

    public static void main(String[] args) {
        String inputString = "N58621S";

        Engine engineTest = new Engine();
        TETile[][] world = engineTest.interactWithInputString(inputString);

        ter.initialize(world.length, world[0].length);
        ter.renderFrame(world);
    }
}

