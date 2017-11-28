import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final public class BruteCollinearPoints {
	private final Point[] points;
	private LineSegment lineSeg = null;
	private int numSeg = 0;

	/**
	 * Constructor 1. ensure the delivered argument is non-null 2. ensure there
	 * is no null element in delivered argument -if have, don't copy 3. ensure
	 * there is no duplicate point
	 * 
	 * All exceptions return "IllegalArgumentException" !!!Attention: do not
	 * copy as "this.points = points" - this is reference copy,where address is
	 * copied,not value.
	 * 
	 * @param points
	 *            array that stores all points read from file
	 */
	public BruteCollinearPoints(final Point[] points) {
		if (points == null)
			throw new IllegalArgumentException();
		this.points = new Point[points.length];
		for (int n = 0; n < points.length; n++) {
			if (points[n] != null) {
				if (!isDuplicate(this.points, points[n]))
					this.points[n] = points[n];
				else
					throw new IllegalArgumentException();
			} else
				throw new IllegalArgumentException();
		}
	}

	/**
	 * Helper function : check duplications Compare whether incoming element is
	 * same as the element in copying array.
	 * 
	 * @param p1
	 *            is copying array, p2 is incoming element
	 * @return boolean variable: true or false
	 */
	private boolean isDuplicate(Point[] p1, Point p2) {
		boolean duplicate = false;
		for (int i = 0; p1[i] != null; i++) {
			if (p2.equals(p1[i])) {
				duplicate = true;
				break;
			}
		}
		return duplicate;
	}

	public int numberOfSegments() {
		return numSeg;
	}

	/**
	 * Find the segment No duplicate segments, no considering 5 or more points
	 * consisting of one segment, 4 is the maximum; Time complexity is n^4
	 * 
	 * @return the segment array where all segments stored as (pointA->pointB)
	 */
	public LineSegment[] segments() {
		// sorted points is the key to ensuring no duplication
		Arrays.sort(points);
		List<LineSegment> segmentsTemp = new ArrayList<LineSegment>();
		Point[] coPoints = new Point[4];
		double slope1 = 0.0; // store slope between A->B
		double slope2 = 0.0; // store slope between A->C
		double slope3 = 0.0; // store slope between A->D
		try {
			// Check A,B,C if（slopeAB == slopeAC),check D, else break
			// If A,B,C,D in the same line, store the points in array coPoints,
			// sort array to find endpoints, construct segment obj.

			for (int i = 0; i < points.length; i++) {
				for (int j = i + 1; j < points.length; j++) {
					for (int k = j + 1; k < points.length; k++) {
						slope1 = points[i].slopeTo(points[j]);
						slope2 = points[i].slopeTo(points[k]);
						if (!(slope1 > slope2 || slope1 < slope2)) {
							for (int l = k + 1; l < points.length; l++) {
								slope3 = points[i].slopeTo(points[l]);
								if (!(slope1 > slope3 || slope1 < slope3)) {
									coPoints[0] = points[i];
									coPoints[1] = points[j];
									coPoints[2] = points[k];
									coPoints[3] = points[l];
									Arrays.sort(coPoints);
									lineSeg = new LineSegment(coPoints[0], coPoints[3]);
									segmentsTemp.add(lineSeg);
									numSeg++;
								}
							}
						}
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		final LineSegment[] segments = new LineSegment[segmentsTemp.size()];
		for (int n = 0; n < segmentsTemp.size(); n++)
			segments[n] = segmentsTemp.get(n);
		return segments;
	}
}
