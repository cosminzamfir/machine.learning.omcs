package ml.stat;

import java.util.Arrays;
import java.util.List;

import ml.model.function.Function;
import ml.stat.kernels.GaussianKernel;
import ml.stat.kernels.TriangularKernel;
import ml.stat.kernels.TricubeKernel;
import ml.stat.kernels.UniformKernel;
import prob.chart.ChartBuilder;
import prob.chart.PlotType;
import util.DoubleHolder;

/**
 * https://en.wikipedia.org/wiki/Kernel_density_estimation
 * @author Cosmin Zamfir
 *
 */
public class KernelDensityEstimator implements Function {
	
	private List<Double> data;
	private Function kernel;
	private double bandWidth;
	
	public KernelDensityEstimator(List<Double> data, Function kernel, double bandWidth) {
		super();
		this.data = data;
		this.kernel = kernel;
		this.bandWidth = bandWidth;
	}
	
	/**
	 * Average the contributions of the kernel functions of each data point to the given x
	 */
	@Override
	public double evaluate(double x) {
		DoubleHolder res = new DoubleHolder(0);
		data.forEach((d) -> res.add(1/bandWidth * kernel.evaluate((x - d)/bandWidth)));
		return res.get() / data.size();
	}
	
	@Override
	public String toString() {
		return data.size() + " samples; " + kernel.getClass().getSimpleName();
	}
	
	public static void main(String[] args) {
		List<Double> data = Arrays.asList(1d,1d,1d,1d,1d,1d,1d,1d,1d,1d,1d,1d,
				2d,2d,2d,2d,2d,3d,3d,4d,4d,5d,5d,5d,5d,5d,5d,5d,5d,5d,5d,6d,6d,6d,6d,6d,6d,6d,6d,6d,6d,6d,6d,6d,6d,6d,6d,6d);
		double bandWidth = 1;

		KernelDensityEstimator gaussianEst = new KernelDensityEstimator(data, new GaussianKernel(), bandWidth);
		KernelDensityEstimator uniformEst = new KernelDensityEstimator(data, new UniformKernel(), bandWidth);
		KernelDensityEstimator triangularEst = new KernelDensityEstimator(data, new TriangularKernel(), bandWidth);
		KernelDensityEstimator tricubeEst = new KernelDensityEstimator(data, new TricubeKernel(), bandWidth);
		new ChartBuilder("KDE", "x", "p(x)")
			.add(gaussianEst, -10, 10, 1000, "gaussian", PlotType.Line)
			//.add(uniformEst, -10, 10, 1000, "uniform", PlotType.Line)
			//.add(triangularEst, -10, 10, 1000, "triangular", PlotType.Line)
			.add(tricubeEst, -10, 10, 1000, "tricube", PlotType.Line)
			.build();
	}
	
}
