package ml.model.builder;

import util.MLUtils;
import ml.model.DataSet;
import ml.model.function.Function;
import ml.model.function.MultivariableFunction;

public class DataSetBuilder {

	private int width;
	private int heigth;
	private MultivariableFunction separationFunction;
	private double separationFunctionThreshold;
	private double minX = -1;
	private double maxX = 1;
	
	public DataSetBuilder heigth(int value) {
		this.heigth = value;
		return this;
	}

	public DataSetBuilder width(int value) {
		this.width = value;
		return this;
	}
	
	public DataSetBuilder separationFunction(MultivariableFunction f, double separationThreshold) {
		this.separationFunction = f;
		this.separationFunctionThreshold = separationThreshold;
		return this;
	}
	
	public DataSet build() {
		DataSet ds = new DataSet(width);
		for (int i = 0; i < heigth; i++) {
			double[] x = MLUtils.randomArray(width,minX, maxX);
			double fValue = separationFunction.evaluate(x);
			double y = fValue < separationFunctionThreshold ? -1 : 1;
			ds.add(y, x);
		}
		return ds;
	}

}
