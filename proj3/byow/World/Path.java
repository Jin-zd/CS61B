package byow.World;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Path {
    private final TETile[][] WORLD;
    private final int WIDTH;
    private final int HEIGHT;
    private final int DIST;


    public Path(TETile[][] world, int dist) {
        this.WORLD = world;
        this.WIDTH = WORLD.length;
        this.HEIGHT = WORLD[0].length;
        this.DIST = dist;
    }

    /**
     * Generate the path in the world
     */
    public void pathGenerator(int seed) {
        int startX = 0;
        int startY = DIST;

        boolean[][] visit = new boolean[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            Arrays.fill(visit[i], false);
        }

        do {
            for (int i = DIST; i < WIDTH; i += 3) {
                if (WORLD[i][startY] == Tileset.FLOOR && !visit[i][startY]) {
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
                if (WORLD[i][j] == Tileset.FLOOR && !visited[i][j]) {
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

        List<Point> adjFloors = adjFloor(currentPoint);
        if (adjFloors.isEmpty()) {
            return;
        }
        for (Point nextPoint : adjFloors) {
            if (!visit[nextPoint.x][nextPoint.y]) {
                connectPoints(currentPoint, nextPoint);
            }

            DFS(nextPoint, visit);
        }
    }

    /**
     * Circular implementation of breadth-first search algorithm
     *
     * @param startPoint the point to start the algorithm
     * @param visit      a matrix that records whether a point has been visited or not
     */
    private void BFS(Point startPoint, boolean[][] visit) {
        Queue<Point> queue = new LinkedList<>();

        visit[startPoint.x][startPoint.y] = true;
        queue.add(startPoint);

        while (!queue.isEmpty()) {
            Point currentPoint = queue.poll();
            List<Point> adjFloors = adjFloor(currentPoint);

            for (Point nextPoint : adjFloors) {
                if (!visit[nextPoint.x][nextPoint.y]) {
                    queue.add(nextPoint);
                    visit[nextPoint.x][nextPoint.y] = true;
                    connectPoints(currentPoint, nextPoint);
                }
            }

        }
    }

    /**
     * Connect two points with FLOOR
     *
     * @param startPoint the start point
     * @param endPoint   the end point
     */
    private void connectPoints(Point startPoint, Point endPoint) {
        int pathX = (startPoint.x + endPoint.x) / 2;
        int pathY = (startPoint.y + endPoint.y) / 2;
        WORLD[pathX][pathY] = Tileset.FLOOR;
        if (pathY == startPoint.y) {
            WORLD[pathX + 1][pathY] = Tileset.FLOOR;
        } else if (pathX == startPoint.x) {
            WORLD[pathX][pathY + 1] = Tileset.FLOOR;
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

        if (WORLD[xMin][y] == Tileset.FLOOR) {
            adjFloors.add(new Point(xMin, y));
        }
        if (WORLD[x][yMax] == Tileset.FLOOR) {
            adjFloors.add(new Point(x, yMax));
        }
        if (WORLD[xMax][y] == Tileset.FLOOR) {
            adjFloors.add(new Point(xMax, y));
        }
        if (WORLD[x][yMin] == Tileset.FLOOR) {
            adjFloors.add(new Point(x, yMin));
        }

        return adjFloors;
    }

    /**
     * Remove the redundant paths
     */
    public void removePath() {
        for (int i = DIST; i < WIDTH - DIST; i++) {
            for (int j = DIST; j < HEIGHT - DIST; j++) {
                if (WORLD[i][j] != Tileset.FLOOR) {
                    continue;
                }
                int count = 0;
                if (WORLD[i - 1][j] == Tileset.NOTHING) {
                    count += 1;
                }
                if (WORLD[i][j - 1] == Tileset.NOTHING) {
                    count += 1;
                }
                if (WORLD[i + 1][j] == Tileset.NOTHING) {
                    count += 1;
                }
                if (WORLD[i][j + 1] == Tileset.NOTHING) {
                    count += 1;
                }
                if (count >= 3) {
                    WORLD[i][j] = Tileset.NOTHING;
                }
            }
        }
    }
}
