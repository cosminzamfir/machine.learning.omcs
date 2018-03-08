package diffeqn;

import static java.lang.Math.*;

import java.text.MessageFormat;

import ml.model.function.Function;
import ml.model.function.Solver;

import org.apache.log4j.Logger;

/**
 * m*x'' + b*x' + k*x = 0
 * @author Cosmin Zamfir
 *
 */
public class SecondOrderHomogeneous {

	private static final Logger log = Logger.getLogger(SecondOrderHomogeneous.class);
	private double m;
	private double b;
	private double k;
	private double ctrl;
	private double x0;
	private double xPrime0;
	private double p;
	private double omega_d;
	private double omega_n;
	private double firstRoot;
	private double secondRoot;
	private Function solution;

	public SecondOrderHomogeneous(double m, double b, double k, double x0, double xPrime0) {
		super();
		this.m = m;
		this.b = b;
		this.k = k;
		this.x0 = x0;
		this.xPrime0 = xPrime0;
	}

	public Function solve() {
		p = b / (2 * m);
		ctrl = pow(b, 2) - 4 * m * k;
		omega_n = sqrt(k / m);
		if (ctrl < 0) {
			omega_d = sqrt(-ctrl) / (2 * m);
		}
		if (ctrl < 0) {
			return solveUnderDamped();
		} else if (ctrl == 0) {
			return solveCriticallyDumped();
		} else {
			return solveOverDamped();
		}
	}
	
	@Override
	public String toString() {
		String s = m + "*x''''(t) + " + b + "*x''(t) + " + k + "*x(t) = 0; x(0) = " + x0 + "; x'(0) = " + xPrime0;
		return MessageFormat.format(s, m,b,k,x0,xPrime0);
	}

	private Function solveUnderDamped() {
		double A = x0;
		double B = (p * A + xPrime0)/omega_d;
		String f = MessageFormat.format("x(t) = e^({0}*t) * [{1}*cos({2}*t) + {3}*sin({2}*t)]", -p, A, omega_d, B);
		log.info("Solution: " + f);
		return (t -> pow(E, -p * t) * (A * cos(omega_d * t) + B * sin(omega_d * t)));
	}

	private Function solveOverDamped() {
		firstRoot = -p - sqrt(ctrl) / (2 * m);
		secondRoot = -p + sqrt(ctrl) / (2 * m);
		double B = (xPrime0 - firstRoot * x0) / (secondRoot - firstRoot);
		double A = x0 - B;
		String f = MessageFormat.format("x(t) = {0}*e^({1}*t) + {2}*e^({3}*t)", A, firstRoot, B, secondRoot);
		log.info("Solution: " + f);
		return (t -> A * pow(E, firstRoot * t) + B * pow(E, secondRoot * t));
	}

	private Function solveCriticallyDumped() {
		firstRoot = secondRoot = -p;
		double A = x0;
		double B = A * p + xPrime0;
		String f = MessageFormat.format("x(t) = {0}*e^({1}*t) + {2}*t*e^({3}*t)", A, firstRoot, B, secondRoot);
		log.info("Solution: " + f);
		return (t -> A * pow(E, firstRoot * t) + B * t * pow(E, secondRoot * t));
	}
	
	public static void main(String[] args) {
		SecondOrderHomogeneous eq = new SecondOrderHomogeneous(0.5, 0.6, 0.5, 1, -1);
		Function f = eq.solve();
		Solver solver = new Solver();
		double t = solver.solve(f, 0.005, 0.0001, 0);
		System.out.println(t);
	}
}
