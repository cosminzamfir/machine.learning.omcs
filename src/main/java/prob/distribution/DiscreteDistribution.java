package prob.distribution;

public interface DiscreteDistribution {

	public void simulate(int samples);
	
	public Number compute(int x);
	
	public DiscreteDistributionResult<?> getResult();
}
