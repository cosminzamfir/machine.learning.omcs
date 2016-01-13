package ml.model.kmeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ml.utils.PointsChart;
import ml.utils.Utils;

public class Points2DSet {

	private List<EuclideanPoint2D> points;
	
	public static Points2DSet create(double ... coordinates) {
		List<EuclideanPoint2D> points = new ArrayList<EuclideanPoint2D>();
		for (int i = 0; i < coordinates.length; i+=2) {
			points.add(new EuclideanPoint2D(coordinates[i], coordinates[i+1]));
		}
		return new Points2DSet(points);
	}

	public Points2DSet(List<EuclideanPoint2D> points) {
		super();
		this.points = points;
	}

	public List<EuclideanPoint2D> getPoints() {
		return points;
	}

	public void setPoints(List<EuclideanPoint2D> points) {
		this.points = points;
	}

	public List<EuclideanPoint2D> getRandomPoints(int count) {
		return Utils.randomChoice(points, count, true);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (EuclideanPoint2D point : points) {
			sb.append(point).append("\n");
		}
		return sb.toString();
	}
	
	public void chart() {
		List<double[][]> l = new ArrayList<>();
		l.add(asArray());
		new PointsChart("Points", l);
	}
	
	public double[][] asArray()	{
		return asArray(points);
	}

	public static double[][] asArray(List<EuclideanPoint2D> points) {
		double[][] res = new double[points.size()][2];
		int index = 0;
		for (EuclideanPoint2D point : points) {
			res[index][0] = point.X();
			res[index++][1] = point.Y();
		}
		return res;
	}

	public static List<double[][]> asListOfArrays(Map<EuclideanPoint2D, List<EuclideanPoint2D>> map) {
		List<double[][]> res = new ArrayList<double[][]>();
		for (List<EuclideanPoint2D> l : map.values()) {
			res.add(asArray(l));
		}
		return res;
	}

}
