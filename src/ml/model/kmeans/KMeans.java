package ml.model.kmeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clustering k-means algo
 * @author Cosmin Zamfir
 *
 */
public class KMeans {

	private Points2DSet points;
	private int k;

	public KMeans(Points2DSet points, int k) {
		this.points = points;
		this.k = k;
	}

	public double distance(EuclideanPoint2D p1, EuclideanPoint2D p2) {
		return distanceImp1(p1, p2);
	}
	
	public double distanceImp1(EuclideanPoint2D p1, EuclideanPoint2D p2) {
		return Math.sqrt((p1.X() - p2.X()) * (p1.X() - p2.X()) + (p1.Y() - p2.Y()) * (p1.Y() - p2.Y()));
	}

	public double distanceImp2(EuclideanPoint2D p1, EuclideanPoint2D p2) {
		return Math.abs(Math.abs(p1.distanceFromCenter()) - Math.abs(p2.distanceFromCenter()));
	}

	public double distanceImp3(EuclideanPoint2D p1, EuclideanPoint2D p2) {
		if (p1.X() > p1.Y() && p1.X() > p2.Y() || p1.X() < p2.Y() && p2.X() < p2.Y()) {
			return 1;
		}
		else {
			return -1;
		}
	}

	/**
	 * <ol>
	 * <li> Select k centroids at random
	 * <li> Build the initial k clusters by assigning each point to the least distant point
	 * <li> Do
	 * <ol>
	 * 		<li> Recompute the centrods of the 'so far' clusters
	 * 		<li> Recompute the cluters by assigning points to the new centroids 
	 * </ol>
	 * <li> Until the gain in the cluster quality function is insignificant, but no more that 50 times
	 * </ol> 
	 * @return the cluster as a Map[EuclideanPoint2D, List<EuclideanPoint2D]
	 */
	public Map<EuclideanPoint2D, List<EuclideanPoint2D>> compute() {
		int repetitions = 50;
		List<EuclideanPoint2D> centroids = points.getRandomPoints(k);
		Map<EuclideanPoint2D, List<EuclideanPoint2D>> initialCluster = buildCluster(centroids);
		Map<EuclideanPoint2D, List<EuclideanPoint2D>> newCluster = null;
		Map<EuclideanPoint2D, List<EuclideanPoint2D>> prevCluster = initialCluster;
		for (int i = 0; i < repetitions; i++) {
			List<EuclideanPoint2D> newCentroids = computeCentroids(prevCluster);
			newCluster = buildCluster(newCentroids);
			prevCluster = newCluster;
		}
		return newCluster;

	}

	private List<EuclideanPoint2D> computeCentroids(Map<EuclideanPoint2D, List<EuclideanPoint2D>> cluster) {
		List<EuclideanPoint2D> res = new ArrayList<>();
		for (List<EuclideanPoint2D> points : cluster.values()) {
			res.add(EuclideanPoint2D.centroid(points));
		}
		return res;
	}

	private Map<EuclideanPoint2D, List<EuclideanPoint2D>> buildCluster(List<EuclideanPoint2D> centroids) {
		Map<EuclideanPoint2D, List<EuclideanPoint2D>> res = initializeMap(centroids);
		//assign points to the res based on minimum distance
		for (EuclideanPoint2D point : points.getPoints()) {
			EuclideanPoint2D leastDistantCentroid = leastDistant(point,centroids);
			res.get(leastDistantCentroid).add(point);
		}
		return res;
	}

	private Map<EuclideanPoint2D, List<EuclideanPoint2D>> initializeMap(List<EuclideanPoint2D> centroids) {
		Map<EuclideanPoint2D, List<EuclideanPoint2D>> res = new HashMap<EuclideanPoint2D, List<EuclideanPoint2D>>();
		for (EuclideanPoint2D centroid : centroids) {
			res.put(centroid, new ArrayList<EuclideanPoint2D>());
		}
		return res;
	}

	public EuclideanPoint2D leastDistant(EuclideanPoint2D point, List<EuclideanPoint2D> points) {
		double minDistance = Double.POSITIVE_INFINITY;
		EuclideanPoint2D res = points.get(0);
		for (EuclideanPoint2D candidate : points) {
			if (distance(point, candidate) < minDistance) {
				minDistance = distance(point, candidate);
				res = candidate;
			}
		}
		return res;
	}

}
