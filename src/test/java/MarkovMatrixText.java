import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import linalg.Matrix;
import linalg.Vector;

public class MarkovMatrixText {

	@Test
	public void test1() throws Exception {
		int experiments = 2;
		int dataSetSize = 6;
		int iterations = 100;
		//Matrix m = Matrix.randomMarkov(dataSetSize, dataSetSize, 0.90); //0.5 = probability that any element of the matrix = 0
		Matrix m = new Matrix(6, 6, 0, 0, 0, 1.0 / 3, 1.0 / 3, 1.0 / 3, 0, 0, 0, 0.5, 0.5, 0, 0, 0, 0, 1, 0, 0, 1.0 / 3, 1.0 / 3, 1.0 / 3, 0, 0, 0, 0.5, 0.5, 0,
				0, 0, 0, 1, 0, 0, 0, 0, 0);
		System.out.println(m);

		List<Matrix> finalStates = new ArrayList<>();
		for (int experimentInd = 0; experimentInd < experiments; experimentInd++) {
			Matrix distribution = Matrix.columnMatrix(Vector.randomMarkov(dataSetSize, 0));
			Matrix prevDistribution = distribution.copy();
			for (int iter = 0; iter < iterations; iter++) {
				distribution = m.multiplyBy(prevDistribution);
				//System.out.println("Iteration: " + iter + "; deltaNorm: " + distribution.substract(prevDistribution).norm());
				prevDistribution = distribution;
			}
			finalStates.add(distribution);
			System.out.println(distribution);
		}

		for (int i = 1; i < finalStates.size(); i++) {
			Assert.assertEquals(0, finalStates.get(i).substract(finalStates.get(i - 1)).norm(), 0.00000000001);
		}
	}

	@Test
	public void test2() throws Exception {
		Matrix m = Matrix.randomMarkov(10, 10, 0.1);
		Matrix inv = m.inverse();
		Matrix prod = m.multiplyBy(inv);
		System.out.println(m);
		System.out.println(inv);
		System.out.println(prod);
	}

	@Test
	public void test3() throws Exception {
		int iterations = 100;
		double c = 0;
		Matrix m = new Matrix(6, 6, 
				0, 0, 0, 1, 1, 1, 
				0, 0, 0, 1, 1, 0, 
				0, 0, 0, 1, 0, 0, 
				1, 1, 1, 0, 0, 0, 
				1, 1, 0, 0, 0, 0, 
				1, 0, 0, 0, 0, 0).normalizeByColumn();
		//Matrix u = Matrix.columnMatrix(new Vector(1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6));
		
		Matrix u = Matrix.randomMarkov(6, 1, 0);
		
		Matrix q = Matrix.columnMatrix(new Vector(0, 1.0, 0, 0, 0, 0));
		System.out.println("      Pat1  Pat2  Pat3  icd91  icd92  icd93");
		System.out.println("graph = \n" + m);
		System.out.println("u = " + u.transpose());
		System.out.println("q = " + q.transpose());

		for (int iter = 0; iter < iterations; iter++) {
			u = m.multiplyBy(u);
			u = getNewU(u,c,q);
			System.out.println("u:" + u.transpose());
		}

	}

	private Matrix getNewU(Matrix u, double c, Matrix q) {
		return u.multiplyByScalar(1 - c).add(q.multiplyByScalar(c));
	}
}
