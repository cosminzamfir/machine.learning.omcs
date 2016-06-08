package ml.rl.mdp.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ml.rl.qlearning.CompleteInfoEnvironment;

import org.apache.log4j.Logger;

/**
 * Build an {@link MDP} given an {@link CompleteInfoEnvironment} and an initial {@link State}
 * @author eh2zamf
 *
 */
public class MDPBuilder {

	private static final Logger log = Logger.getLogger(MDPBuilder.class);
	private State initialState;
	private CompleteInfoEnvironment environment;
	private MDP mdp;
	/** Map State to whether it has been processed. Reason is to remember visited States to avoid re-processing them */
	private Map<State, Boolean> cache = new HashMap<State, Boolean>();

	public MDPBuilder(CompleteInfoEnvironment environment) {
		super();
		this.initialState = environment.initialState();
		this.environment = environment;
		mdp = MDP.instance();
	}

	public MDP build() {
		State state = initialState;
		cache.put(state, false);
		while ((state = pollNextState()) != null) {

			if (!environment.isTerminal(state)) {
				processState(state);
				cache.put(state, true);
				log.info("Processed non-terminal state and set it as processed: " + state + ". Cache size: " + cache.size());
			} else {
				cache.put(state, true);
				log.info("Set terminal state as processed: " + ". Cache size: " + cache.size());
			}
		}
		return mdp;
	}

	private State pollNextState() {
		for (Iterator<State> iterator = cache.keySet().iterator(); iterator.hasNext();) {
			State state = iterator.next();
			if (cache.get(state) == false) {
				return state;
			}
		}
		return null;
	}

	private void processState(State state) {
		List<StateAction> stateActions = environment.getStateActions(state);
		for (StateAction stateAction : stateActions) {
			mdp.addStateAction(stateAction);
			stateAction.getTransitions().keySet().forEach((transition) -> cache.putIfAbsent(transition.getsPrime(), false));
		}
	}
}
