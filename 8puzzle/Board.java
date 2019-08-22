/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;

import java.util.Iterator;

public class Board {

    public int[][] tiles;

    private int n;

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
                    correctrow = n - 1;
                    correctcol = n - 1;
                }
                else {
                    correctrow = findNum / n;
                    correctcol = findNum % n - 1;
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
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
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
            boardStack.push(moveup());
        }
        else if (zerorow == n - 1) {
            boardStack.push(movedown());
        }
        else {
            boardStack.push(moveup());
            boardStack.push(movedown());
        }
        if (zerocol == 0) {
            boardStack.push(moveleft());
        }
        else if (zerocol == n - 1) {
            boardStack.push(moveright());
        }
        else {
            boardStack.push(moveleft());
            boardStack.push(moveright());
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
        Board board = new Board(tiles);
        board.tiles[zerorow][zerocol] = board.tiles[zerorow + 1][zerocol];
        board.tiles[zerorow + 1][zerocol] = 0;
        return board;
    }

    private Board movedown() {
        Board board = new Board(tiles);
        board.tiles[zerorow][zerocol] = board.tiles[zerorow - 1][zerocol];
        board.tiles[zerorow - 1][zerocol] = 0;
        return board;
    }

    private Board moveleft() {
        Board board = new Board(tiles);
        board.tiles[zerorow][zerocol] = board.tiles[zerorow][zerocol + 1];
        board.tiles[zerorow][zerocol + 1] = 0;
        return board;
    }

    private Board moveright() {
        Board board = new Board(tiles);
        board.tiles[zerorow][zerocol] = board.tiles[zerorow][zerocol - 1];
        board.tiles[zerorow][zerocol - 1] = 0;
        return board;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = { { 0, 1, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
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
