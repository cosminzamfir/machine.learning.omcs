import linalg.Matrix;
import linalg.Vector;

public class MatrixTest {

	public static void main(String[] args) {
			Matrix m = new Matrix(2,2,0.18,0.32,1,1);
			Vector b = new Vector(0.25,1);
			System.out.println(m);
			System.out.println(b);
			Matrix res = m.inverse().multiplyBy(Matrix.columnMatrix(b));
			System.out.println(res);

	}
}
