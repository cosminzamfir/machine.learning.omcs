package ml.rl.hw6;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.type.IntersectionType;

import util.MLUtils;

/**
 * 
 * @author Cosmin Zamfir
 * Given: 
 * <ul>
 * <li>A set of P patrons, one of whom is the instigator, another one is the pacemaker
 * <li>On a given day, a subset S is present at the establishment. 
 * <li>If the instigator is present, there is a fight.
 * <li>If no instigator is present or the pacemaker is present, no fight
 * </ul>
 * You have to predict whether a fight will occur among a subset of the patrons, initially without knowing the identities of instigator and peacemaker
 * <p> You should develop a KWIK learner. You are given the input:
 * <ul>
 * <li>int numPatrons
 * <li>boolean[][] atEstablishment - the subset of patrons for each episode
 * <li>boolean[][] fightOccured with size 1 x numPatrons
 * </ul>
 * For each episode, you should present your learner with the next row of atEstablishment and the corresponding value of fightOccured.
 * <p> If the learner returns 'Fight' of 'No fight', continue to next episode.
 * <p>If the learner returns 'I dont know', you should present the pair above to the learner to learn from
 * <p>The test case is successful if no wrong answers are returned and 'I DONT KNOW' is returned no more than numPatrons*(numPatrons-1) -1 times
 */
public class BarBrawl {

	//must keep 2 lists: possible positions of the pacemaker and possible positions of the pacemaker
	//if there is a fight =>
	// 	- the instigator cannot be outside S
	//  - the peacemaker cannot be in S
	//if there is no fight =>
	//  - it is not possible that the 

	private static String FIGTH = "FIGHT";
	private static String NO_FIGHT = "NO FIGHT";
	private static String DONTKNOW = "I DON'T KNOW";
	private int n;
	private int[][] subsets;
	private int[] fightOccured;
	private List<List<Combination>> possiblePositions; 

	public BarBrawl(int n, int[][] subsets, int[] fightOccured) {
		super();
		this.n = n;
		this.subsets = subsets;
		this.fightOccured = fightOccured;
		initialize();
	}

	public void initialize() {
		possiblePositions = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			List<Combination> l = new ArrayList<>();
			l.add(Combination._00);
			l.add(Combination._01);
			l.add(Combination._10);
			l.add(Combination._11);
			possiblePositions.add(l);
		}
		
	}
	
	public void run() {
		for (int i = 0; i < subsets.length; i++) {
			int[] subset = subsets[i];
			boolean fight = fightOccured[i] == 1;
			String res = evaluate(subset, fight);
			System.out.print(res + ",");
			if(res.equals(DONTKNOW)) {
				learn(subset, fight);
			}
		}
	}
	
	
	//must keep 2 lists: possible positions of the pacemaker and possible positions of the pacemaker
	//if there is a fight =>
	// 	- the instigator cannot be outside S
	//  - the peacemaker cannot be in S
	//if there is no fight =>
	//  - it cannot be that 
	public void learn(int[] subset, boolean fight) {
		if(fight) {
			for (int i = 0; i < subset.length; i++) {
				//the instigator cannot be outside S
				if(subset[i] == 0) {
					possiblePositions.remove(Combination._10);
					possiblePositions.remove(Combination._11);
				}
				//the peacemaker cannot be in S
				if(subset[i] == 1) {
					peacemakerPositions[i] = 0;
				}
			}
		}
	}

	public String evaluate(int[] subset, boolean fight) {
		Position instigatorPos = getPosition(subset, instigatorPositions);
		Position peacemakerPos = getPosition(subset, peacemakerPositions);
		if(instigatorPos == Position.OUT || peacemakerPos == Position.IN) {
			return NO_FIGHT;
		}
		if(instigatorPos == Position.IN && peacemakerPos == Position.OUT) {
			return FIGTH;
		}
		return DONTKNOW;
	}
	
	

	private Position getPosition(int[] subset, int[] possiblePositions) {
		boolean canBeIn = intersect(subset, possiblePositions);
		boolean canBeOut = intersect(complement(subset), possiblePositions);
		if(canBeIn && canBeOut) {
			return Position.BOTH;
		}
		if(canBeIn) {
			return Position.IN;
		}
		return Position.OUT;
	}



	private int[] complement(int[] subset) {
		int[] res = new int[n];
		for (int i = 0; i < n; i++) {
			res[i] = 1 - subset[i];
		}
		return res;
	}

	private boolean intersect(int[] subset, int[] possiblePositions) {
		for (int i = 0; i < n; i++) {
			if(subset[i] == 1 && possiblePositions[i] == 1) {
				return true;
			}
		}
		return false;
	}



	enum Position {
		IN, OUT, BOTH
	};
	
	enum Combination {
		_00, _01, _10, _11;
		
		public boolean isInstigator() {
			return this == _10 || this == _11; 
		}
		
		public boolean isPeacemaker() {
			return this == _01 || this == _11;
		}
		
		public boolean isInstigator(Combination[] cs) {
			for (Combination combination : cs) {
				if(combination.isInstigator()) {
					return true;
				}
			}
			return false;
		}

		public boolean isPeaceMaker(Combination[] cs) {
			for (Combination combination : cs) {
				if(combination.isPeacemaker()) {
					return true;
				}
			}
			return false;
		}

	}
}
