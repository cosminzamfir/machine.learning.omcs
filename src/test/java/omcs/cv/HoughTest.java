package omcs.cv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prob.chart.ChartBuilder;
import prob.chart.PlotType;
import util.MLUtils;
import ml.model.function.Function;
import static java.lang.Math.*;

/**
 * Given a line , plot each point of the line in the polar coordinates 
 * @author eh2zamf
 *
 */
public class HoughTest {

	public static void main(String[] args) {
		ChartBuilder cb = new ChartBuilder();
		polar(cb);
		//cartezian(cb);
		cb.build();
	}
	
	public static void polar(ChartBuilder cb) {
		List<double[]> l = getPoints();
		for (double[] ds : l) {
			Map<Double, Double> map = runPolar(ds[0], ds[1]);
			cb.add(map, "HoughLinePolarCoord[" + ds[0] + "," + ds[1] + "]", PlotType.Line);
		}
	}
	
	public static void cartezian(ChartBuilder cb) {
		List<double[]> l = getPoints();
		for (double[] ds : l) {
			Function houghliFunction = runCartezian(ds[0], ds[1]);
			cb.add(houghliFunction, -20, 20, 1000, "HoughCartPolarCoord[" + ds[0] + "," + ds[1] + "]", PlotType.Line);
		}
	}

	private static List<double[]> getPoints() {
		List<double[]> l = new ArrayList<double[]>();
		l.add(new double[] {0,0});
		l.add(new double[] {1,1});
		l.add(new double[] {2,3});
		return l;
	}
	/**
	 * Input: the coordinate of a point in 2D space
	 * <p>Output: the line in Hough space, which represent all lines in 2D which can contain the given point 
	 * <p>Algorithm:
	 * <ul>
	 * <li> The line containing x0,y0 comply with : y0=m*x0+b
	 * <li> Therefore, m and b are related by: b = -x0*m +y0. This is the equation of the line in the Houugh space  
	 * <li> In polar coordinate, based on the equality: y = (-1)cos(theta)/sin(theta) * x + r/sin(theta) => y*sin(theta) = -cos(theta)*x + r =>
	 * r = y*sint(theta) + x*cos(theta)
	 * <li> Therefore, grid theta form -pi/2 to pi/2 , calculate r for each and plot the pairs 
	 * </ul>
	 * @param x0
	 * @param y0
	 */
	public static Map<Double, Double> runPolar(double x0, double y0) {
		Map<Double, Double> res = new HashMap<Double, Double>();
		Function houghLine = m -> -x0*m + y0;
		//new ChartBuilder().add(houghLine, -20, 20, 1000, "Hough line", PlotType.Line).build();
		System.out.println("Hough line in cartesian coordinates: " + houghLine);
		//compute the b0,m0 of the intercepts of the houghLine
		double b0 = houghLine.evaluate(0);
		double m0 = y0/x0;
		for (double theta = 0; theta < 2*PI; theta+=0.01) {
			double r = b0*sin(theta) + m0*cos(theta);
			res.put(theta, r);
		}
		return res;
	}
	
	public static Function runCartezian(double x0, double y0) {
		Function houghLine = m -> -x0*m + y0;
		return houghLine;
		
	}
}
