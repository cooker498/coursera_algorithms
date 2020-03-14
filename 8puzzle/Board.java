/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;

import java.util.Iterator;

public class Board {

    private int[][] tiles;

    private final int n;

    private int zerocol;

    private int zerorow;


    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (this.tiles[i][j] == 0) {
                    zerocol = j;
                    zerorow = i;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int hammingDistance = 0;
        int correctnum = 0;
        int n2 = n * n;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                correctnum = (correctnum + 1) % n2;
                if (i == n - 1 && j == n - 1) {
                    continue;
                }
                if (tiles[i][j] != correctnum) {
                    hammingDistance++;
                }
            }
        }
        return hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattanDistance = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int findNum = tiles[i][j];
                int correctrow;
                int correctcol;
                if (findNum == 0) {
                    continue;
                }
                else {
                    correctrow = (findNum - 1) / n;
                    correctcol = (findNum - 1) % n;
                }
                manhattanDistance = manhattanDistance + Math.abs(correctrow - i) + Math
                        .abs(correctcol - j);
            }
        }
        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> boardStack = new Stack<>();
        if (zerorow == 0) {
            boardStack.push(new Board(tiles).moveup());
        }
        else if (zerorow == n - 1) {
            boardStack.push(new Board(tiles).movedown());
        }
        else {
            boardStack.push(new Board(tiles).moveup());
            boardStack.push(new Board(tiles).movedown());
        }
        if (zerocol == 0) {
            boardStack.push(new Board(tiles).moveleft());
        }
        else if (zerocol == n - 1) {
            boardStack.push(new Board(tiles).moveright());
        }
        else {
            boardStack.push(new Board(tiles).moveleft());
            boardStack.push(new Board(tiles).moveright());
        }
        return boardStack;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board board = new Board(tiles);
        if (zerorow == 0) {
            int temp = board.tiles[n - 1][0];
            board.tiles[n - 1][0] = board.tiles[n - 1][1];
            board.tiles[n - 1][1] = temp;
        }
        else {
            int temp = board.tiles[0][0];
            board.tiles[0][0] = board.tiles[0][1];
            board.tiles[0][1] = temp;
        }
        return board;
    }

    private Board moveup() {
        tiles[zerorow][zerocol] = tiles[zerorow + 1][zerocol];
        tiles[++zerorow][zerocol] = 0;
        return this;
    }

    private Board movedown() {
        tiles[zerorow][zerocol] = tiles[zerorow - 1][zerocol];
        tiles[--zerorow][zerocol] = 0;
        return this;
    }

    private Board moveleft() {
        tiles[zerorow][zerocol] = tiles[zerorow][zerocol + 1];
        tiles[zerorow][++zerocol] = 0;
        return this;
    }

    private Board moveright() {
        tiles[zerorow][zerocol] = tiles[zerorow][zerocol - 1];
        tiles[zerorow][--zerocol] = 0;
        return this;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = { { 1, 0, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        System.out.println("test construct");
        Board board = new Board(tiles);
        System.out.println(board);
        System.out.println("test hamming");
        System.out.println(board.hamming());
        System.out.println("test manhattan");
        System.out.println(board.manhattan());
        System.out.println("test isGoal");
        System.out.println(board.isGoal());
        System.out.println("test equals");
        System.out.println(board.equals(board));
        System.out.println("test neighbors");
        Iterable<Board> boards = board.neighbors();
        Iterator<Board> iterator = boards.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        System.out.println("test twin");
        System.out.println(board.twin());

    }


}
