package ml.rl.mdp.model;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Episode {

	private static NumberFormat nf = NumberFormat.getInstance();
	static {
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
	}

	private List<Transition> transitions = new ArrayList<Transition>();
	/**
	 * The count of this Episode instance in the learning process. Starts at 1,
	 * increment by one for each new episode
	 */
	private int count;
	private List<State> allStates = new ArrayList<>();

	public static Episode instance() {
		return new Episode();
	}

	public void addTransition(Transition transition) {
		if (!transitions.isEmpty() && !transition.getS().equals(transitions.get(transitions.size() - 1).getsPrime())) {
			throw new RuntimeException(
					"Cannot add transition. Initial state is not the same as final state of previous transition");
		}
		if (allStates.isEmpty()) {
			allStates.add(transition.getS());
		}
		transitions.add(transition);
		allStates.add(transition.getsPrime());

	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	/** zero-index based*/
	public Transition getTransition(int i) {
		return transitions.get(i);
	}

	/** zero-index based*/
	public double getStateValueAt(int i) {
		return getAllStates().get(i).getValue();
	}

	public int getCount() {
		return count;
	}

	public void initAllStates() {
		for (Transition transition : transitions) {
			allStates.add(transition.getS());
		}
		allStates.add(transitions.get(transitions.size() - 1).getsPrime());
	}

	public List<State> getAllStates() {
		return new ArrayList<>(allStates);
	}
	
	public List<State> getNonTerminalStates() {
		List<State> res = new ArrayList<>(allStates);
		res.remove(res.size()-1);
		return res;
	}

	public static List<State> getAllStates(List<Episode> episodes) {
		Set<State> res = new LinkedHashSet<>();
		episodes.forEach((episode) -> res.addAll(episode.getAllStates()));
		return new ArrayList<>(res);
	}

	public int index(Transition transition) {
		return transitions.indexOf(transition);
	}

	public boolean isLast(Transition transition) {
		return transitions.indexOf(transition) == transitions.size() - 1;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < transitions.size(); i++) {
			Transition t = transitions.get(i);
			if (i == 0) {
				sb.append(t.getS());
			}
			sb.append("->");
			sb.append(t.getsPrime());
		}
		return sb.toString();
	}

	public String printStateValues() {
		StringBuilder res = new StringBuilder();
		res.append("State values:");
		getAllStates().forEach((s) -> res.append("[" + nf.format(s.getValue()) + "]"));
		return res.toString();
	}

	public String printRewards() {
		StringBuilder res = new StringBuilder();
		res.append("Rewards:     ");
		transitions.forEach((t) -> res.append("[" + nf.format(t.getReward()) + "]"));
		res.append("  Sum:" + transitions.parallelStream().mapToDouble(Transition::getReward).sum());
		return res.toString();
	}

	public void setStateValueAt(int i, double value) {
		getAllStates().get(i).setValue(value);
	}

	public double getTotalReward() {
		return transitions.parallelStream().mapToDouble(Transition::getReward).sum();
	}

	public State getLastState() {
		return allStates.get(allStates.size() - 1);
	}
	
	public State getState(int i) {
		return allStates.get(i);
	}
	
	public int length() {
		return allStates.size();
	}

}
