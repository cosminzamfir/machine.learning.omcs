import java.util.ArrayList;
import java.util.List;

import ml.ulearning.kmeans.model.EuclideanPoint2D;
import ml.utils.Utils;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class KMeansSimulator {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.err
					.println("Usage: java KMeansSimulator -noClusters <noClusters> -area <minX maxX minY maxY counts>[1..n] -distanceFunction <euclidian/distanceFromCenter/sameSideOfLine|<yIntercept>|<angleInDegrees>>");
		}
		int noClusters = 0;
		String distanceFunction = null;
		List<EuclideanPoint2D> points = new ArrayList<>();
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-noClusters")) {
				noClusters = Integer.parseInt(args[i++]);
			} else if (args[i].equals("-area")) {
				int minX = Integer.parseInt(args[i++]);
				int maxX = Integer.parseInt(args[i++]);
				int minY = Integer.parseInt(args[i++]);
				int maxY = Integer.parseInt(args[i++]);
				int counts = Integer.parseInt(args[i++]);
				addPoints(minX, maxX, minY, maxY, counts, points);
			} else if (args[i].equals("-distanceFunction")) {
				distanceFunction = args[i++];
			} else {
				System.err
						.println("Unknown param:"
								+ args[i]
								+ ". Usage: java KMeansSimulator -noClusters <noClusters> [-area <minX maxX minY maxY counts>]+ -distanceFunction <euclidian/distanceFromCenter/sameSideOfLine|<yIntercept>|<angleInDegrees>>");
			}
		}
	}

	private static void addPoints(int minX, int maxX, int minY, int maxY, int counts, List<EuclideanPoint2D> points) {
		for (int i = 0; i < counts; i++) {
			points.add(new EuclideanPoint2D(Utils.randomDouble(minX, maxX), Utils.randomDouble(minY, maxY)));
		}
	}
}
