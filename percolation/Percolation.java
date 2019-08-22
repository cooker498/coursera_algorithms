/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int OPENED = 1;
    private static final int BLOCKED = 0;
    private static final int BOTTOM = 2;
    // private static final int FULL = 1;
    // private static final int NOT_FULL = 0;
    private final WeightedQuickUnionUF unionUF;
    private byte[] sitestate;
    private final int n;
    private int opennum = 0;
    private final int virtualtop;
    // private final int virtualbutton;
    // private boolean ispercolatesstate = false;

    public Percolation(int n) {

        if (n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        this.n = n;
        int size = (n + 2) * (n + 2);
        this.unionUF = new WeightedQuickUnionUF(size + 1);
        // this.virtualbutton = size + 1;
        this.virtualtop = size;
        this.sitestate = new byte[size + 1];
        // this.sitestate2 = new int[size];
        for (int i = 0; i <= size; i++) {
            sitestate[i] = BLOCKED;
            // sitestate2[i] = NOT_FULL;
        }


    }            // create n-by-n grid, with all sites blocked

    public void open(int row, int col) {
        checkRange(row, col);
        if (isOpen(row, col)) {
            return;
        }
        opennum = opennum + 1;
        this.sitestate[getIndex(row, col)] = OPENED;
        if (row == 1) {
            this.unionUF.union(this.virtualtop, getIndex(row, col));
        }
        if (row == n) {
            sitestate[unionUF.find(getIndex(row, col))] = (byte) (
                    sitestate[unionUF.find(getIndex(row, col))] | BOTTOM);
        }
        this.unionaround(row, col);

        // if (!this.ispercolatesstate && this.unionUF
        //         .connected(this.virtualtop, this.virtualbutton)) {
        //     this.ispercolatesstate = true;
        //     // this.initsitestate2();
        //
        // }
        // else if (this.ispercolatesstate) {
        //     if (row == 1 || this.unionUF.connected(this.virtualtop, getIndex(row, col))) {
        //         this.sitestate2[getIndex(row, col)] = FULL;
        //     }
        // }
    }
    // open site (row, col) if it is not open already


    public boolean isOpen(int row, int col) {
        checkRange(row, col);
        return isOpenPrivate(row, col);
    }


    // is site (row, col) open?

    public boolean isFull(int row, int col) {
        checkRange(row, col);
        return this.isOpen(row, col) && this.unionUF
                .connected(this.virtualtop, getIndex(row, col));
        // return this.sitestate2[getIndex(row, col)] == FULL;
    }
    // is site (row, col) full?

    public int numberOfOpenSites() {
        return opennum;
    }    // number of open sites

    public boolean percolates() {
        return (this.sitestate[this.unionUF.find(this.virtualtop)] & BOTTOM) == BOTTOM;
    }

    private void checkRange(int row, int col) {
        if (row <= 0 || col <= 0) {
            throw new java.lang.IllegalArgumentException("row or col <= 0");
        }
        if (row > n || col > n) {
            throw new java.lang.IllegalArgumentException("row or col out of range");
        }
    }

    private boolean isOpenPrivate(int row, int col) {
        // System.out.println(this.sitestate[getIndex(row, col)] & OPENED);
        return (this.sitestate[getIndex(row, col)] & OPENED) == OPENED;
    }

    private int getIndex(int row, int col) {
        return row * (n + 2) + col;
    }


    private void unionaround(int row, int col) {
        boolean isUpdateRoot = false;

        if (isRootBottom(row, col)) {
            isUpdateRoot = true;
        }
        if (isOpenPrivate(row - 1, col)) {
            if (isRootBottom(row - 1, col)) {
                isUpdateRoot = true;
            }
            unionUF.union(getIndex(row, col), getIndex(row - 1, col));
        }
        if (isOpenPrivate(row + 1, col)) {
            if (isRootBottom(row + 1, col)) {
                isUpdateRoot = true;
            }
            unionUF.union(getIndex(row, col), getIndex(row + 1, col));
        }
        if (isOpenPrivate(row, col - 1)) {
            if (isRootBottom(row, col - 1)) {
                isUpdateRoot = true;
            }
            unionUF.union(getIndex(row, col), getIndex(row, col - 1));
        }
        if (isOpenPrivate(row, col + 1)) {
            if (isRootBottom(row, col + 1)) {
                isUpdateRoot = true;
            }
            unionUF.union(getIndex(row, col), getIndex(row, col + 1));
        }
        if (isUpdateRoot) {
            sitestate[unionUF.find(getIndex(row, col))] = (byte) (
                    sitestate[unionUF.find(getIndex(row, col))] | BOTTOM);
            // System.out.println(sitestate[unionUF.find(getIndex(row, col))]);
        }

    }

    private boolean isRootBottom(int row, int col) {
        int index = unionUF.find(getIndex(row, col));
        // System.out.println((sitestate[index] & BOTTOM) == BOTTOM);
        return (sitestate[index] & BOTTOM) == BOTTOM;
    }


    public static void main(String[] args) {
        Percolation percolation = new Percolation(4);
        percolation.open(1, 1);
        percolation.open(1, 2);
        percolation.open(2, 2);
        percolation.open(3, 2);
        percolation.open(4, 2);
        System.out.println(percolation.unionUF
                                   .connected(percolation.getIndex(1, 1),
                                              percolation.getIndex(1, 2)));
        System.out.println(percolation.percolates());
    }
}
