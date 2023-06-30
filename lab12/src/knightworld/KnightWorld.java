package knightworld;

import com.google.common.truth.DoubleSubject;
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
    private final int size;
    private static final TETile CHESS_FLOOR = new TETile('▢', Color.BLACK, Color.GRAY,
            "chess floor");

    public KnightWorld(int width, int height, int holeSize) {
        seed = System.currentTimeMillis();
        tiles = new TETile[width][height];
        size = holeSize;
        KnightTiles();
    }

    private void KnightTiles() {
        initialFiveMulFive();
        for (int j = 0; j < 5 * size; j += size) {
            repeatTilesY(j);
        }
        for (int i = 0; i < tiles.length; i += size) {
            repeatTilesX(i);
        }
    }

    private void initialFiveMulFive() {
        int index = randomTETile();
        drawSquare(0, index, Tileset.NOTHING, 5 * size, 5 * size);
        for (int i = 0; i < 5 * size; i += size) {
            for (int j = 0; j < 5 * size; j += size) {
                if (tiles[i][j] != null && tiles[i][j].description().equals("nothing")) {
                    singleHoleSurrounded(i, j, 5 * size, 5 * size);
                }
            }
        }
        for (int i = 0; i < 5 * size; i += 1) {
            for (int j = 0; j < 5 * size; j += 1) {
                if (tiles[i][j] == null) {
                    tiles[i][j] = CHESS_FLOOR;
                }
            }
        }
    }

    private void singleHoleSurrounded(int x, int y, int xLimited, int yLimited) {
        drawSquare(x + size, y + 2 * size, Tileset.NOTHING, xLimited, yLimited);
        drawSquare(x + 2 * size, y - size, Tileset.NOTHING, xLimited, yLimited);
        drawSquare(x - size, y - 2 * size, Tileset.NOTHING, xLimited, yLimited);
        drawSquare(x - 2 * size, y + size, Tileset.NOTHING, xLimited, yLimited);
    }


    private void repeatTilesX(int i) {
        for (int j = 5 * size; j < tiles[0].length; j += size) {
            drawSquare(i, j, tiles[i][j - 5 * size], tiles.length, tiles[0].length);
        }
    }

    private void repeatTilesY(int j) {
        for (int i = 5 * size; i < tiles.length; i += size) {
            drawSquare(i, j, tiles[i - 5 * size][j], tiles.length, tiles[0].length);
        }
    }

    private void drawSquare(int i, int j, TETile tile, int xLimited, int yLimited) {
        if (i < 0 || j < 0 || i > xLimited || j > yLimited) {
            return;
        }
        for (int x = i; x < i + size && x < xLimited; x++) {
            for (int y = j; y < j + size && y < yLimited; y++) {
                tiles[x][y] = tile;
            }
        }
    }

    private int randomTETile() {
        Random r = new Random(seed);
        int ran = r.nextInt(5);
        if (ran == 0) {
            return ran;
        } else {
            return ran * size;
        }
    }

    /**
     * Returns the tiles associated with this KnightWorld.
     */
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
