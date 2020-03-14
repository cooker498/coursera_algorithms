/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;

public class Solver {

    private SearchNode lastSearchNode;

    private boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        SearchNode initSearchNode = new SearchNode(initial, 0, null);
        MinPQ<SearchNode> searchNodeMinPQ = new MinPQ<>(new ManhattanComparator());
        searchNodeMinPQ.insert(initSearchNode);
        SearchNode minNode = searchNodeMinPQ.delMin();
        lastSearchNode = minNode;

        MinPQ<SearchNode> searchNodeMinPQ2 = new MinPQ<>(new ManhattanComparator());
        searchNodeMinPQ2.insert(initSearchNode.twin());
        SearchNode minNode2 = searchNodeMinPQ2.delMin();
        while (!minNode.isGoal() && !minNode2.isGoal()) {
            Iterator<SearchNode> searchNodes = minNode.neighbors().iterator();
            while (searchNodes.hasNext()) {
                searchNodeMinPQ.insert(searchNodes.next());
            }
            minNode = searchNodeMinPQ.delMin();
            lastSearchNode = minNode;

            Iterator<SearchNode> searchNodes2 = minNode2.neighbors().iterator();
            while (searchNodes2.hasNext()) {
                searchNodeMinPQ2.insert(searchNodes2.next());
            }
            minNode2 = searchNodeMinPQ2.delMin();
        }
        if (minNode.isGoal()) {
            solvable = true;
        }
        else {
            solvable = false;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return solvable ? lastSearchNode.movenum : -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!solvable) {
            return null;
        }
        Stack<Board> boardStack = new Stack<>();
        boardStack.push(lastSearchNode.board);
        SearchNode temp = lastSearchNode;
        while (temp.previous != null) {
            temp = temp.previous;
            boardStack.push(temp.board);
        }
        return boardStack;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private class SearchNode {
        private final Board board;
        private final int movenum;
        private final SearchNode previous;
        private final int hammingDistance;
        private final int manhattanDistance;

        public SearchNode(Board board, int movenum, SearchNode previous) {
            this.board = board;
            this.movenum = movenum;
            this.previous = previous;
            this.hammingDistance = board.hamming();
            this.manhattanDistance = board.manhattan();
        }

        private boolean isGoal() {
            return this.board.isGoal();
        }

        private Iterable<SearchNode> neighbors() {
            Iterator<Board> neighborsiterator = board.neighbors().iterator();
            Stack<SearchNode> stack = new Stack<>();
            while (neighborsiterator.hasNext()) {
                Board neighbor = neighborsiterator.next();
                if (previous == null || !previous.board.equals(neighbor)) {
                    stack.push(new SearchNode(neighbor, movenum + 1, this));
                }
            }
            return stack;
        }

        private SearchNode twin() {
            return new SearchNode(board.twin(), movenum, previous);
        }
    }

    private class ManhattanComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode a, SearchNode b) {
            return a.manhattanDistance + a.movenum - b.manhattanDistance - b.movenum;
        }
    }


}
