import linalg.Matrix;

public class MatrixTest {

	public static void main(String[] args) {
			Matrix m = new Matrix(5, 3, 90,60,90,90,90,30,60,60,60,60,60,90,30,30,30);
			System.out.println(m);
			System.out.println(m.varianceCovarianceMatrix());

	}
}
