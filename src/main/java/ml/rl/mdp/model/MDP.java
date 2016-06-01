package ml.rl.mdp.model;

import java.beans.XMLEncoder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import util.JsonEncoder;
import util.MLUtils;

public class MDP {

	private List<State> states = new ArrayList<>();
	private List<State> nonTerminalStates = new ArrayList<>();
	private Map<State, List<StateAction>> stateActions = new LinkedHashMap<State, List<StateAction>>();

	public MDP() {
	}

	public static MDP instance() {
		return new MDP();
	}

	public List<State> getStates() {
		return states;
	}

	public List<State> getNonTerminalStates() {
		return nonTerminalStates;
	}

	private void setNonTerminalStates() {
		//nonTerminalStates = states.stream().filter((s) -> (!isTerminal(s))).collect(Collectors.toList());
		nonTerminalStates.clear();
		for (State state : states) {
			if (!isTerminal(state)) {
				nonTerminalStates.add(state);
			}
		}
	}

	public List<StateAction> getStateActions(State state) {
		return stateActions.get(state);
	}

	public boolean isTerminal(State state) {
		return stateActions.get(state) == null || stateActions.get(state).isEmpty();
	}

	public void addStateAction(StateAction stateAction) {
		if(!states.contains(stateAction.getState())) {
			this.states.add(stateAction.getState());
		}
		for (State sprime : stateAction.getAllSprimes()) {
			if(!states.contains(sprime)) {
				this.states.add(sprime);
			}
		}
		if (!stateActions.containsKey(stateAction.getState())) {
			stateActions.put(stateAction.getState(), new ArrayList<>());
		}
		stateActions.get(stateAction.getState()).add(stateAction);
		setNonTerminalStates();
	}

	/** Generate initial policy - equal probabilities for all decisions */
	public MDPPolicy initialPolicy() {
		return MDPPolicy.initialPolicy(this);
	}

	/**
	 * Generate an initial Policy - choose randomly for each state a single StateAction with probability 1
	 * @return
	 */
	public MDPPolicy initialDeterministicPolicy() {
		return MDPPolicy.initialDeterministicPolicy(this);
	}

	/** Generate an Episode by sampling actions according to the given mdppolicy */
	public Episode generateEpisode(MDPPolicy policy) {
		Episode res = Episode.instance();
		State state = states.iterator().next();
		while (!isTerminal(state)) {
			StateAction stateAction = policy.generate(state);
			Transition transition = stateAction.generateTransition();
			res.addTransition(transition);
			state = transition.getsPrime();
		}
		return res;
	}

	public void resetStateValues() {
		states.forEach((s) -> s.setValue(0));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (State state : stateActions.keySet()) {
			if (index++ > 0) {
				sb.append("\n");
			}
			sb.append(state.toExtendedString());
			stateActions.get(state).forEach((stateAction) -> sb.append("\n").append(stateAction));
		}
		return sb.toString();
	}

	public Map<State, List<StateAction>> getStateActions() {
		return stateActions;
	}

	public void addSingleOutcomStateAction(State s, State sprime, double reward, String actionName) {
		StateAction stateAction = StateAction.instance(s, Action.instance(actionName));
		stateAction.addTransition(sprime, reward, 1);
		addStateAction(stateAction);
	}

	public void addDoubleOutcomStateAction(State s, State sprime1, State sprime2, double reward1, double reward2, double prob1, double prob2, String actionName) {
		StateAction stateAction = StateAction.instance(s, Action.instance(actionName));
		stateAction.addTransition(sprime1, reward1, prob1);
		stateAction.addTransition(sprime2, reward2, prob2);
		addStateAction(stateAction);
	}

	/**
	 * Add an {@link StateAction} with multiple stochastic outcomes 
	 * @param s the initial state
	 * @param sPrimes the List of arriving state
	 * @param rewards the List of rewards for each sPrime
	 * @param probabilities the probability to arrive to each of sPrime
	 * @param actionNamePrefix the name of the Action
	 */
	public void addMultipleOutcomStateAction(State s, List<State> sPrimes, List<Double> rewards, List<Double> probabilities, String actionName) {
		StateAction stateAction = StateAction.instance(s, Action.instance(actionName));
		for (int i = 0; i < sPrimes.size(); i++) {
			stateAction.addTransition(sPrimes.get(i), rewards.get(i), probabilities.get(i));
		}
		addStateAction(stateAction);
	}

	public void saveAsXML(String resourceName) {
		URL url = Thread.currentThread().getContextClassLoader().getResource("log4j.properties");
		File file;
		try {
			file = new File(url.toURI().getPath().replace("bin/log4j.properties", "src/main/resources/" + resourceName));
			FileOutputStream fos = new FileOutputStream(file);
			new XMLEncoder(fos).writeObject(this);
			fos.flush();
			fos.close();

		} catch (Exception e) {
			throw new RuntimeException("", e);
		}
	}

	public void saveAsString(String resourceName) {
		URL url = Thread.currentThread().getContextClassLoader().getResource("log4j.properties");
		File file;
		try {
			file = new File(url.toURI().getPath().replace("bin/log4j.properties", "src/main/resources/" + resourceName));
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(toString());
			writer.flush();
			writer.close();

		} catch (Exception e) {
			throw new RuntimeException("", e);
		}
	}

	public void saveAsJson(String resourceName, double gamma) {
		URL url = Thread.currentThread().getContextClassLoader().getResource("log4j.properties");
		File file;
		try {
			file = new File(url.toURI().getPath().replace("bin/log4j.properties", "src/main/resources/" + resourceName));
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(new JsonEncoder(this).encode(gamma));
			writer.flush();
			writer.close();

		} catch (Exception e) {
			throw new RuntimeException("", e);
		}
	}

	public Object printStateValues() {
		StringBuilder res = new StringBuilder();
		states.forEach((s) -> res.append(s + "=" + MLUtils.format6(s.getValue())).append(" "));
		return res.toString();
	}

}
