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
    private Point[] segmentPoints = new Point[3];

    // Slope and initial points for each line
    private HashMap<Double, ArrayList<Point>> slopes = new HashMap<Double, ArrayList<Point>>();

    // Finds all line segments containing at least 4 points
    public FastCollinearPoints(Point[] inPoints) {
        if (inPoints == null) throw new java.lang.NullPointerException();

        int    len = inPoints.length,
               count,
               next;
        double slope;
        Point  curr;
        points = new Point[len];

        // check null points
        for (int i = 0; i < len; ++i) {
            if (inPoints[i] == null) {
                throw new java.lang.NullPointerException();
            }
            points[i] = inPoints[i];
        }

        // Arrays.sort(points);

        for (int i = 0; i < len; ++i) {
            curr = points[i];

            // Default using merge sort for sorting objects in Java, O(n * log(n))
            Arrays.sort(points, i, len, curr.slopeOrder());


            // System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
            // for (int j = i; j < len; ++j) {
            //     System.out.println("Point: " + points[j] + ", SlopeTo:" + curr.slopeTo(points[j]));
            // }
            // System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");

            next = i + 1;
            if (next < len && curr.slopeTo(points[next]) == Double.NEGATIVE_INFINITY) {
                throw new java.lang.IllegalArgumentException();
            }

            // System.out.println(">>> curr point: " + curr);

            for (; next < len;) {
                int begin = next;
                count = 1; // Current point
                slope = curr.slopeTo(points[next]);
                // System.out.println(" Slope: " + slope);

                do {
                    // System.out.println(" - step on " + points[next]);
                    ++count;
                    ++next;
                } while (next < len && hasSameSlope(slope, curr.slopeTo(points[next])));

                if (count > 2) {
                    Arrays.sort(points, begin, next);
                    --next;
                }

                // System.out.println(" count: "+count+", next: "+next);

                // At least 4 points
                if (count > 3) {
                    boolean isDuplicated = false;
                    ArrayList<Point> initialPoints = slopes.get(slope);
                    if (initialPoints == null) {
                        initialPoints = new ArrayList<Point>();
                    }

                    // for (int ii = begin; ii < next + 1; ii++) {
                    //     System.out.println("P:" + points[ii] + ", slope: "+ curr.slopeTo(points[ii]));
                    // }

                    segmentPoints[0] = curr;
                    segmentPoints[1] = points[begin];
                    segmentPoints[2] = points[next];
                    Arrays.sort(segmentPoints);

                    for (Point p : initialPoints) {
                        // System.out.println("init points: " + p + ", slope: " + slope);
                        // Has the same slope to the initial point, thus collinear
                        if (hasSameSlope(slope, segmentPoints[0].slopeTo(p)) ||
                            hasSameSlope(slope, segmentPoints[2].slopeTo(p))) {
                            isDuplicated = true;
                            break;
                        }
                    }

                    // for (Point p : segmentPoints) {
                    //     System.out.println("seg points: " + p );
                    // }

                    if (!isDuplicated) {
                        // System.out.println("@@ no Duplicated!! will save" + segmentPoints[0] + segmentPoints[2]);
                        initialPoints.add(segmentPoints[0]);
                        initialPoints.add(segmentPoints[2]);
                        slopes.put(slope, initialPoints);
                        segments.add(new LineSegment(segmentPoints[0], segmentPoints[2]));
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
