package ml.rl.hw5;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ml.rl.mdp.model.Action;
import ml.rl.mdp.model.FullTransitionInfoEnvironment;
import ml.rl.mdp.model.State;
import ml.rl.mdp.model.StateAction;
import prob.distribution.DiscreteDistributionResult;

/**
 * A one dimensional environment with x in [0..xMax]
 * <p>
 * The interval in divided in numTerrainTypes terrain types of equal length
 * <p>
 * There are numActions possible actions
 * <p>
 * Given the terrain state and action, the mean and standard deviation of the position change is given => a two-dimensional array of size [numTerrainTypes, numActions] 
 * @author Cosmin Zamfir
 *
 */
public class HW5Environment implements FullTransitionInfoEnvironment {

	public static final String POSITION = "pos";
	public static final String TERRAIN_TYPE = "terrainType";
	Random random = new Random();
	/** How many times to sample from the gaussian distribution of position change in order to build the transition matrix for each state-action pair */
	private int samplingSize = 10000;
	
	private double xMax = 1.0;
	private int numTerrainTypes = 5;
	private int numActions = 8;
	/** In how many descrete intervals (States) to divide. An initialState + an absorbingState will be added, besides the numStates States*/
	private int numStates = 100;
	
	
	private double[][] movementMean;
	private double[][] movementSD;
	private List<Action> actions;
	private List<State> states;
	private State initialState;
	private State terminalState;
	
	
	public HW5Environment(int numStates, double[][] movementMean, double[][] movementSD) {
		super();
		this.movementMean = movementMean;
		this.movementSD = movementSD;
		this.numStates = numStates;
		initActions();
		initStates();
	}

	private void initActions() {
		actions = new ArrayList<Action>();
		for (int i = 0; i < numActions; i++) {
			actions.add(Action.instance(String.valueOf(i)));
		}
	}
	
	private void initStates() {
		states = new ArrayList<>();
		double intervalLength = xMax / numStates;
		double terrainTypeIntervalSize = 1.0 / numTerrainTypes;
		for (int i = 0; i < numStates; i++) {
			double pos = (i*intervalLength + (i+1)*intervalLength) / 2; //position is the middle of the interval.
			int terrainType = (int) Math.floor(pos/terrainTypeIntervalSize);
			State state = State.instance(i+1);
			state.set(POSITION, pos);
			state.set(TERRAIN_TYPE, terrainType);
			states.add(state);
		}
		initialState = State.instance(0);
		initialState.set(POSITION, 0.0);
		initialState.set(TERRAIN_TYPE, 0);
		states.add(0, initialState);

		terminalState = State.instance(numStates + 1);
		terminalState.set(POSITION, xMax);
		terminalState.set(TERRAIN_TYPE, numTerrainTypes - 1);
		states.add(terminalState);
	}

	@Override
	public boolean isTerminal(State state) {
		return state == terminalState;
	}

	@Override
	public State initialState() {
		return initialState;
	}

	@Override
	public List<StateAction> getTransitions(State state) {
		List<StateAction> res = new ArrayList<>();
		for (Action action : actions) {
			res.add(getStateAction(state,action));
		}
		return res;
	}

	/** Build the {@link StateAction}, ie including the transition probability matrix
	 * for the given Action in the given State, by sampling {@link #samplingSize} samples from the gaussian distribution
	 * given by {@link #movementMean} and {@link #movementSD}
	 */
	private StateAction getStateAction(State state, Action action) {
		StateAction res = StateAction.instance(state, action);
		double position = state.getDouble(POSITION);
		int terrainTypeIndex = state.getInteger(TERRAIN_TYPE);
		int actionIndex = Integer.valueOf(action.getName());

		double mean = movementMean[terrainTypeIndex][actionIndex];
		double sd = movementSD[terrainTypeIndex][actionIndex];
		DiscreteDistributionResult<State> distribution = new DiscreteDistributionResult<State>();

		for (int i = 0; i < samplingSize; i++) {
			double positionPrime = position + random.nextGaussian() * sd  + mean;
			State sPrime = getState(positionPrime);
			distribution.add(sPrime);
		}
		distribution.get().forEach((ddp) -> res.addTransition(ddp.getValue(), isTerminal(ddp.getValue()) ? 10000 : -1, ddp.getP()));
		return res;
		
	}

	/** The state at given position */
	public State getState(double position) {
		if(position <= 0.0) {
			return initialState;
		}
		if(position >= xMax) {
			return terminalState;
		}
		double intervalLength = xMax/numStates;
		int index = (int) Math.floor(position / intervalLength) + 1;		
		return State.instance(index);
	}

	public int getNumActions() {
		return numActions;
	}
	
	public int getNumStates() {
		return numStates;
	}
}