package ml.rl.mdp;
import java.util.Observable;
import java.util.Observer;

import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;
import ml.rl.td.view.MDPViewer;


public class ValueIterationRunner implements Observer {

	private MDP mdp;
	private MDPViewer mdpViewer;
	private ValueIteration vi;

	public ValueIterationRunner(MDP mdp) {
		super();
		this.mdp = mdp;
		this.mdpViewer = MDPViewer.instance(mdp);
		mdpViewer.display();
		vi = new ValueIteration(mdp);
	}
	
	public void run() {
		vi.addObserver(this);
		vi.run();
		mdpViewer.setCompleted();
	}

	@Override
	public void update(Observable o, Object arg) {
		State s = (State) arg;
		mdpViewer.update(s);
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			throw new RuntimeException("",e);
		}
	}

	public void setGamma(double d) {
		vi.setGamma(d);
	}
	
	
}
