package ml.rl.mdp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author eh2zamf
 * A die with N sides, and an array of bad sides.
 * At each game step, decide whether wou roll or not. If not, game over.
 * If yes, win the face amount or lose everything in the face is in bad sides array 
 *
 */
public class DieNEnvironment implements FullTransitionInfoEnvironment{

	private static final String QUIT = "quit";
	private static final String ROLL = "roll";
	private static final String GAMEOVER = "gameOver";
	private static final String AMOUNT = "amount";
	private static final int MAX_AMOUNT = 1000;
	
	private int n;
	private List<Integer> badSides;
	
	public DieNEnvironment(int n, List<Integer> badSides) {
		super();
		this.n = n;
		this.badSides = badSides;
	}

	@Override
	public List<StateAction> getTransitions(State state) {
		List<StateAction> res = new ArrayList<StateAction>();
		
		StateAction quit = StateAction.instance(state, Action.instance(QUIT));
		State sprime = State.instance();
		sprime.set(GAMEOVER, true);
		sprime.set(AMOUNT, state.get(AMOUNT));
		sprime.setValue(state.getInteger(AMOUNT));
		quit.addTransition(sprime, 0, 1);
		
		
		StateAction roll = StateAction.instance(state, Action.instance(ROLL));
		for (int i = 1; i <= n; i++) {
			sprime = State.instance();
			if(badSides.contains(i)) {
				sprime.set(GAMEOVER, true);
				sprime.set(AMOUNT, 0);
				sprime.setValue(0);
			} else {
				sprime.set(GAMEOVER, false);
				sprime.set(AMOUNT, state.getInteger(AMOUNT) + i);
				sprime.setValue(0);
			}
			roll.addTransition(sprime, 0, 1.0/n);
		}
		res.add(quit);
		res.add(roll);
		return res;
	}
	
	@Override
	public boolean isTerminal(State state) {
		return state.getInteger(AMOUNT) > MAX_AMOUNT || state.getBoolean(GAMEOVER);
	}
	
	@Override
	public State initialState() {
		State res = State.instance();
		res.set(AMOUNT, 0);
		res.set(GAMEOVER, false);
		return res;
	}
	
	
}
