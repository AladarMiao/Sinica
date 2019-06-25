package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int top;
    private final int bottom;
    private boolean[][] twoDArr;
    private int gridSize;
    private int numOpen;
    private WeightedQuickUnionUF helper;
    private WeightedQuickUnionUF helper2;

    /**
     * Construction Method, declare two WQUUF, and two virtual node
     *
     * @param N the scale of the input
     * @throws java.lang.IllegalArgumentException if N < 0
     */
    public Percolation(int N) {               // create N-by-N twoDArr, with all sites blocked
        if (N <= 0) {
            throw new IllegalArgumentException("The input N is illegal!");
        }
        twoDArr = new boolean[N][N];
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                this.twoDArr[x][y] = false;
            }
        }

        this.gridSize = N;
        this.top = N * N + 1;
        this.bottom = N * N + 2;
        this.helper = new WeightedQuickUnionUF(N * N + 4);
        this.helper2 = new WeightedQuickUnionUF(N * N + 4);
    }

    public static void main(String[] args) {  // test client (optional)

    }

    public int numberOfOpenSites() {
        return numOpen;
    }

    private void bottomLeftAndRight(int row, int col) {
        validateArray(row, col);
        if (row == (gridSize - 1) && col == 0) {

            if (isOpen(row - 1, col)) {
                helper.union(twoDToOneD(row - 1, col), twoDToOneD(row, col));
                helper2.union(twoDToOneD(row - 1, col), twoDToOneD(row, col));
            }
            if (isOpen(row, col + 1)) {
                helper.union(twoDToOneD(row, col + 1), twoDToOneD(row, col));
                helper2.union(twoDToOneD(row, col + 1), twoDToOneD(row, col));
            }

        } else if (row == (gridSize - 1) && col == (gridSize - 1)) { //bottom right
            if (isOpen(row - 1, col)) {
                helper.union(twoDToOneD(row - 1, col), twoDToOneD(row, col));
                helper2.union(twoDToOneD(row - 1, col), twoDToOneD(row, col));
            }
            if (isOpen(row, col - 1)) {
                helper.union(twoDToOneD(row, col - 1), twoDToOneD(row, col));
                helper2.union(twoDToOneD(row - 1, col), twoDToOneD(row, col));
            }

        }

    }

    public void open(int i, int j) {
        validateArray(i, j);
        twoDArr[i][j] = true;
        numOpen++;

        if (gridSize == 1) {
            helper.union(twoDToOneD(i, j), bottom);
            helper2.union(twoDToOneD(i, j), bottom);
            helper.union(twoDToOneD(i, j), top);
            helper2.union(twoDToOneD(i, j), top);
        } else {
            if (i == 0) {
                helper.union(top, twoDToOneD(i, j));
                helper2.union(top, twoDToOneD(i, j));

            }

            if (!percolates()) {
                if (i == gridSize - 1) {
                    helper.union(twoDToOneD(i, j), bottom);
                }
            }
            bottomLeftAndRight(i, j);

            if (i > 0 && isOpen(i - 1, j)) {
                helper.union(twoDToOneD(i, j), twoDToOneD(i - 1, j));
                helper2.union(twoDToOneD(i, j), twoDToOneD(i - 1, j));
            }

            if (i < gridSize - 1 && isOpen(i + 1, j)) {
                helper.union(twoDToOneD(i, j), twoDToOneD(i + 1, j));
                helper2.union(twoDToOneD(i, j), twoDToOneD(i + 1, j));
            }

            if (j > 0 && isOpen(i, j - 1)) {
                helper.union(twoDToOneD(i, j), twoDToOneD(i, j - 1));
                helper2.union(twoDToOneD(i, j), twoDToOneD(i, j - 1));
            }

            if (j < gridSize - 1 && isOpen(i, j + 1)) {
                helper.union(twoDToOneD(i, j), twoDToOneD(i, j + 1));
                helper2.union(twoDToOneD(i, j), twoDToOneD(i, j + 1));

            }
        }

    }

    public boolean isOpen(int i, int j) {    // is site (row i, column j) open?
        validateArray(i, j);
        return twoDArr[i][j];
    }

    public boolean isFull(int i, int j) {    // is site (row i, column j) full?
        validateArray(i, j);
        if (helper2.connected(twoDToOneD(i, j), top) && isOpen(i, j)) {
            return true;
        }
        return false;
    }

    public boolean percolates() {            // does the system percolate?
        if (helper2.connected(top, bottom) || helper.connected(top, bottom)) {
            return true;
        }
        return false;
    }


    private int twoDToOneD(int row, int col) {
        return row * gridSize + col;
    }

    private void validateArray(int i, int j) {
        if (i < 0 || (i > gridSize - 1) || j < 0 || (j > gridSize - 1)) {
            throw new IndexOutOfBoundsException("must be between 0 and N-1");
        }
    }

}
