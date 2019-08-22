/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private final List<LineSegment> segments = new ArrayList<>(0);

    private Point[] pointsCopy;

    public FastCollinearPoints(Point[] points) {
        if (points == null || points.length == 0) {
            throw new IllegalArgumentException();
        }
        pointsCopy = new Point[points.length];
        Point[] tempPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            tempPoints[i] = points[i];
            pointsCopy[i] = points[i];
        }
        if (!checkRepeated(pointsCopy)) {
            throw new IllegalArgumentException();
        }
        Point[] collinearPoints = new Point[pointsCopy.length];
        for (Point p : pointsCopy) {
            int index = 0;
            collinearPoints[index++] = p;
            Arrays.sort(tempPoints, p.slopeOrder());
            double lastS = p.slopeTo(tempPoints[0]);
            for (int i = 1; i < tempPoints.length; i++) {
                double currentS = p.slopeTo(tempPoints[i]);
                if (currentS != lastS) {
                    checkAndAddSegment(index, collinearPoints);
                    lastS = currentS;
                    index = 1;
                }
                collinearPoints[index++] = tempPoints[i];
            }
            checkAndAddSegment(index, collinearPoints);
        }
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[numberOfSegments()]);
    }

    private void checkAndAddSegment(int index, Point[] collinearPoints) {
        if (index >= 4) {
            Arrays.sort(collinearPoints, 1, index);
            if (collinearPoints[0].compareTo(collinearPoints[1]) < 0) {
                segments.add(new LineSegment(collinearPoints[0], collinearPoints[index - 1]));
            }
        }
    }

    private boolean checkRepeated(Point[] points) {
        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
