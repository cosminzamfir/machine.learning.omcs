package ml.rl.mdp.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Build an {@link MDP} given an {@link FullTransitionInfoEnvironment}
 * @author Cosmin Zamfir
 *
 */
public class MDPBuilder {

	private static final Logger log = Logger.getLogger(MDPBuilder.class);
	private FullTransitionInfoEnvironment environment;
	private MDP mdp;
	/** Map State to whether it has been processed. Reason is to keep all States to be processed and remember visited States to avoid re-processing them */
	private Map<State, Boolean> cache = new HashMap<State, Boolean>();

	public MDPBuilder(FullTransitionInfoEnvironment environment) {
		super();
		this.environment = environment;
		mdp = MDP.instance();
	}

	public MDP build() {
		State state = environment.initialState();
		cache.put(state, false);
		while ((state = pollNextState()) != null) {

			if (!environment.isTerminal(state)) {
				processState(state);
				cache.put(state, true);
				log.debug("Processed non-terminal state and mark it as processed: " + state + ". Cache size: " + cache.size());
			} else {
				cache.put(state, true);
				log.debug("Mark terminal state as processed: " + ". Cache size: " + cache.size());
			}
		}
		log.info("Done. MDP number of states: " + mdp.getStates().size());
		return mdp;
	}

	/**
	 * Retrieve next unprocessed State from the {@link #cache}
	 * @return
	 */
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
		List<StateAction> stateActions = environment.getTransitions(state);
		for (StateAction stateAction : stateActions) {
			mdp.addStateAction(stateAction);
			stateAction.getTransitions().keySet().forEach((transition) -> cache.putIfAbsent(transition.getsPrime(), false));
		}
	}
}
