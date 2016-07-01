package ml.rl.hw6;

import java.util.List;

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
	//  - no conclusion

	private Static 
	private int n;
	private int[][] subsets;
	private int[][] fightOccured;
	private int[] peacemakerPositions;
	private int[] instigatorPositions;

	public BarBrawl(int n, int[][] subsets, int[][] fightOccured) {
		super();
		this.n = n;
		this.subsets = subsets;
		this.fightOccured = fightOccured;
	}

	public void initialize() {
		peacemakerPositions = new int[n];
		instigatorPositions = new int[n];
		for (int i = 0; i < peacemakerPositions.length; i++) {
			peacemakerPositions[i] = 1;
			instigatorPositions[i] = 1;
		}

	}

	public String evaluate(boolean[] subset, boolean fightOccured) {
		if (!intersect(subset, instigatorPositions)) {
			retrun
		}
	}

	public void learn(boolean[] subset, boolean fightOccured) {

	}
}
