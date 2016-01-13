package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ml.model.kmeans.EuclideanPoint2D;
import ml.model.kmeans.KMeans;
import ml.model.kmeans.Points2DSet;
import ml.utils.PointsChart;
import ml.utils.Utils;

import org.junit.Test;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class KMeansTest {

	@Test
	public void test1() throws Exception {
		Points2DSet set = Points2DSet.create(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 20, 20, 21, 21, 22, 22, 23, 23, 24, 24, 25, 25, 26, 26, 27,
				27, 28, 28, 29, 29);
		set.chart();
		KMeans kMeans = new KMeans(set, 2);
		Map<EuclideanPoint2D, List<EuclideanPoint2D>> s = kMeans.compute();
		List<double[][]> series = Points2DSet.asListOfArrays(s);
		new PointsChart("k-means", series);
		System.in.read();
	}

	@Test
	public void test2() throws Exception {
		int n = 10000;
		List<EuclideanPoint2D> l = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			l.add(new EuclideanPoint2D(Utils.randomDouble(0, 100), Utils.randomDouble(0, 100)));
		}

		Points2DSet set = new Points2DSet(l);
		KMeans kMeans = new KMeans(set, 2);

		Map<EuclideanPoint2D, List<EuclideanPoint2D>> s = kMeans.compute();
		Utils.showClusterChart(s);
		System.in.read();
	}

	
}
