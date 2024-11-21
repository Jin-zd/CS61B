package byow.World;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class World {
    private final TETile[][] WORLD;
    private final Rooms ROOMS;
    private final Path PATH;
    private final int WIDTH;
    private final int HEIGHT;
    private final int DIST;
    private final int SEED;

    public World(int width, int height, int dist, int seed) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.DIST = dist;
        this.SEED = seed;
        this.WORLD = new TETile[WIDTH][HEIGHT];
        this.ROOMS = new Rooms(WORLD, DIST);
        this.PATH = new Path(WORLD, DIST);
    }

    /**
     * The World Generator
     */
    public void worldGenerator() {
        initialWorld();
        worldRoomsGenerator();
        worldPathGenerator();
        worldRoomConnect();
        worldClean();
    }

    /**
     * Initial the world array
     */
    private void initialWorld() {
        for (int i = DIST; i < WIDTH; i += 3) {
            for (int j = DIST; j < HEIGHT; j += 3) {
                WORLD[i][j] = Tileset.FLOOR;
            }
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (WORLD[i][j] == null) {
                    WORLD[i][j] = Tileset.NOTHING;
                }
            }
        }
    }

    /**
     * Generate the rooms in the world
     */
    public void worldRoomsGenerator() {
        ROOMS.roomGenerator(SEED);
    }

    /**
     * Generate the paths in the world
     */
    public void worldPathGenerator() {
        PATH.pathGenerator(SEED);
    }

    /**
     * Connect the rooms with the floor
     */
    public void worldRoomConnect() {
        ROOMS.roomConnect(SEED);
    }

    /**
     * Clean the redundant rooms and paths
     */
    public void worldClean() {
        ROOMS.cleanRoom();
        PATH.removePath();
    }

    /**
     * Get the world array
     *
     * @return the world array
     */
    public TETile[][] getWorld() {
        return WORLD;
    }
}
