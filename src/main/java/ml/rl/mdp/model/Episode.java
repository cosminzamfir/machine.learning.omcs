package ml.rl.mdp.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Episode {

	private List<Transition> transitions = new ArrayList<Transition>();
	/**The count of this Episode instance in the learning process. Starts at 1, increment by one for each new episode*/
	private int count;
	private List<State> allStates = new ArrayList<>();

	public void addTransition(Transition transition) {
		if (!transitions.isEmpty() && !transition.getS().equals(transitions.get(transitions.size() - 1).getsPrime())) {
			throw new RuntimeException("Cannot add transition. Initial state is not the same as final state of previous transition");
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
		return allStates;
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
			Transition t= transitions.get(i);
			if(i==0) {
				sb.append(t.getS());
			}
			sb.append("->");
			sb.append(t.getsPrime());
		}
		return sb.toString();
	}
	
	public void printStateValues() {
		getAllStates().forEach((s) -> System.out.print("[" + s + ":" + s.getValue() + "]"));
		System.out.println();
	}


}
