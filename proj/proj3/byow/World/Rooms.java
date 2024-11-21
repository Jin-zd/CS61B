package byow.World;

import byow.Core.RandomUtils;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Rooms {
    private record RoomCoordinate(int left, int top, int bottom, int right) {
    }

    private final TETile[][] WORLD;
    private final List<RoomCoordinate> ROOMS = new ArrayList<>();
    private final int WIDTH;
    private final int HEIGHT;
    private final int DIST;

    public Rooms(TETile[][] world, int dist) {
        this.WORLD = world;
        this.WIDTH = WORLD.length;
        this.HEIGHT = WORLD[0].length;
        this.DIST = dist;
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
     * Calculate the room start coordinates
     *
     * @param centerX the abscissa of the randomly selected center point
     * @param centerY the ordinate of a randomly selected center point
     * @return an array of the coordinates of the lower left corner of the room
     */
    private int[] startXY(int centerX, int centerY) {
        for (int i = centerX - 1; i < Math.min(WIDTH - DIST, centerX + DIST); i++) {
            for (int j = centerY - 1; j < Math.min(HEIGHT - DIST, centerY + DIST); j++) {
                if (WORLD[i][j] == Tileset.FLOOR) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    /**
     * Generate the rooms in the world
     *
     * @param seed the random number seed
     */
    public void roomGenerator(int seed) {
        Random r = new Random(seed);
        int roomNum = RandomUtils.uniform(r, 10, 20);
        int roomCount = 0;
        int seedCount = 0;

        while (roomCount != roomNum) {
            int realSeed = seed + seedCount;
            Random realR = new Random(realSeed);
            seedCount += 1;
            int roomWidth = RandomUtils.uniform(realR, 1, 5) * 3;
            int roomHeight = RandomUtils.uniform(realR, 1, 5) * 3;
            int centerX = RandomUtils.uniform(realR, DIST, WIDTH - DIST);
            int centerY = RandomUtils.uniform(realR, DIST, HEIGHT - DIST);

            int[] xy = startXY(centerX, centerY);
            if (xy == null) {
                continue;
            }
            int left = xy[0];
            int bottom = xy[1];
            int top = Math.min(HEIGHT - DIST - 1, bottom + roomHeight);
            int right = Math.min(WIDTH - DIST - 1, left + roomWidth);

            RoomCoordinate newRoom = new RoomCoordinate(left, top, bottom, right);
            boolean flag = true;

            if (roomCount == 0) {
                ROOMS.add(newRoom);
                roomCount += 1;
            } else {
                for (RoomCoordinate room : ROOMS) {
                    if (isOverlap(room, newRoom)) {
                        flag = false;
                        break;
                    }
                }
            }

            if (flag) {
                ROOMS.add(newRoom);
                roomCount += 1;

                for (int m = left; m <= right; m++) {
                    for (int n = bottom; n <= top; n++) {
                        WORLD[m][n] = Tileset.ROOM;
                    }
                }
            }
        }
    }


    /**
     * Connect the room and the floor with the floor
     *
     * @param seed the random number seed
     */
    public void roomConnect(int seed) {
        if (ROOMS.isEmpty()) {
            return;
        }
        for (RoomCoordinate room : ROOMS) {

            Random r = new Random(seed);
            int pathNum = RandomUtils.uniform(r, 1, 3);

            while (pathNum != 0) {
                int select = r.nextInt(seed);
                if (select % 4 == 0) {
                    // Top
                    List<Integer> topPoints = new ArrayList<>();
                    int maxTop = Math.min(room.top + DIST + 1, HEIGHT - 1);
                    for (int i = room.left; i <= room.right; i++) {
                        if (WORLD[i][maxTop] == Tileset.FLOOR) {
                            topPoints.add(i);
                        }
                    }
                    if (!topPoints.isEmpty()) {
                        int connectPoint = closestPoint(topPoints, (room.left + room.right) / 2);
                        WORLD[connectPoint][room.top + DIST - 1] = Tileset.FLOOR;
                        WORLD[connectPoint][room.top + DIST] = Tileset.FLOOR;
                    }
                } else if (select % 4 == 1) {
                    // Left
                    List<Integer> leftPoints = new ArrayList<>();
                    int minLeft = Math.max(DIST + 1, room.left - DIST - 1);
                    for (int i = room.bottom; i <= room.top; i++) {
                        if (WORLD[minLeft][i] == Tileset.FLOOR) {
                            leftPoints.add(i);
                        }
                    }
                    if (!leftPoints.isEmpty()) {
                        int connectPoint = closestPoint(leftPoints, (room.bottom + room.top) / 2);
                        WORLD[room.left - DIST + 1][connectPoint] = Tileset.FLOOR;
                        WORLD[room.left - DIST][connectPoint] = Tileset.FLOOR;
                    }
                } else if (select % 4 == 2) {
                    // Bottom
                    List<Integer> bottomPoints = new ArrayList<>();
                    int minBottom = Math.max(DIST, room.bottom - DIST - 1);
                    for (int i = room.left; i <= room.right; i++) {
                        if (WORLD[i][minBottom] == Tileset.FLOOR) {
                            bottomPoints.add(i);
                        }
                    }
                    if (!bottomPoints.isEmpty()) {
                        int connectPoint = closestPoint(bottomPoints, (room.left + room.right) / 2);
                        WORLD[connectPoint][room.bottom - DIST + 1] = Tileset.FLOOR;
                        WORLD[connectPoint][room.bottom - DIST] = Tileset.FLOOR;
                    }
                } else {
                    // Right
                    List<Integer> rightPoints = new ArrayList<>();
                    int maxRight = Math.min(WIDTH - DIST, room.right + DIST + 1);
                    for (int i = room.bottom; i <= room.top; i++) {
                        if (WORLD[maxRight][i] == Tileset.FLOOR) {
                            rightPoints.add(i);
                        }
                    }
                    if (!rightPoints.isEmpty()) {
                        int connectPoint = closestPoint(rightPoints, (room.bottom + room.top) / 2);
                        WORLD[room.right + DIST - 1][connectPoint] = Tileset.FLOOR;
                        WORLD[room.right + DIST][connectPoint] = Tileset.FLOOR;
                    }
                }

                pathNum -= 1;
            }

            seed += 1;
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

    /**
     * Clean the redundant rooms
     */
    public void cleanRoom() {
        List<RoomCoordinate> removeRooms = new ArrayList<>();
        for (RoomCoordinate room : ROOMS) {
            boolean isClean = true;
            List<Point> roomAroundPoints = roomAround(room);
            for (Point point : roomAroundPoints) {
                if (WORLD[point.x][point.y] == Tileset.FLOOR) {
                    isClean = false;
                    break;
                }
            }
            if (isClean) {
                removeRooms.add(room);
                for (int i = room.left; i <= room.right; i++) {
                    for (int j = room.bottom; j <= room.top; j++) {
                        WORLD[i][j] = Tileset.NOTHING;
                    }
                }
            }
        }
        ROOMS.removeAll(removeRooms);
    }

    /**
     * Find the points around the room
     *
     * @param room the target room
     * @return the points around the target room
     */
    private List<Point> roomAround(RoomCoordinate room) {
        List<Point> points = new ArrayList<>();
        for (int x = room.left; x < room.right; x++) {
            points.add(new Point(x, room.bottom - 1));
            points.add(new Point(x, room.top));
        }
        for (int y = room.bottom; y < room.top; y++) {
            points.add(new Point(room.left - 1, y));
            points.add(new Point(room.right + 1, y));
        }
        return points;
    }


}
