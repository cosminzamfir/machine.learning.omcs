package ml.rl.td;

import java.util.Observable;
import java.util.Observer;

import ml.rl.builder.EpisodeBuilder;
import ml.rl.mdp.model.Episode;
import ml.rl.mdp.model.Transition;
import ml.rl.td.view.EpisodeViewer;

public class TDLambdaRunner implements Observer {

	public static int updateSpeed;
	public static void main(String[] args) {
		System.out.println("Parameters: <lambda>,<gamma>,<alfa>,<updateSpeed>,<s1>,<r1>,<s2>,<r2>,<s3>,.....<sn>,<rn>,<sn+1>"
				+ "\n   - updateSpeed - in ms, gui speed update"
				+ "\n   - <si> - the state index: 1,2,3 etc"
				+ "\n   - <ri> - reward for going from <si> to <si+1>");
		double lambda = Double.valueOf(args[0]);
		double gamma = Double.valueOf(args[1]);
		double alfa= Double.valueOf(args[2]);
		updateSpeed = Integer.valueOf(args[3]);
		int[] transitions = new int[args.length - 4];
		for (int i = 0; i < transitions.length; i++) {
			transitions[i] = Integer.valueOf(args[i+4]);
		}
		Episode e = EpisodeBuilder.instance().addTransitions(transitions).build();
		TDLambdaRunner runner = new TDLambdaRunner(e, lambda);
		runner.run(gamma, alfa);
	}

	private Episode episode;
	private EpisodeViewer viewer;
	private double lambda;

	public TDLambdaRunner(Episode episode, double lambda) {
		super();
		this.episode = episode;
		this.lambda = lambda;
		this.viewer = new EpisodeViewer(episode);
	}

	public void run(double gamma, double alpha) {
		viewer = new EpisodeViewer(episode);
		TDLambdaEpisode algo = new TDLambdaEpisode(episode, lambda, gamma, alpha);
		algo.addObserver(this);
		viewer.display();
		algo.run();
	}

	@Override
	public void update(Observable o, Object arg) {
		Transition transition = (Transition) arg;
		viewer.markTransition(transition);
		viewer.waitForMouseClick("Click to see the state value updates");
		viewer.update(transition);
		viewer.waitForMouseClick("Click for next transition");
		if (episode.isLast(transition)) {
			viewer.waitForMouseClick("Finished. Click to see results:");
			viewer.markCompleted();
		}
	}
}
