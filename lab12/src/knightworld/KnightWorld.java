package knightworld;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.util.Random;

/**
 * Draws a world consisting of knight-move holes.
 */
public class KnightWorld {

    private TETile[][] tiles;
    private final long seed;
    private static final TETile CHESS_FLOOR = new TETile('■', Color.GRAY, Color.BLACK, "Chess floor");
    // TODO: Add additional instance variables here

    public KnightWorld(int width, int height, int holeSize) {
        // TODO: Fill in this constructor and class, adding helper methods and/or classes as necessary to draw the
        //  specified pattern of the given hole size for a window of size width x height. If you're stuck on how to
        //  begin, look at the provided demo code!
        seed = System.currentTimeMillis();
        tiles = new TETile[width][height];
        KnightTiles(holeSize);
    }

    private void KnightTiles(int holeSize) {
        for (int j = 0; j < tiles[0].length; j++) {
                if (j % holeSize == 0) {
                    tiles[0][j] = randomTETile();
                }
                else {
                    tiles[0][j] = tiles[0][j - 1];
                }
            }
        firstFloor(holeSize);
        restPosition(holeSize);
    }

    private void firstFloor(int holeSize) {
        for (int i = 1; i < holeSize; i++) {
            for (int j = 0; j < tiles[0].length; i++) {
                tiles[i][j] = tiles[i - 1][j];
            }
        }
    }

    private void restPosition (int holeSize) {

    }

    private TETile randomTETile() {
        Random r = new Random(seed);
        int ran = r.nextInt(2);
        return switch (ran) {
            case 0 -> CHESS_FLOOR;
            case 1 -> Tileset.NOTHING;
            default -> throw new IllegalStateException("Unexpected value: " + ran);
        };
    }

    /** Returns the tiles associated with this KnightWorld. */
    public TETile[][] getTiles() {
        return tiles;
    }

    public static void main(String[] args) {
        // Change these parameters as necessary
        int width = 50;
        int height = 30;
        int holeSize = 2;

        KnightWorld knightWorld = new KnightWorld(width, height, holeSize);

        TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        ter.renderFrame(knightWorld.getTiles());

    }
}
