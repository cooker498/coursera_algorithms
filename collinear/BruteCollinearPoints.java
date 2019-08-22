import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private final List<LineSegment> segments = new ArrayList<>(0);

    private Point[] pointsCopy;


    public BruteCollinearPoints(Point[] points) {
        if (points == null || points.length == 0) {
            throw new IllegalArgumentException();
        }
        pointsCopy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            pointsCopy[i] = points[i];

        }
        if (!checkRepeated(pointsCopy)) {
            throw new IllegalArgumentException();
        }
        for (int index1 = 0; index1 < pointsCopy.length - 3; index1++) {
            for (int index2 = index1 + 1; index2 < pointsCopy.length - 2; index2++) {
                for (int index3 = index2 + 1; index3 < pointsCopy.length - 1; index3++) {
                    for (int index4 = index3 + 1; index4 < pointsCopy.length; index4++) {
                        Point[] points1 = new Point[4];
                        points1[0] = pointsCopy[index1];
                        points1[1] = pointsCopy[index2];
                        points1[2] = pointsCopy[index3];
                        points1[3] = pointsCopy[index4];
                        Arrays.sort(points1);
                        if (checkCollinear(points1)) {
                            segments.add(new LineSegment(points1[0], points1[3]));
                        }
                    }
                }
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

    private boolean checkCollinear(Point[] points) {
        double s = points[0].slopeTo(points[1]);
        for (int i = 1; i < points.length; i++) {
            if (points[0].slopeTo(points[i]) != s) {
                return false;
            }
        }
        return true;
    }


    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[numberOfSegments()]);
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
