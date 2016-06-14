package linalg;

import java.util.ArrayList;
import java.util.List;

public class GramSchimdt {

	/**
	 * Transform the col vectors of A into orthonormal vectors
	 * Steps:
	 * <ul>
	 * <li>Take the fist vector columns as is
	 * <li>At each step, transform the current col vector to a vector perpendicular to all other 'so far' vectors, by substracting from the current vector the projections on
	 * all 'so far' vectors
	 * <li>At the end, diveide each vector by its lengths to have it normalized
	 * <ul> 
	 * @return the matrix Q containing the orthonormal vectors as columns
	 * @author Cosmin Zamfir
	 */
	public Matrix orthonormal(Matrix A) {
		return processInternal(A, true);
	}

	public Matrix orthogonal(Matrix A) {
		return processInternal(A, false);
	}

	private Matrix processInternal(Matrix A, boolean normalize) {
		List<Vector> res = new ArrayList<Vector>();
		res.add(new Vector(A.column(0)));
		for (int i = 1; i < A.n; i++) {
			Vector current = new Vector(A.column(i));
			for (Vector vector : res) {
				current = current.substract(current.projectionOn(vector));
			}
			res.add(current);
		}
		if (normalize) {
			for (Vector vector : res) {
				vector.normalize();
			}
		}
		return Matrix.fromColumnVectors(res);

	}
}
