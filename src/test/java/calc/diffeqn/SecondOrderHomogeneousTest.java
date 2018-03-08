package calc.diffeqn;

import java.text.MessageFormat;

import ml.model.function.Function;

import org.apache.log4j.Logger;
import org.junit.Test;

import prob.chart.ChartBuilder;
import prob.chart.PlotType;
import util.MLUtils;
import static java.lang.Math.*;
import static org.junit.Assert.*;
import diffeqn.SecondOrderHomogeneous;

public class SecondOrderHomogeneousTest {

	private static final Logger log = Logger.getLogger(SecondOrderHomogeneousTest.class);

	@Test
	public void testUnderDamped() throws Exception {
		SecondOrderHomogeneous eq = new SecondOrderHomogeneous(1, 1, 3, 1, 0);
		System.out.println(eq);
		eq.solve();
	}

	@Test
	public void testOverDamped() throws Exception {
		SecondOrderHomogeneous eq = new SecondOrderHomogeneous(1, 4, 3, 1, 0);
		System.out.println(eq);
		eq.solve();
	}

	@Test
	public void testCriticallyDamped() throws Exception {
		SecondOrderHomogeneous eq = new SecondOrderHomogeneous(1, 4, 4, 1, 0);
		System.out.println(eq);
		eq.solve();
	}

	@Test
	public void testVibrations() throws Exception {
		double x0 = 1;
		double xPrime0 = 0;
		double bestT = Double.POSITIVE_INFINITY;
		double bestB = 0;
		for (double b = 0.1; b < 2; b+=0.01) {
			double tEquilibrium = findEquilibruim(0.5, b, 0.5, x0, xPrime0);
			if(tEquilibrium < bestT) {
				bestT = tEquilibrium;
				bestB = b;
			}
		}
		System.out.println("Best b value is:" + bestB + ". It reaches equilibruim in " + bestT + " seconds.");
	}
	
	@Test
	public void testRockingMotion() throws Exception {
		SecondOrderHomogeneous eq = new SecondOrderHomogeneous(1, 0.1, 2, 0, 5);
		log.info(eq);
		Function f = eq.solve();
		new ChartBuilder().add(f, 0, 20, 1000, "diff", PlotType.Line).build();
		System.in.read();
	}

	private double findEquilibruim(double m, double b, double k, double x0, double xPrime0) throws Exception {
		log.info(MessageFormat.format("m={0}, b={1}, k={2}, x0={3}, xPrime0={4}", m, b, k, x0, xPrime0));
		SecondOrderHomogeneous eq = new SecondOrderHomogeneous(m, b, k, x0, xPrime0);

		log.info(eq);
		Function f = eq.solve();
		double t = 0;
		double step = 0.0001;
		double delta = 0.0001;
		boolean justFound = false;
		double found = 0;
		while (t < 20) {
			double d = f.evaluate(t);
			if (abs(abs(d) - 0.005) < delta) {
				//log.info(MLUtils.format6(t));
				if (!justFound) {
					//log.info(MLUtils.format6(t));
					found = t;
					justFound = true;
				}
			} else {
				justFound = false;
			}
			t += step;
		}
		return found;
	}

}
