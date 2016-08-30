package prob.distribution;

public interface ContinousDistribution {

	public void simulate(int samples);
	
	public double compute(double x);
	
	public ContinuousDistributionResult getResult();
}
