import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final public class FastCollinearPoints {

	private final List<Point> points;
	private LineSegment lineSeg = null;
	private int numSeg = 0;
	private List<LineSegment> segments = new ArrayList<LineSegment>();

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
	public FastCollinearPoints(final Point[] points) {
		if (points == null)
			throw new IllegalArgumentException();
		this.points = new ArrayList<Point>();

		for (int n = 0; n < points.length; n++) {
			if (points[n] != null) {
				if (!isDuplicate(this.points, points[n]))
					this.points.add(points[n]);
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
	private boolean isDuplicate(List<Point> p1, Point p2) {
		boolean duplicate = false;
		if (!p1.isEmpty()) {
			for (int i = 0; i < p1.size(); i++) {
				if (p2.equals(p1.get(i))) {
					duplicate = true;
					break;
				}
			}
		}
		return duplicate;
	}

	public int numberOfSegments() {
		return numSeg;
	}

	/**
	 * Find the segment 1. Think of p as the origin. 2. For each other point q,
	 * determine the slope it makes with p. 3. Sort the points according to the
	 * slopes they makes with p. 4. Check if any 3 (or more) adjacent points in
	 * the sorted order have equal slopes with respect to p. If so, these
	 * points, together with p, are collinear.
	 * 
	 * @return the segment array where all segments stored as (pointA->pointB)
	 *         No duplicate segments, not solving subsegment problem
	 */
	public LineSegment[] segments() {
		List<Double> slopes1;
		List<Point> coPoints = new ArrayList<Point>();

		for (int i = 0; i < points.size(); i = 0) {
			// need to sort every time since it will be ordered according to
			// slope later
			Collections.sort(points);
			// stores the slopes of every other points to origin point
			slopes1 = new ArrayList<Double>();
			for (int j = i + 1; j < points.size(); j++) {
				slopes1.add(points.get(i).slopeTo(points.get(j)));
			}

			Collections.sort(slopes1); // sort the slope array
			Point orign = points.get(i); // store the origin point

			Collections.sort(points, points.get(i).slopeOrder()); // sort the
																	// points
																	// according
																	// to
																	// slopeOrder
			points.remove(0); // remove the origin point

			// visit the slope array to check same slope
			for (int k = 0; k < slopes1.size(); k++) {
				coPoints.clear();
				coPoints.add(points.get(k));
				for (int m = k + 1; m < slopes1.size(); m++) {
					if (m != k && slopes1.get(m).equals(slopes1.get(k))) {
						coPoints.add(points.get(m));
					} else
						break;
				}

				// if over 3 points have same slope to origin, construct segment
				if (coPoints.size() >= 3) {
					coPoints.add(orign);
					Collections.sort(coPoints);
					lineSeg = new LineSegment(coPoints.get(0), coPoints.get(coPoints.size() - 1));
					segments.add(lineSeg);
					numSeg++;
					// determining the index of backtracking
					k = k + coPoints.size() - 2;
				}
			}
		}
		final LineSegment[] segmentsNew = new LineSegment[segments.size()];
		for (int n = 0; n < segments.size(); n++) {
			segmentsNew[n] = segments.get(n);
		}
		return segmentsNew;
	}
}
