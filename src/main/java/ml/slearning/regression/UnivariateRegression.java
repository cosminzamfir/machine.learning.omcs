package ml.slearning.regression;

import linalg.Matrix;
import linalg.Vector;
import ml.model.DataSet;
import ml.model.Observation;
import ml.model.function.PolynomialFunction;
import ml.utils.DataSetCsvParser;
import ml.utils.FunctionChart;

/**
 * Input: x's mapped to y's
 * Output: a {@link PolynomialFunction} which minimizes the squared error
 * In matrix form (ai are the grade n PolynomialFunction coeficients for a {@link DataSet} with m {@link Observation}s)
 * <p>| 1  x1  x1^2   ...  x1^n | * |a1| = |y1|  		
 * <p>| 1  x2  x2^2   ...  x2^n |   |a2|   |y2|
 * <p> ...						     ...   ...
 * <p>| 1  xm  xm^2   ...  xm^n |   |an|   |ym|
 * As the above above has in general no exact solution, compute vector a' which solves X*a' = projection of y on column space of X
 * The vector is: a' = inverse(transpose(X) * X) * transpose(X) * y
 * @see http://ocw.mit.edu/courses/mathematics/18-06sc-linear-algebra-fall-2011/least-squares-determinants-and-eigenvalues/projections-onto-subspaces/MIT18_06SCF11_Ses2.2sum.pdf
 * @author Cosmin Zamfir
 *
 */
public class UnivariateRegression {

	public PolynomialFunction compute(DataSet dataSet, int range) {
		Matrix X = getMatrix(dataSet, range);
		Vector y = getYVector(dataSet);
		Matrix Xt = X.transpose();
		double coefficients[] = Xt.multiplyBy(X).inverse().multiplyBy(Xt).multiplyBy(Matrix.columnMatrix(y)).column(0);
		
		Vector v = new Vector(coefficients);
		System.out.println(X.multiplyBy(Matrix.columnMatrix(v))); //this should be closed to y vector
		
		return new PolynomialFunction(coefficients);
	}

	private Matrix getMatrix(DataSet dataSet, int range) {
		double[][] data = new double[dataSet.size()][range + 1];
		for (int i = 0; i < data.length; i++) {
			Double x = (Double) dataSet.getValue(i, 0);
			for (int j = 0; j <= range; j++) {
				data[i][j] = Math.pow(x, j);
			}
		}
		return new Matrix(data);
	}

	private Vector getYVector(DataSet dataSet) {
		double[] data = new double[dataSet.size()];
		for (int i = 0; i < data.length; i++) {
			data[i] = (Double) dataSet.getTargetAttributeValue(i);
		}
		return new Vector(data);
	}
	
	public static void main(String[] args) {
		DataSet dataSet = DataSetCsvParser.parseNumericDataSet("house_prices.txt", false);
		PolynomialFunction f = new UnivariateRegression().compute(dataSet, 8);
		System.out.println(f);
		new FunctionChart("Regression", f,dataSet.data());
	}
}
