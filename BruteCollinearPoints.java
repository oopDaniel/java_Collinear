/******************************************************************************
 *  Compilation:  javac BruteCollinearPoints.java
 *  Execution:    java BruteCollinearPoints <input10.txt>
 *  Dependencies: Point.java LineSegment.java
 *
 *  Examines 4 points at a time and checks whether they all lie on the same line segment.
 *  Return all such line segments.
 *
 *  Time complexity: O(n^4)
 *
 ******************************************************************************/

import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


public class BruteCollinearPoints {
    private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new java.lang.NullPointerException();

        int len = points.length;
        double slope;
        Point curr;

        for (int i = 0; i < len; ++i) {
            // check null points
            if (points[i] == null) {
                throw new java.lang.NullPointerException();
            }

            // check duplicate points
            if (i + 1 < len && points[i].compareTo(points[i + 1]) == 0) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        Arrays.sort(points);

        for (int i = 0; i < len - 3; ++i) {
            curr = points[i];

            for (int j = i + 1; j < len - 2; ++j) {
                slope = curr.slopeTo(points[j]);

                for (int k = j + 1; k < len - 1; ++k) {
                    double slopeK = curr.slopeTo(points[k]);

                    if (hasSameSlope(slope, slopeK)) {

                        for (int l = k + 1; l < len; ++l) {
                            double slopeL = curr.slopeTo(points[l]);

                            if (hasSameSlope(slope, slopeL)) {
                                segments.add(new LineSegment(curr, points[l]));
                                break;
                            }
                        }
                    }
                }
            }

        }
    }

    /**
     * Calculate the comparing double result and return true if it's the same slope
     * @param  s1 slope to compare
     * @param  s2 slope to compare
     * @return the boolean representation of the same slope
     */
    private boolean hasSameSlope(double s1, double s2) {
        return s1 * s2 >= 0 && Math.abs(s1 - s2) <= 0.000001;
    }

    /**
     * The number of line segments
     * @return the integer of line segments
     */
    public int numberOfSegments() {
        return segments.size();
    }

    /**
     * The line segments
     * @return the line segments
     */
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
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
