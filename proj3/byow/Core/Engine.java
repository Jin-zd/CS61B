package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.In;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import ucb.test.TimeLimitedTests;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

public class Engine {
    static TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;

    private static final int DIST = 2;
    
    private TETile[][] world;

    private static class RoomCoordinate {
        public int left;
        public int top;
        public int bottom;
        public int right;

        public RoomCoordinate(int left, int top, int bottom, int right) {
            this.left = left;
            this.top = top;
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
        
        worldGenerator(seed);

        return world;
    }

    /**
     * Pseudo-random number generation function
     *
     * @param seed the random number seed
     * @param lo   the min value of the random number
     * @param hi   the max value of the random number
     * @return the random number between lo and hi
     */
    private int randomNums(int seed, int lo, int hi) {
        Random r = new Random(seed);
        return r.nextInt(lo, hi);
    }

    /**
     * the World Generator
     *
     * @param seed the random number seed
     */
    private void worldGenerator(int seed) {
        world = new TETile[WIDTH][HEIGHT];

        initialWorld();
        List<RoomCoordinate> rooms = roomGenerator(seed);
        pathGenerator();
        roomConnect(rooms, seed);

    }

    /**
     * Initial the world array
     *
     */
    private void initialWorld() {
        for (int i = DIST; i < WIDTH; i += 3) {
            for (int j = DIST; j < HEIGHT; j += 3) {
                world[i][j] = Tileset.FLOOR;
            }
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (world[i][j] == null) {
                    world[i][j] = Tileset.NOTHING;
                }
            }
        }
    }

    /**
     * Generate the rooms in the world
     *
     * @param seed       the random number seed
     * @return the list of all rooms which do not overlap each other
     */
    private List<RoomCoordinate> roomGenerator(int seed) {
        int roomNum = randomNums(seed, 10, 20);
        List<RoomCoordinate> rooms = new ArrayList<>();
        int roomCount = 0;
        int seedCount = 0;

        while (roomCount != roomNum) {
            int realSeed = seed + seedCount;
            seedCount += 1;
            int roomWidth = randomNums(realSeed, 1, 5) * 3;
            int roomHeight = randomNums(realSeed, 1, 5) * 3;
            int centerX = randomNums(realSeed, DIST, WIDTH - 2);
            int centerY = randomNums(realSeed, DIST, HEIGHT - 2);

            int[] xy = startXY(centerX, centerY);
            if (xy == null) {
                continue;
            }
            int left = xy[0];
            int bottom = xy[1];
            int top = Math.min(HEIGHT - 3, bottom + roomHeight);
            int right = Math.min(WIDTH - 3, left + roomWidth);

            RoomCoordinate newRoom = new RoomCoordinate(left, top, bottom, right);
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

                for (int m = left; m <= right; m++) {
                    for (int n = bottom; n <= top; n++) {
                        world[m][n] = Tileset.ROOM;
                    }
                }
            }
        }
        return rooms;
    }


    /**
     * Calculate the room start coordinates
     *
     * @param centerX    the abscissa of the randomly selected center point
     * @param centerY    the ordinate of a randomly selected center point
     * @return an array of the coordinates of the lower left corner of the room
     */
    private int[] startXY(int centerX, int centerY) {
        for (int i = centerX - 1; i < Math.min(WIDTH - 2, centerX + 2); i++) {
            for (int j = centerY - 1; j < Math.min(HEIGHT - 2, centerY + 2); j++) {
                if (world[i][j] == Tileset.FLOOR) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    /**
     * Determine if two rooms overlap
     *
     * @param room    one room's coordinate
     * @param newRoom another room's coordinate
     * @return whether the two rooms overlap
     */
    private boolean isOverlap(RoomCoordinate room, RoomCoordinate newRoom) {
        return !(room.left > newRoom.right ||
                room.bottom > newRoom.top ||
                room.right < newRoom.left ||
                room.top < newRoom.bottom);
    }

    /**
     * Generate the path in the world
     *
     */
    private void pathGenerator() {
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
     * @param visited    the discriminant matrix of whether the lattice points are accessed or not
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

        List<Point> adjFloors = adjFloor(currentPoint);
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
    private List<Point> adjFloor(Point currentPoint) {
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

    /**
     * Connect the room and the floor with the floor
     *
     * @param rooms      the rooms in the world
     */
    private void roomConnect(List<RoomCoordinate> rooms, int seed) {
        for (RoomCoordinate room : rooms) {

            int pathNum = randomNums(seed, 1, 5);
            seed += 1;
            // Top
            List<Integer> topPoints = new ArrayList<>();
            int maxTop = Math.min(room.top + 3, HEIGHT - 1);
            for (int i = room.left; i <= room.right; i++) {
                if (world[i][maxTop] == Tileset.FLOOR) {
                    topPoints.add(i);
                }
            }
            if (!topPoints.isEmpty()) {
                int connectPoint = closestPoint(topPoints, (room.left + room.right) / 2);
                world[connectPoint][room.top + 1] = Tileset.FLOOR;
                world[connectPoint][room.top + 2] = Tileset.FLOOR;
            }

            // Left
            List<Integer> leftPoints = new ArrayList<>();
            int minLeft = Math.max(DIST + 1, room.left - 3);
            for (int i = room.bottom; i <= room.top; i++) {
                if (world[minLeft][i] == Tileset.FLOOR) {
                    leftPoints.add(i);
                }
            }
            if (!leftPoints.isEmpty()) {
                int connectPoint = closestPoint(leftPoints, (room.bottom + room.top) / 2);
                world[room.left - 1][connectPoint] = Tileset.FLOOR;
                world[room.left - 2][connectPoint] = Tileset.FLOOR;
            }

            // Bottom
            List<Integer> bottomPoints = new ArrayList<>();
            int minBottom = Math.max(DIST + 1, room.bottom - 3);
            for (int i = room.left; i <= room.right; i++) {
                if (world[i][minBottom] == Tileset.FLOOR) {
                    bottomPoints.add(i);
                }
            }
            if (!bottomPoints.isEmpty()){
                int connectPoint = closestPoint(bottomPoints, (room.left + room.right) / 2);
                world[connectPoint][room.bottom - 1] = Tileset.FLOOR;
                world[connectPoint][room.bottom - 2] = Tileset.FLOOR;
            }

            // Right
            List<Integer> rightPoints = new ArrayList<>();
            int maxRight = Math.min(WIDTH - 3, room.right + 3);
            for (int i = room.bottom; i <= room.top; i++) {
                if (world[maxRight][i] == Tileset.FLOOR) {
                    rightPoints.add(i);
                }
            }
            if (!rightPoints.isEmpty()) {
                int connectPoint = closestPoint(rightPoints, (room.bottom + room.top) / 2);
                world[room.right + 1][connectPoint] = Tileset.FLOOR;
                world[room.right + 2][connectPoint] = Tileset.FLOOR;
            }
        }
    }


    /**
     * Find the point closest to a given point
     *
     * @param points the list of all the points
     * @param mid    the given point
     * @return the index of the closest point
     */
    private int closestPoint(List<Integer> points, int mid) {
        List<Integer> rightPoint = points.stream().filter(x -> x >= mid).map(x -> x - mid).toList();
        List<Integer> leftPoint = points.stream().filter(x -> x < mid).map(x -> mid - x).toList();

        int minRight;
        int minLeft;
        if (rightPoint.isEmpty()) {
            minRight = WIDTH;
        } else {
            minRight = Collections.min(rightPoint);
        }
        if (leftPoint.isEmpty()) {
            minLeft = WIDTH;
        } else {
            minLeft = Collections.min(leftPoint);
        }


        if (minRight <= minLeft) {
            return minRight + mid;
        } else {
            return mid - minLeft;
        }
    }


    public static void main(String[] args) {
        String inputString = "N45123S";

        Engine engineTest = new Engine();
        TETile[][] world = engineTest.interactWithInputString(inputString);

        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);
    }
}

