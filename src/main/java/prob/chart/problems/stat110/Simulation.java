package prob.chart.problems.stat110;

public abstract class Simulation {

	private int trials = 1000;

	public void run() {
		int successes = 0;
		int n = trials;
		for (int i = 0; i < n; i++) {
			Boolean res = simulate();
			if(res == null) {
				n++;
				continue;
			}
			if (res) {
				successes++;
			}
		}
		double simulated = (double) successes / trials;
		double calculated = computedResult();
		System.out.println(this.getClass().getSimpleName() +
				"\n  -Simulated Result:   " + simulated +
				"\n  -Computed Result:    " + calculated +
				"\n  -Difference:         " + (simulated - calculated));
	}

	//** Null result does not count*/
	protected abstract Boolean simulate();

	protected abstract double computedResult();
}
