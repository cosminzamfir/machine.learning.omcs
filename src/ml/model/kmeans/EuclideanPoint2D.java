package ml.model.kmeans;

import java.util.List;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class EuclideanPoint2D {

	private double x;
	private double y;

	public EuclideanPoint2D(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public double X() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double Y() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public double distance1(EuclideanPoint2D other) {
		return Math.sqrt((other.X()-this.X())*(other.X()-this.X()) + (other.Y()-this.Y())*(other.Y()-this.Y())); 
	}
	
	public double distanceFromCenter() {
		 return x*x + y*y;
	}
	
	/**
	 * Creates adn return the centroid of the given points
	 * @param points
	 * @return the centroid as a {@link EuclideanPoint2D}
	 */
	public static EuclideanPoint2D centroid(List<EuclideanPoint2D> points) {
		double x = 0;
		double y = 0;
		for (EuclideanPoint2D point : points) {
			x += point.X();
			y += point.Y();
		}
		return new EuclideanPoint2D(x/points.size(), y/points.size());
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

}
