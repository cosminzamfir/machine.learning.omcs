package prob.distribution;

public abstract class AbstractContinuousDistribution implements ContinousDistribution {

	protected ContinuousDistributionResult result;

	public AbstractContinuousDistribution() {
		super();
		result = new ContinuousDistributionResult();
	}
	
	@Override
	public void simulate(int samples) {
		for (int i = 0; i < samples; i++) {
			result.add(simulateNext());
		}

	}

	protected abstract double simulateNext();
	
	@Override
	public ContinuousDistributionResult getResult() {
		return result;
	}
	
	

}
