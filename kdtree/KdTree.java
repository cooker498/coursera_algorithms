import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;

public class KdTree {

    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int TOP = 2;
    private static final int BOTTOM = 3;
    private static final int EMPTY = 4;
    private static final int VERTICAL = 0;
    private static final int HORIZONTAL = 1;


    private static class Node {
        private final Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private int splits;     //vertical or horizontal;

        public Node(Node parent, int orientation, Point2D p) {
            this.p = p;
            switch (orientation) {
                case EMPTY:
                    this.rect = new RectHV(0, 0, 1, 1);
                    this.splits = VERTICAL;
                    break;
                case LEFT:
                    this.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.p.x(),
                                           parent.rect.ymax());
                    this.splits = HORIZONTAL;
                    break;
                case RIGHT:
                    this.rect = new RectHV(parent.p.x(), parent.rect.ymin(), parent.rect.xmax(),
                                           parent.rect.ymax());
                    this.splits = HORIZONTAL;
                    break;
                case BOTTOM:
                    this.rect = new RectHV(parent.rect.xmin(),
                                           parent.rect.ymin(), parent.rect.xmax(),
                                           parent.p.y());
                    this.splits = VERTICAL;
                    break;
                case TOP:
                    this.rect = new RectHV(parent.rect.xmin(), parent.p.y(), parent.rect.xmax(),
                                           parent.rect.ymax());
                    this.splits = VERTICAL;
                    break;
            }

        }
    }

    private int nodesseize;

    private Node root;

    public KdTree()                               // construct an empty set of points
    {
        root = null;
        nodesseize = 0;
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return root == null;
    }

    public int size()                         // number of points in the set
    {
        return nodesseize;
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (contains(p)) {
            return;
        }
        nodesseize++;
        root = inertLeftOrRight(null, EMPTY, root, p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return containLeftOrRight(root, p);
    }

    public void draw()                         // draw all points to standard draw
    {
        draw(root);
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        LinkedList<Point2D> ls = new LinkedList<Point2D>();
        range(ls, root, rect);
        return ls;
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            return null;
        }
        return nearest(root, root.p, p);
    }

    private Point2D nearest(Node node, Point2D nearestPoint, Point2D p) {
        if (node == null) {
            return nearestPoint;
        }
        if (node.rect.distanceSquaredTo(p) > nearestPoint.distanceSquaredTo(p)) {
            return nearestPoint;
        }
        if (p.distanceSquaredTo(node.p) < p.distanceSquaredTo(nearestPoint)) {
            nearestPoint = node.p;
        }
        if (node.splits == VERTICAL && p.x() >= node.p.x()
                || node.splits == HORIZONTAL && p.y() >= node.p.y()) {
            nearestPoint = nearest(node.rt, nearestPoint, p);
            nearestPoint = nearest(node.lb, nearestPoint, p);
        }
        else {
            nearestPoint = nearest(node.lb, nearestPoint, p);
            nearestPoint = nearest(node.rt, nearestPoint, p);
        }
        return nearestPoint;
    }

    private void range(LinkedList<Point2D> ls, Node node, RectHV rect) {
        if (node == null) {
            return;
        }
        if (rect.intersects(node.rect)) {
            if (rect.contains(node.p)) {
                ls.add(node.p);
            }
            range(ls, node.lb, rect);
            range(ls, node.rt, rect);
        }
    }

    private void draw(Node node) {
        if (node != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(node.p.x(), node.p.y());
            if (node.splits == VERTICAL) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius();
                StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius();
                StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
            }
            draw(node.lb);
            draw(node.rt);
        }

    }

    private Node inertLeftOrRight(Node parent, int orientation, Node current, Point2D p) {
        if (current == null) {
            return new Node(parent, orientation, p);
        }
        double cmp = p.x() - current.p.x();
        if (cmp < 0) {
            current.lb = inertBottomOrTop(current, LEFT, current.lb, p);
        }
        else {
            current.rt = inertBottomOrTop(current, RIGHT, current.rt, p);
        }
        return current;
    }

    private Node inertBottomOrTop(Node parent, int orientation, Node current, Point2D p) {
        if (current == null) {
            return new Node(parent, orientation, p);
        }
        double cmp = p.y() - current.p.y();
        if (cmp < 0) {
            current.lb = inertLeftOrRight(current, BOTTOM, current.lb, p);
        }
        else {
            current.rt = inertLeftOrRight(current, TOP, current.rt, p);
        }
        return current;
    }


    private boolean containBottomOrTop(Node current, Point2D p) {
        if (current == null) {
            return false;
        }
        if (current.p.compareTo(p) == 0) {
            return true;
        }
        double cmp = p.y() - current.p.y();
        if (cmp < 0) {
            return containLeftOrRight(current.lb, p);
        }
        else {
            return containLeftOrRight(current.rt, p);
        }
    }

    private boolean containLeftOrRight(Node current, Point2D p) {
        if (current == null) {
            return false;
        }
        if (current.p.compareTo(p) == 0) {
            return true;
        }
        double cmp = p.x() - current.p.x();
        if (cmp < 0) {
            return containBottomOrTop(current.lb, p);
        }
        else {
            return containBottomOrTop(current.rt, p);
        }
    }

    public static void main(String[] args) {


    }
}
