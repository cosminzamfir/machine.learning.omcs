package linalg;

import java.text.MessageFormat;
import java.util.List;

import util.MLUtils;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class Vector {

	private double[] data;
	private int n;

	public static Vector column(Matrix m, int column) {
		return new Vector(m.column(column));
	}

	public static Vector row(Matrix m, int row) {
		return new Vector(m.getRow(row));
	}

	public static Vector zeroVector(int size) {
		double[] data = new double[size];
		return new Vector(data);
	}

	/** A random Vector with size elements, randomly choosed from the interval minValue..maxValue */
	public static Vector randomInstance(int size, double minValue, double maxValue) {
		double[] data = new double[size];
		for (int i = 0; i < data.length; i++) {
			data[i] = MLUtils.randomDouble(minValue, maxValue);
		}
		return new Vector(data);
	}
	
	/**
	 * A vector whose elements sum to one, with the given probability that any element = 0
	 */
	public static Vector randomMarkov(int size, double zeroProbability) {
		Vector res = randomInstance(size, 10, 20);
		for (int i = 0; i < size; i++) {
			if(Math.random() < zeroProbability) {
				res.set(i, 0);
			}
		}
		return res.multiply(1/res.sum());
	}

	public Vector(double... data) {
		this.data = data;
		this.n = data.length;
	}
	
	public int size() {
		return n;
	}

	public Vector add(Vector other) {
		double[] res = new double[n];
		for (int i = 0; i < n; i++) {
			res[i] = data[i] + other.data[i];
		}
		return new Vector(res);
	}

	public Matrix toRowMatrix() {
		return new Matrix(1, data.length, data);
	}

	public double sum() {
		double res = 0;
		for (double d : data) {
			res += d;
		}
		return res;
	}

	public double mean() {
		return sum() / data.length;
	}

	public Vector substract(Vector other) {
		double[] res = new double[n];
		for (int i = 0; i < n; i++) {
			res[i] = data[i] - other.data[i];
		}
		return new Vector(res);
	}

	public Vector multiply(double d) {
		double res[] = new double[n];
		for (int i = 0; i < n; i++) {
			res[i] = data[i] * d;
		}
		return new Vector(res);
	}

	public double dotProduct(Vector other) {
		if (this.data.length != other.data.length) {
			throw new RuntimeException(MessageFormat.format("Tried to multiply vectors of different length: {0} with length {1} and {2} with length {3}", this,
					this.data.length, other, other.data.length));
		}
		double res = 0;
		for (int i = 0; i < n; i++) {
			res += data[i] * other.data[i];
		}
		return res;
	}

	public double magnitude() {
		return Math.sqrt(this.dotProduct(this));
	}

	public double length() {
		return Math.sqrt(this.dotProduct(this));
	}

	public Matrix multiplyByTranspose() {
		return Matrix.columnMatrix(this).multiplyBy(Matrix.rowMatrix(this));
	}

	/**
	 * Return the projection of this instance on the given vector
	 * @param v
	 * @return
	 */
	public Vector projectionOn(Vector v) {
		Matrix projectionMatrix = v.projectionMatrix();
		return column(projectionMatrix.multiplyBy(Matrix.columnMatrix(this)), 0);
	}

	/**Return the orthogonal component of this instance on the given vector*/
	public Vector orthogonalComponentOn(Vector v) {
		return this.substract(projectionOn(v));
	}

	/**@return the projection matrix on this vector*/
	private Matrix projectionMatrix() {
		return multiplyByTranspose().divide(this.dotProduct(this));
	}

	public double get(int i) {
		return data[i];
	}

	public void set(int i, double value) {
		data[i] = value;
	}

	public void normalize() {
		double length = length();
		for (int i = 0; i < data.length; i++) {
			data[i] = data[i] / length;
		}
	}

	public double[] getData() {
		return data;
	}

	public Vector variationFromMean() {
		double[] res = new double[data.length];
		double mean = mean();
		for (int i = 0; i < res.length; i++) {
			res[i] = data[i] - mean;
		}
		return new Vector(res);
	}

	public double rootMeanSquaredError(Vector other) {
		double res = 0;
		for (int i = 0; i < data.length; i++) {
			res += Math.pow(data[i] - other.data[i], 2);
		}
		return Math.sqrt(res / size());
	}

	@Override
	public String toString() {
		return Matrix.columnMatrix(this).toString(false);
	}

	public static Vector average(List<Vector> vectors) {
		Vector res = Vector.zeroVector(vectors.get(0).size());
		for (Vector vector : vectors) {
			res = res.add(vector);
		}
		return res.multiply(1.0 / vectors.size());
	}

	public Vector insert(int index, double value) {
		double[] temp = new double[data.length + 1];
		for (int i = 0; i < index; i++) {
			temp[i] = data[i];
		}
		temp[index] = value;
		for (int i = index + 1; i < temp.length; i++) {
			temp[i] = data[i - 1];
		}
		return new Vector(temp);

	}

}
