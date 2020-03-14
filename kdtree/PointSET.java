/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> points;

    public PointSET()                               // construct an empty set of points
    {
        points = new TreeSet<>();
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return points.isEmpty();
    }

    public int size()                         // number of points in the set
    {
        return points.size();
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (!points.contains(p)) {
            points.add(p);
        }
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return points.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        Iterator<Point2D> iterator = points.iterator();
        while (iterator.hasNext()) {
            Point2D point = iterator.next();
            point.draw();
        }
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Set<Point2D> fitpoints = new TreeSet<>();
        Iterator<Point2D> iterator = points.iterator();
        while (iterator.hasNext()) {
            Point2D point = iterator.next();
            if (rect.contains(point)) {
                fitpoints.add(point);
            }
        }
        return fitpoints;
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D nearestp = null;
        double mindistance = Double.POSITIVE_INFINITY;
        Iterator<Point2D> iterator = points.iterator();
        while (iterator.hasNext()) {
            Point2D point = iterator.next();
            if (p.distanceSquaredTo(point) < mindistance) {
                mindistance = p.distanceSquaredTo(point);
                nearestp = point;
            }
        }
        return nearestp;
    }

    public static void main(String[] args) {


    }
}
