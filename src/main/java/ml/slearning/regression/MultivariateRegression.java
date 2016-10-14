package ml.slearning.regression;

import linalg.Matrix;
import linalg.Vector;
import ml.model.DataSet;
import ml.model.Observation;
import ml.model.function.PolynomialMultivariableFunction;
import ml.utils.DataSetCsvParser;

/**
 * Input: (x1,x2,...,xn) mapped to y's
 * Output: a {@link PolynomialMultivariableFunction} which minimizes the squared error
 * In matrix form (ai are the grade n function coeficients for a {@link DataSet} with m {@link Observation}s)
 * <p>| 1  x1  x1^2   ...  x1^n | * |a1| = |y1|  		
 * <p>| 1  x2  x2^2   ...  x2^n |   |a2|   |y2|
 * <p> ...						     ...   ...
 * <p>| 1  xm  xm^2   ...  xm^n |   |an|   |yn|
 * As the above above has no exact solution, compute vector a' which solves X*a' = projection of y on column space of X
 * The vector is: a' = inverse(transpose(X) * X) * transpose(X) * y
 * @see http://ocw.mit.edu/courses/mathematics/18-06sc-linear-algebra-fall-2011/least-squares-determinants-and-eigenvalues/projections-onto-subspaces/MIT18_06SCF11_Ses2.2sum.pdf
 * @author Cosmin Zamfir
 *
 */
public class MultivariateRegression {

	public PolynomialMultivariableFunction compute(DataSet dataSet, int range) {
		Matrix X = getMatrix(dataSet, range);
		Vector y = getYVector(dataSet);
		Matrix Xt = X.transpose();
		double coefficients[] = Xt.multiplyBy(X).inverse().multiplyBy(Xt).multiplyBy(Matrix.columnMatrix(y)).column(0);
		
		Vector v = new Vector(coefficients);
		System.out.println(X.multiplyBy(Matrix.columnMatrix(v))); //this should be closed to y vector
		
		double[][] c = new double[1][coefficients.length];
		c[0] = coefficients;
		PolynomialMultivariableFunction res = new PolynomialMultivariableFunction(0,c);
		return res;
	}

	/** Each row of the matrix will be: 1 x1 x1^2 ... x1^n 1 x2 x2^2 ... x2^n .... */
	private Matrix getMatrix(DataSet dataSet, int range) {
		int width = dataSet.getAttributes().size();
		int height = dataSet.size();
		double[][] data = new double[height][width*range + 1];
		for (int i = 0; i < data.length; i++) {
			data[i][0] = 1;
			int index = 1;
			for (int j = 0; j < width; j++) {
				Double x = (Double) dataSet.getValue(i, j);
				for (int k = 1; k <= range; k++) {
					data[i][index++] = Math.pow(x,k);
				}
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
		DataSet dataSet = DataSetCsvParser.parseNumericDataSet("forest_fires.csv", false);
		PolynomialMultivariableFunction f = new MultivariateRegression().compute(dataSet, 3);
		System.out.println(f);
		
	}
}
