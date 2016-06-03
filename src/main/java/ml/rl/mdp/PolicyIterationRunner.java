package ml.rl.mdp;

import java.util.Observable;
import java.util.Observer;

import util.MLUtils;
import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.MDPPolicy;
import ml.rl.mdp.view.MDPViewer;

public class PolicyIterationRunner implements Observer {

	private MDPViewer mdpViewer;
	private PolicyIteration pi;
	private long slideShowDelay = 1000;

	public void setSlideShowDelay(long slideShowDelay) {
		this.slideShowDelay = slideShowDelay;
	}

	public PolicyIterationRunner(MDP mdp) {
		super();
		this.mdpViewer = MDPViewer.instance(mdp);
		mdpViewer.display();
		pi = new PolicyIteration(mdp);
	}

	public void setGamma(double gamma) {
		pi.setGamma(gamma);
	}

	public void run() {
		pi.addObserver(this);
		pi.run();
		mdpViewer.setCompleted();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof MDPPolicy) {
			MDPPolicy policy = (MDPPolicy) arg;
			mdpViewer.markPolicy(policy);
			MLUtils.readFromConsole("Marked the latest policy. Press any key to see the state values");
		} else if (arg instanceof MDP) {
			mdpViewer.updateStateVerticesValues();
			MLUtils.readFromConsole("State values updated. Press any key to see the new policy");
		}
	}

}
