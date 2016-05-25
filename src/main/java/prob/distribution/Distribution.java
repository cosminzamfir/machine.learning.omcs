package prob.distribution;

public interface Distribution {

	public void simulate(int samples);
	
	public Number compute(int x);
	
	public DiscreteDistributionResult getResult();
}
