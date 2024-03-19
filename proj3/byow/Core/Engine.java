package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Engine {
    static TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;

    private static final int dist = 2;

    private class RoomCoordinate {
        public int left;
        public int up;
        public int bottom;
        public int right;

        public RoomCoordinate(int left, int up, int bottom, int right) {
            this.left = left;
            this.up = up;
            this.bottom = bottom;
            this.right = right;
        }
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
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
        if (input.charAt(0) != 'N' || input.charAt(0) != 'n') {
            return null;
        }
        if (input.charAt(input.length() - 1) != 'S' || input.charAt(input.length() - 1) != 's') {
            return null;
        }
        int seed = Integer.parseInt(input.substring(1, input.length() - 1));

        return worldGenerator(seed);
    }

    /**
     * Pseudo-random number generation function
     * @param seed the random number seed
     * @param lo the min value of the random number
     * @param hi the max value of the random number
     * @return the random number between lo and hi
     */
    private int randomNums(int seed, int lo, int hi) {
        Random r = new Random(seed);
        return r.nextInt(lo, hi);
    }

    /**
     * the World Generator
     * @param seed the random number seed
     * @return an 2-D array of the world's composition
     */
    private TETile[][] worldGenerator(int seed) {
        TETile[][] worldFrame = new TETile[WIDTH][HEIGHT];
        initialWorld(worldFrame);

        int roomNum = randomNums(seed, 10, 20);
        List<RoomCoordinate> rooms = new ArrayList<>();
        int roomCount = 0;
        int seedCount = 0;

        while (roomCount != roomNum) {
            int realSeed = seed + seedCount;
            seedCount += 1;
            int roomWidth = randomNums(realSeed, 0, 4) * 3 + 1;
            int roomHeight = randomNums(realSeed, 1, 4) * 3 + 1;
            int centerX = randomNums(realSeed, dist, WIDTH);
            int centerY = randomNums(realSeed, dist, HEIGHT);

            int[] xy = startXY(centerX, centerY, worldFrame);
            int left = xy[0];
            int bottom = xy[1];
            int up = Math.min(HEIGHT - 2, bottom + roomHeight);
            int right = Math.min(WIDTH - 2, left + roomWidth);

            RoomCoordinate newRoom = new RoomCoordinate(left, up, bottom, right);
            boolean flag = true;

            if (roomCount == 0) {
                rooms.add(newRoom);
                roomCount += 1;
            } else {
                for (RoomCoordinate room : rooms) {
                    if (isOverlap(room, newRoom)) {
                        flag = false;
                        break;
                    }
                }
            }

            if (flag) {
                rooms.add(newRoom);
                roomCount += 1;

                for (int m = left; m < right; m++) {
                    for (int n = bottom; n < up; n++) {
                        worldFrame[m][n] = Tileset.ROOM;
                    }
                }
            }
        }

        return worldFrame;
    }

    /**
     * Initial the world array
     * @param worldFrame the 2-D array of the world's composition
     */
    private void initialWorld(TETile[][] worldFrame) {
        for (int i = dist; i < WIDTH; i += 3) {
            for (int j = dist; j < HEIGHT; j += 3) {
                worldFrame[i][j] = Tileset.FLOOR;
            }
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (worldFrame[i][j] == null) {
                    worldFrame[i][j] = Tileset.NOTHING;
                }
            }
        }
    }

    /**
     * Calculate the room start coordinates
     * @param centerX the abscissa of the randomly selected center point
     * @param centerY the ordinate of a randomly selected center point
     * @param worldFrame the 2-D array of the world's composition
     * @return an array of the coordinates of the lower left corner of the room
     */
    private int[] startXY(int centerX, int centerY, TETile[][] worldFrame) {
        for (int i = centerX - 1; i < Math.min(WIDTH - 2, centerX + 2); i++) {
            for (int j = centerY - 1; j < Math.min(HEIGHT - 2, centerY + 2); j++) {
                if (worldFrame[i][j] == Tileset.FLOOR) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{centerX, centerY};
    }

    /**
     * Determine if two rooms overlap
     * @param room one room's coordinate
     * @param newRoom another room's coordinate
     * @return whether the two rooms overlap
     */
    private boolean isOverlap(RoomCoordinate room, RoomCoordinate newRoom) {
        return !(room.left > newRoom.right ||
                room.bottom > newRoom.up ||
                room.right < newRoom.left ||
                room.up < newRoom.bottom);
    }


    public static void main(String[] args) {
        int seed = 45123;

        Engine engineTest = new Engine();
        TETile[][] world = engineTest.worldGenerator(seed);

        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);
    }
}

