package linalg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javafx.util.converter.IntegerStringConverter;
import util.MLUtils;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class Matrix {

	/** array of rows*/
	private double[][] data;
	/** number of rows*/
	int m;
	/** number of columns */
	int n;

	public static Matrix columnMatrix(Vector v) {
		double[][] data = new double[v.size()][1];
		for (int i = 0; i < v.size(); i++) {
			data[i][0] = v.get(i);
		}
		return new Matrix(data);
	}

	public static Matrix emtpyMatrix() {
		return new Matrix();
	}

	public void addRow(double[] row) {
		n = row.length;
		double[][] newData = new double[m + 1][n];
		for (int i = 0; i < m; i++) {
			newData[i] = data[i];
		}
		newData[m] = row;
		data = newData;
		m++;
	}

	public static Matrix rowMatrix(Vector v) {
		double[][] data = new double[1][v.size()];
		for (int i = 0; i < v.size(); i++) {
			data[0][i] = v.get(i);
		}
		return new Matrix(data);
	}

	public static Matrix fromColumnVectors(List<Vector> vectors) {
		int rows = vectors.get(0).size();
		int cols = vectors.size();
		double[][] data = new double[rows][cols];
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				data[row][col] = vectors.get(col).get(row);
			}
		}
		return new Matrix(data);

	}

	public static Matrix randomDouble(int m, int n, double minValue, double maxValue) {
		double[][] data = new double[m][n];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				data[i][j] = MLUtils.randomDouble(minValue, maxValue);
			}
		}
		return new Matrix(data);
	}

	public static Matrix randomInt(int m, int n, int maxValue) {
		double[][] data = new double[m][n];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				data[i][j] = MLUtils.random(maxValue);
			}
		}
		return new Matrix(data);
	}
	
	/**
	 * Create a matrix whose columns are positive numbers summing to one
	 * @param m
	 * @param n
	 * @param zeroProbability the probability that the value of any given element is equal to 0, i.e. largeer zeroProbability => sparser the matrix
	 * @return
	 */
	public static Matrix randomMarkov(int m, int n, double zeroProbability) {
		return fromColumnVectors(IntStream.range(0, n).mapToObj(x -> Vector.randomMarkov(m, zeroProbability)).collect(Collectors.toList()));
	}

	public Matrix(double[][] array) {
		super();
		this.data = array;
		this.m = array.length;
		this.n = array[0].length;
	}

	/**
	 * if values.length < n*m, fill remaining positions with 0
	 */
	public Matrix(int rows, int cols, double... values) {
		this.m = rows;
		this.n = cols;
		int index = 0;
		data = new double[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (values.length < index + 1) {
					data[i][j] = 0;
				} else {
					data[i][j] = values[index++];
				}
			}
		}
	}
	
	public Matrix normalizeByColumn() {
		List<Vector> colums = toColumnVectors();
		colums.stream().forEach(x -> x.normalize());
		return Matrix.fromColumnVectors(colums);
	}

	public Matrix() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * The identity matrix, with 1 on the diagonal
	 */
	public static Matrix identityMatrix(int n) {
		double[][] data = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				data[i][j] = i == j ? 1 : 0;
			}
		}
		return new Matrix(data);
	}

	public static Matrix joinHorizontal(Matrix m1, Matrix m2) {
		if (m1.m != m2.m) {
			throw new RuntimeException("Invalid matrices size for joinHorizontal");
		}
		double[][] res = new double[m1.m][m1.n + m2.n];
		for (int i = 0; i < res.length; i++) {
			for (int j = 0; j < res[i].length; j++) {
				res[i][j] = j < m1.n ? m1.get(i, j) : m2.get(i, j - m1.n);
			}
		}
		return new Matrix(res);
	}

	public Matrix clone() {
		double[][] copy = new double[m][n];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				copy[i][j] = data[i][j];
			}
		}
		return new Matrix(copy);
	}

	/**The max value of matrix elements */
	public double max() {
		double max = Double.NEGATIVE_INFINITY;
		for (double[] row : data) {
			for (double d : row) {
				if (d > max) {
					max = d;
				}
			}
		}
		return max;
	}

	public double[] row(int i) {
		return data[i];
	}

	public Vector rowVector(int i) {
		return new Vector(data[i]);
	}

	/**Returns the jth column as an array*/
	public double[] column(int j) {
		double[] res = new double[m];
		for (int i = 0; i < res.length; i++) {
			res[i] = data[i][j];
		}
		return res;
	}

	/**Returns the jth column as a vector*/
	public Vector columnVector(int j) {
		double[] res = new double[m];
		for (int i = 0; i < res.length; i++) {
			res[i] = data[i][j];
		}
		return new Vector(res);
	}

	public List<Vector> toColumnVectors() {
		List<Vector> res = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			res.add(new Vector(column(i)));
		}
		return res;
	}

	/**Substract multiplier*rowToSubstract from rowToSubstractFrom */
	public double[] substractRows(int rowToSubstractFrom, int rowToSubstract, double multiplier) {
		double[] res = new double[n];
		for (int i = 0; i < res.length; i++) {
			res[i] = data[rowToSubstractFrom][i] - multiplier * data[rowToSubstract][i];
		}
		return res;
	}

	/**
	 * Multiply all instances in the list: M1*M2*...*Mn
	 * @param matrices
	 * @return
	 */
	public static Matrix multiply(List<Matrix> matrices) {
		Matrix res = matrices.get(0);
		for (int i = 1; i < matrices.size(); i++) {
			res = res.multiplyBy(matrices.get(i));
		}
		return res;
	}

	/**
	 * Try to echange the row of the given zero pivot with another row underneath
	 * @return <code>true</code> if the exhahnge was possible, otherwise <code>false</code>
	 * 
	 */
	public boolean tryExchange(int pivotRow, int pivotCol) {
		boolean exchanged = false;
		for (int k = pivotRow + 1; k < data.length; k++) {
			if (data[k][pivotCol] != 0) {
				exchangeRows(pivotRow, k);
				exchanged = true;
				break;
			}
		}
		return exchanged;
	}

	/**
	 * Exchange row1 with row2
	 */
	public void exchangeRows(int row1, int row2) {
		double[] tmp = data[row1];
		data[row1] = data[row2];
		data[row2] = tmp;
		//System.out.println("Exchanging row " + (row1 + 1) + " with " + (row2 + 1) + " => \n" + this);
	}

	/**
	 * Multiply this instance with the given matrix
	 */
	public Matrix multiplyBy(Matrix other) {
		if (this.n != other.m) {
			throw new RuntimeException("Invalid matrices size for multiplication: [" + this.m + "," + this.n + "] and [" + other.m + "," + other.n + "]");
		}
		double[][] res = new double[this.m][other.n];
		//product A*B as a linear combination of the rows of B by the columns of A
		for (int thisRow = 0; thisRow < m; thisRow++) {
			for (int otherColumn = 0; otherColumn < other.n; otherColumn++) {
				res[thisRow][otherColumn] = this.rowVector(thisRow).dotProduct(other.columnVector(otherColumn));
			}
		}
		return new Matrix(res);
	}

	/**
	 * Return one vector as a linear combination of the rows of this instance with the given vector
	 * @param thisRow
	 * @return
	 */
	private double[] linearRowCombination(double[] v) {
		double[] res = multiply(v[0], row(0));
		for (int i = 1; i < v.length; i++) {
			res = add(res, multiply(v[i], row(i)));
		}
		return res;
	}

	/**
	 * @return the given vector multiplied with the given scalar
	 */
	private static double[] multiply(double multiplier, double[] row) {
		double[] res = new double[row.length];
		for (int i = 0; i < row.length; i++) {
			res[i] = multiplier * row[i];
		}
		return res;
	}

	/**Add two vectors*/
	private static double[] add(double[] row1, double row2[]) {
		double[] res = new double[row1.length];
		for (int i = 0; i < row1.length; i++) {
			res[i] = row1[i] + row2[i];
		}
		return res;
	}

	@Override
	public String toString() {
		int maxLength = maxLength() + 1;
		StringBuilder sb = new StringBuilder();
		for (double[] row : data) {
			for (double d : row) {
				//sb.append(MLUtils.printIntNumber(d, maxLength));
				sb.append(MLUtils.printNumber(d, maxLength));
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public String toString(boolean newLine) {
		int maxLength = maxLength() + 1;
		StringBuilder sb = new StringBuilder();
		for (double[] row : data) {
			for (double d : row) {
				sb.append(MLUtils.printNumber(d, maxLength));
			}
			if (newLine) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	/**The max length of the string representation of all matrix elements*/
	private int maxLength() {
		int res = 0;
		for (double[] row : data) {
			for (double d : row) {
				String s = MLUtils.format(d);
				if (s.length() > res) {
					res = s.length();
				}
			}
		}
		return res;
	}

	/**Get the element at row i, column j*/
	public double get(int i, int j) {
		return data[i][j];
	}

	public void set(int i, int j, double d) {
		data[i][j] = d;

	}

	public void setRow(int row, double[] value) {
		data[row] = value;
	}

	public double[] getRow(int i) {
		return data[i];
	}

	/**Multiply the row at given index by the multiplier*/
	public void multiplyRow(int i, double multiplier) {
		setRow(i, multiply(multiplier, getRow(i)));
	}

	/**Return a cut from this matrix as a new matrix instance*/
	public Matrix subMatrix(int topRow, int leftCol, int bottomRow, int rightCol) {
		int rows = bottomRow - topRow + 1;
		int cols = rightCol - leftCol + 1;
		double[][] res = new double[rows][cols];
		for (int i = topRow; i <= bottomRow; i++) {
			for (int j = leftCol; j <= rightCol; j++) {
				res[i - topRow][j - leftCol] = data[i][j];
			}
		}
		return new Matrix(res);

	}

	public Matrix transpose() {
		double[][] res = new double[n][m];
		for (int i = 0; i < res.length; i++) {
			for (int j = 0; j < res[i].length; j++) {
				res[i][j] = data[j][i];
			}
		}
		return new Matrix(res);
	}

	public int getFirstNonZeroColumn(int row) {
		for (int i = 0; i < n; i++) {
			if (data[row][i] != 0) {
				return i;
			}
		}
		return -1;
	}

	public Matrix inverse() {
		return new Solver().computeInverse(this);
	}

	/**
	 * @return the projection matrix for the hyperplane represented by this instance
	 */
	public Matrix projectionMatrix() {
		return this.
				multiplyBy(this.transpose().multiplyBy(this).inverse()).
				multiplyBy(this.transpose());
	}

	public Matrix projectOn(Matrix hyperPlane) {
		if (n != 1) {
			throw new RuntimeException("Only vectors(represented as a column matrix) can be projected on a hyperplane");
		}
		return hyperPlane.projectionMatrix().multiplyBy(this);
	}

	public Matrix divide(double divisor) {
		double res[][] = new double[m][n];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				res[i][j] = data[i][j] / divisor;
			}
		}
		return new Matrix(res);
	}

	public double determinant() {
		Solver solver = new Solver();
		Matrix U = solver.solve(this);
		double res = 1;
		for (int i = 0; i < n; i++) {
			res = res * U.get(i, i);
		}
		return solver.getRowExchanges() % 2 == 0 ? res : -res;

	}

	public Matrix multiplyByScalar(double scalar) {
		double[][] copy = new double[m][n];
		for (int i = 0; i < copy.length; i++) {
			for (int j = 0; j < copy[i].length; j++) {
				copy[i][j] = data[i][j] * scalar;
			}
		}
		return new Matrix(copy);

	}

	/**
	 * The variance/covariance matrix - this Matrix instance is interpreted as: rows = observations; columns = features
	 * @return
	 */
	public Matrix varianceCovarianceMatrix() {
		List<Vector> columns = toColumnVectors();
		List<Vector> diffs = new ArrayList<>();
		for (Vector column : columns) {
			diffs.add(column.variationFromMean());
		}
		Matrix variationMatrix = Matrix.fromColumnVectors(diffs);
		return variationMatrix.transpose().multiplyBy(variationMatrix).divide(variationMatrix.m);
	}

	public Matrix copy() {
		return new Matrix(data.clone());
	}

	public double sum() {
		double res = 0;
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				res += data[i][j];
			}
		}
		return res;
	}

	public double norm() {
		double res = 0;
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				res += data[i][j] * data[i][j];
			}
		}
		return res;
	}

	public Matrix substract(Matrix other) {
		double[][] res = new double[m][n];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				res[i][j] = data[i][j] - other.data[i][j]; 
			}
		}
		return new Matrix(res);
	}

	public Matrix add(Matrix other) {
		double[][] res = new double[m][n];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				res[i][j] = data[i][j] + other.data[i][j]; 
			}
		}
		return new Matrix(res);
	}

}
