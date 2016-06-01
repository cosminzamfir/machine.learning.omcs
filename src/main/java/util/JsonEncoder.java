package util;

import java.util.Iterator;
import java.util.List;

import ml.rl.mdp.model.MDP;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;
import ml.rl.mdp.model.Transition;

public class JsonEncoder {

	StringBuilder res = new StringBuilder();
	private MDP mdp;

	public JsonEncoder(MDP mdp) {
		this.mdp = mdp;
	}

	public String encode(double gamma) {
		openCurly();
		addField("gamma", gamma);
		addLine("\"states\": [");
		List<State> states = mdp.getNonTerminalStates();
		int numStates = states.size();
		for (int i = 0; i < states.size(); i++) {
			State state = states.get(i);
			encodeState(state);
			if (i < numStates - 1) {
				add(",");
			}
		}
		addLine("]");
		closeCurly();
		return res.toString();
	}

	public void encodeTransition(StateAction sa, Transition t, int index) {
		openCurly();
		addField("id", index);
		addField("probability", sa.getProbability(t));
		addField("reward", t.getReward());
		addField("to", t.getsPrime().getId(), true);
		closeCurly();
	}

	private void addField(String name, Object value, boolean lastField) {
		if (!lastField)
			res.append("\"" + name + "\"").append(":").append(value).append(",").append("\n");
		else
			res.append("\"" + name + "\"").append(":").append(value).append("\n");
	}

	public void encodeStateAction(StateAction sa, int id) {
		openCurly();
		addField("id", id);
		addLine("\"transitions\" : [");
		int numTransitions = sa.getTransitions().size();
		Iterator<Transition> iterator = sa.getTransitions().keySet().iterator();
		int index = 0;
		while (iterator.hasNext()) {
			Transition transition = (Transition) iterator.next();
			encodeTransition(sa, transition, index);
			if (index < numTransitions - 1) {
				add(",");
			}
			index++;
		}
		addLine("]");
		closeCurly();
	}

	public void encodeState(State state) {
		openCurly();
		addField("id", state.getId());
		addLine("\"actions\" : [");

		int numStateActions = mdp.getStateActions(state).size();
		Iterator<StateAction> iterator = mdp.getStateActions(state).iterator();
		int index = 0;
		while (iterator.hasNext()) {
			StateAction stateAction = (StateAction) iterator.next();
			encodeStateAction(stateAction, index);
			if (index < numStateActions - 1) {
				add(",");
			}
			index++;
		}
		addLine("]");
		closeCurly();
	}

	private void add(String string) {
		res.append(string);
	}

	private void addLine(String string) {
		res.append(string).append("\n");
	}

	private void closeCurly() {
		res.append("}");
	}

	private void addField(String name, Object value) {
		res.append("\"" + name + "\"").append(":").append(value).append(",").append("\n");
	}

	private void openCurly() {
		res.append("{").append("\n");
	}
}
