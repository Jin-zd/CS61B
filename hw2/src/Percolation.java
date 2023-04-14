import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int[][] grid;
    private final WeightedQuickUnionUF connectSet;
    private int openCount;
    private final int gridLength;

    public Percolation(int N) {
        if (N < 0) {
            throw new IllegalArgumentException();
        }
        connectSet = new WeightedQuickUnionUF(N * N);
        grid = new int[N+1][N+1];
        openCount = 0;
        gridLength = N;
    }

    private int correspondedIndex(int row, int col) {
        return row * gridLength + col;
    }

    private void connectItem(int row, int col, int currIndex) {
        if (row < 0 || row >= gridLength || col < 0 || col >= gridLength) {
            return;
        }
        int index = correspondedIndex(row, col);
        connectSet.union(currIndex, index);
    }

    public void open(int row, int col) {
        if (row < 0 || row >= gridLength || col < 0 || col >= gridLength) {
            throw new IndexOutOfBoundsException();
        }
        grid[row][col] = 1;
        openCount += 1;
        int currIndex = correspondedIndex(row, col);
        connectItem(row - 1, col, currIndex);
        connectItem(row + 1, col, currIndex);
        connectItem(row, col - 1, currIndex);
        connectItem(row, col + 1, currIndex);
    }

    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= gridLength || col < 0 || col >= gridLength) {
            throw new IndexOutOfBoundsException();
        }
        return grid[row][col] == 1;
    }

    public boolean isFull(int row, int col) {
        if (row < 0 || row >= gridLength || col < 0 || col >= gridLength) {
            throw new IndexOutOfBoundsException();
        }
        if (!isOpen(row, col)) {
            return false;
        }
        int currIndex = correspondedIndex(row, col);
        for (int i = 0; i < gridLength; i++) {
            if (connectSet.connected(currIndex, i)) {
                return true;
            }
        }
        return false;
    }

    public int numberOfOpenSites() {
        return openCount;
    }

    public boolean percolates() {
        for (int i = 0; i < gridLength; i++) {
            if (isFull(gridLength, i)) {
                return true;
            }
        }
        return false;
    }
}
