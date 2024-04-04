package byow.World;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Path {
    private final TETile[][] world;
    private final int WIDTH;
    private final int HEIGHT;

    private final int DIST;


    public Path(TETile[][] world, int dist) {
        this.world = world;
        this.WIDTH = world.length;
        this.HEIGHT = world[0].length;
        this.DIST = dist;
    }

    /**
     * Generate the path in the world
     */
    public void pathGenerator() {
        int startX = 0;
        int startY = DIST;

        boolean[][] visit = new boolean[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            Arrays.fill(visit[i], false);
        }

        do {
            for (int i = DIST; i < WIDTH; i += 3) {
                if (world[i][startY] == Tileset.FLOOR && !visit[i][startY]) {
                    startX = i;
                    break;
                }
            }

            Point startPoint = new Point(startX, startY);
            DFS(startPoint, visit);

        } while (!isAllVisited(visit));
    }

    /**
     * Determines whether all grid points are visited
     *
     * @param visited the discriminant matrix of whether the lattice points are accessed or not
     * @return whether all grid points are visited
     */
    private boolean isAllVisited(boolean[][] visited) {
        boolean flag = true;
        for (int i = DIST; i < WIDTH; i += 3) {
            for (int j = DIST; j < HEIGHT; j += 3) {
                if (world[i][j] == Tileset.FLOOR && !visited[i][j]) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * Recursive implementation of the depth-first search algorithm
     *
     * @param currentPoint the point which is visited now
     * @param visit        a matrix that records whether a point has been visited or not
     */
    private void DFS(Point currentPoint, boolean[][] visit) {
        if (visit[currentPoint.x][currentPoint.y]) {
            return;
        }
        visit[currentPoint.x][currentPoint.y] = true;

        java.util.List<Point> adjFloors = adjFloor(currentPoint);
        if (adjFloors.isEmpty()) {
            return;
        }
        for (Point nextPoint : adjFloors) {

            if (!visit[nextPoint.x][nextPoint.y]) {
                int pathX = (currentPoint.x + nextPoint.x) / 2;
                int pathY = (currentPoint.y + nextPoint.y) / 2;
                world[pathX][pathY] = Tileset.FLOOR;
                if (pathY == currentPoint.y) {
                    world[pathX + 1][pathY] = Tileset.FLOOR;
                } else if (pathX == currentPoint.x) {
                    world[pathX][pathY + 1] = Tileset.FLOOR;
                }
            }

            DFS(nextPoint, visit);
        }
    }


    /**
     * Find the nearest points around one point
     *
     * @param currentPoint the input point
     * @return the list of the nearest points
     */
    public List<Point> adjFloor(Point currentPoint) {
        List<Point> adjFloors = new ArrayList<>();
        int x = currentPoint.x;
        int y = currentPoint.y;

        int xMin = Math.max(0, x - DIST - 1);
        int xMax = Math.min(WIDTH - 1, x + DIST + 1);
        int yMin = Math.max(0, y - DIST - 1);
        int yMax = Math.min(HEIGHT - 1, y + DIST + 1);

        if (world[xMin][y] == Tileset.FLOOR) {
            adjFloors.add(new Point(xMin, y));
        }
        if (world[x][yMax] == Tileset.FLOOR) {
            adjFloors.add(new Point(x, yMax));
        }
        if (world[xMax][y] == Tileset.FLOOR) {
            adjFloors.add(new Point(xMax, y));
        }
        if (world[x][yMin] == Tileset.FLOOR) {
            adjFloors.add(new Point(x, yMin));
        }

        return adjFloors;
    }
}
