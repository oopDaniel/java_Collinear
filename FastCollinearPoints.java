/******************************************************************************
 *  Compilation:  javac FastCollinearPoints.java
 *  Execution:    java FastCollinearPoints <input10.txt>
 *  Dependencies: Point.java LineSegment.java
 *
 *  Examines 4 points at a time and checks whether they all lie on the same line segment.
 *  Return all such line segments.
 *
 *  Time complexity: O(n^2 * log(n))
 *
 *  Space proportional to n
 *
 ******************************************************************************/

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


public class FastCollinearPoints {
    private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();

    // Determine the segment and its initial point
    private Point[] points;

    // Slope and initial points for each line
    private HashMap<Double, ArrayList<Point[]>> slopes = new HashMap<Double, ArrayList<Point[]>>();

    // Finds all line segments containing at least 4 points
    public FastCollinearPoints(Point[] inPoints) {
        if (inPoints == null) throw new java.lang.NullPointerException();

        int len = inPoints.length,
            count,
            next,
            begin,
            offset;
        double slope,
               curSlope;
        Point  curr;
        points = new Point[len];

        // check null points & copy
        for (int i = 0; i < len; ++i) {
            if (inPoints[i] == null) {
                throw new java.lang.NullPointerException();
            }
            points[i] = inPoints[i];
        }

        int lenMinusOne = len - 1;
        for (int i = 0; i < lenMinusOne; ++i) {
            curr = points[i];

            // Default using merge sort for sorting objects in Java, O(n * log(n))
            Arrays.sort(points, i, len, curr.slopeOrder());

            next  = i + 1;
            begin = next;
            count = 1;
            slope = curr.slopeTo(points[next]);

            if (next < len && slope == Double.NEGATIVE_INFINITY) {
                throw new java.lang.IllegalArgumentException();
            }

            for (; next < len; ++next) {
                curSlope = curr.slopeTo(points[next]);

                if (hasSameSlope(curSlope, slope)) {
                    ++count;
                } else {
                    if (count > 3) addSegment(points, curr, slope, begin, next - 1);

                    begin = next;
                    slope = curSlope;
                    count = 2;
                }
            }

            if (count > 3) addSegment(points, curr, slope, begin, len - 1);
        }
    }

    /**
     * Calculate the comparing double result and return true if it's the same slope
     * @param  s1 slope to compare
     * @param  s2 slope to compare
     * @return the boolean representation of the same slope
     */
    private void addSegment(Point[] p, Point origin, double slope, int base, int offset) {
        Point start, end;
        Point[] pair;
        ArrayList<Point[]> endPoints;
        boolean shouldAdd = false;

        Arrays.sort(points, base, offset + 1);

        start = points[base];
        end   = points[offset];

        if (origin.compareTo(start) < 0) start = origin;
        if (origin.compareTo(end) > 0)   end   = origin;

        endPoints = slopes.get(slope);

        if (endPoints == null) {
            endPoints = new ArrayList<Point[]>();
            shouldAdd = true;
        }

        if (shouldAdd || !isCollinear(endPoints, start)) {
            pair    = new Point[2];
            pair[0] = start;
            pair[1] = end;

            endPoints.add(pair);
            slopes.put(slope, endPoints);
            segments.add(new LineSegment(pair[0], pair[1]));
        }
    }

    private boolean isCollinear(ArrayList<Point[]> endPoints, Point p) {
        for (Point[] pair: endPoints) {
            if (hasSameSlope(pair[0].slopeTo(pair[1]), pair[0].slopeTo(p)))
                return true;
        }
        return false;
    }

    /**
     * Calculate the comparing double result and return true if it's the same slope
     * @param  s1 slope to compare
     * @param  s2 slope to compare
     * @return the boolean representation of the same slope
     */
    private boolean hasSameSlope(double s1, double s2) {
        return (s1 == Double.POSITIVE_INFINITY && s2 == Double.POSITIVE_INFINITY)
            || (s1 * s2 >= 0 && Math.abs(s1 - s2) <= 0.000000001);
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
