package linalg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Matrix solver - statefull
 * @author Cosmin Zamfir
 *
 */
public class Solver {

	private static final Logger log = Logger.getLogger(Solver.class);
	private List<Matrix> eliminationMatrices = new ArrayList<>();
	private Matrix U;
	private int rowExchanges;

	/**
	 * Run the elimination process on this instance to produce an upper triangular matrix
	 */
	public Matrix solve(Matrix input) {
		eliminationMatrices.clear();
		rowExchanges = 0;
		Matrix m = input.clone();
		this.eliminationMatrices.clear();
		for (int i = 0; i < m.m; i++) {
			double pivot = m.get(i, i);
			if (pivot == 0) {
				boolean exchanged = m.tryExchange(i, i);
				if (!exchanged) {
					throw new RuntimeException("Zero pivot at [" + (i + 1) + "," + (i + 1) + "]. No posibility for row exchange");
				}
				rowExchanges++;
			}
			for (int j = i + 1; j < m.m; j++) {
				if (m.get(j, i) != 0) {
					eliminate(m, i, i, j);
				}
			}
		}
		printEliminationMatrices(input);
		U = m;
		return m;
	}

	public Matrix getU() {
		return U;
	}

	/**
	 * Zeroze the element under(gauss)/above(jordan) the pivot on the given row  
	 * @param m 
	 * @param pivotRow the row of the pivot
	 * @param pivotCol the col of the pivot
	 * @param row the row of the element
	 */
	public void eliminate(Matrix m, int pivotRow, int pivotCol, int row) {
		double toZeroize = m.get(row, pivotCol);
		double pivot = m.get(pivotRow, pivotCol);
		double multiplier = toZeroize / pivot;
		eliminationMatrices.add(getEliminationMatrix(m, pivotRow, pivotCol, row));
		m.setRow(row, m.substractRows(row, pivotRow, multiplier));
		if (log.isTraceEnabled()) {
			log.trace("Pivot:[" + (pivotRow + 1) + "," + (pivotCol + 1) + "]=" + pivot + ";row " + (row + 1) + ";multiplier:" + multiplier + "=>\n" + m);
		}
	}

	/**
	 * Print the elimination matrices in the order: E21,E31,E32... and their product in the order: E32*E31*E31
	 * @param input 
	 */
	public void printEliminationMatrices(Matrix input) {
		for (Matrix e : eliminationMatrices) {
			if (log.isTraceEnabled()) {
				log.trace(e);
			}
		}
		if (eliminationMatrices.isEmpty()) {
			return;
		}
		Collections.reverse(eliminationMatrices);
		if (log.isTraceEnabled()) {
			log.trace("L: \n" + Matrix.multiply(eliminationMatrices));
			log.trace("Product of Es * M:\n" + Matrix.multiply(eliminationMatrices).multiplyBy(input));
		}
	}

	/**Get the elimination matrix - the matrix which when multiplied with this instance zeroize the element in the given row under the given pivot
	 * @param m */
	public Matrix getEliminationMatrix(Matrix m, int pivotRow, int pivotCol, int row) {
		Matrix res = Matrix.identityMatrix(m.m);
		double multiplier = m.get(row, pivotCol) / m.get(pivotRow, pivotCol);
		res.set(row, pivotRow, -multiplier);
		return res;
	}

	/**
	 * Gauss-Jordan algorithm: given a matrix A, compute its inverse.
	 * Steps: 
	 * <ol>
	 * <li>Start with the augmented matrix [A I]
	 * <li>Run elimination process on the augmented matrix
	 * <li>Run backward elimination to zeroize the elements above the pivots
	 * <li>Divide by the pivots
	 * </ol>
	 * The result is a new matrix: [I K]. The process above can be represented as a multiplication wiht some matrix M.
	 * We have then: [MA MI] = [I K] => M is the inverse of A (form MA=I] and K is exactly M (from MI=K) => the right side matrix is
	 * the required inverse
	 * @param m the matrix
	 * @return the inverse
	 */
	public Matrix computeInverse(Matrix m) {
		Matrix ext = Matrix.joinHorizontal(m, Matrix.identityMatrix(m.m));
		ext = solve(ext);

		//eliminate elements above the pivots
		for (int i = m.m - 1; i >= 1; i--) {
			for (int j = i - 1; j >= 0; j--) {
				eliminate(ext, i, i, j);
			}
		}

		//divide by the pivots
		for (int i = 0; i < m.m; i++) {
			ext.multiplyRow(i, 1 / ext.get(i, i));
			//System.out.println("Divide by pivot on (" + i + ",i) => \n" + ext);
		}

		return ext.subMatrix(0, m.n, ext.m - 1, ext.n - 1);
	}

	/**
	 * Compute the reduced row echelon form of a m*n matrix
	 * Short description of the algorithm
	 * <ul>
	 * <li>Perform an 'enhanced' Gauss elimination: if the pivot on the current row is zero and no row exchange is possible, continue with the next row
	 * <li>Eliminate the elements about the pivots
	 * <li>Make the pivots equal to 1, by dividing each relevant row to the pivot.
	 * </ul>
	 * The columns with non zero pivots represent 'pivot variables'. The columns with zero pivots represent 'free variable'.
	 * The free variables can have any value. For each value combination of the free variables, there is a solution to the system.
	 *
	 * @param m the initial matrix 
	 * @return the reduced row echelon form matrix
	 */
	public Matrix rref(Matrix m) {
		int lastPivotColumn = 0;
		for (int i = 0; i < m.m; i++) {
			if (lastPivotColumn > m.n - 1) {
				break;
			}

			double pivot = m.get(i, lastPivotColumn);

			while (pivot == 0 && !m.tryExchange(i, lastPivotColumn) && lastPivotColumn < m.m - 1) {
				lastPivotColumn++;
				pivot = m.get(i, lastPivotColumn);
			}

			for (int j = i + 1; j < m.m; j++) {
				if (m.get(j, lastPivotColumn) != 0) {
					eliminate(m, i, lastPivotColumn, j);
					if (log.isTraceEnabled()) {
						log.trace(m);
					}
				}
			}
			lastPivotColumn++;
		}

		//eliminate elements above the pivots
		for (int i = m.m - 1; i >= 1; i--) {
			int pivotColumn = m.getFirstNonZeroColumn(i);
			if (pivotColumn >= 0) {
				for (int j = i - 1; j >= 0; j--) {
					eliminate(m, i, pivotColumn, j);
					if (log.isTraceEnabled()) {
						log.trace(m);
					}
				}
			}
		}

		//divide each row by the pivot
		for (int i = 0; i < m.m; i++) {
			int pivotColumn = m.getFirstNonZeroColumn(i);
			if (pivotColumn >= 0) {
				m.multiplyRow(i, 1 / m.get(i, pivotColumn));
				if (log.isTraceEnabled()) {
					log.trace(m);
				}
			}
		}
		return m;
	}

	public int getRowExchanges() {
		return rowExchanges;
	}

}
