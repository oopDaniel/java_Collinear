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
    private ArrayList<LineSegment> _segments= new ArrayList<LineSegment>();
    private HashMap<Double, Integer> _slopes = new HashMap<Double, Integer>();

    // finds all line segments containing 4 points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new java.lang.NullPointerException();

        int len = points.length,
            count,
            next;
        double slope;
        Point curr;

        for (int i = 0; i < len; ++i) {
            curr = points[i];
            // check null points
            if (curr == null) {
                throw new java.lang.NullPointerException();
            }

            // check duplicate points
            if (i + 1 < len && curr.compareTo(points[i + 1]) == 0) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        Arrays.sort(points);


        for (int i = 0; i < len - 3; ++i) {
            curr = points[i];

            // Default using merge sort for sorting objects in Java, O(n * log(n))
            Arrays.sort(points, 0, len, curr.slopeOrder());

            System.out.println("- now inner points:");
            for (int index = 0; index < len; ++index) {
                System.out.println(" " + points[index]+ curr.slopeTo(points[index]));
            }
            System.out.println("======");

            System.out.println("~~~~~~restart i = " + i + "~~~~~~~~");

            next = 0;
            for (; next < len; next++) {
                count = 1; // Current point plus the next one
                slope = curr.slopeTo(points[next]);

                System.out.println("curr point = " + curr + "; next point = " + points[next]);
                System.out.println("now next = " + next);
                System.out.println("slope = " + slope);

                while (next < len && hasSameSlope(slope, curr.slopeTo(points[next]))) {
                    System.out.println("step on " + points[next]);
                    ++count;
                    ++next;
                }

                if (count > 1) next --;

                System.out.println("count " + count +  " " + next);

                // At least 4 points
                if (count > 3) {
                    System.out.println("><><>< next: " + next + ", saved" + curr + " " + points[next]);
                    Integer prevCount = _slopes.get(slope);
                    if (prevCount == null || prevCount < count) {
                        System.out.println("!!!!perv the slope" + slope);
                        _segments.add(new LineSegment(curr, points[next]));
                        _slopes.put(slope, count);
                    } else {
                        System.out.println("!!!!sorry");
                    }
                } else {
                    System.out.println("(bad)");
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
        return _segments.size();
    }

    /**
     * The line segments
     * @return the line segments
     */
    public LineSegment[] segments() {
        return _segments.toArray(new LineSegment[_segments.size()]);
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

        StdOut.println("-end");
    }
}
