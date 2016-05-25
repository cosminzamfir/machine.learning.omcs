package prob.chart.problems.grinstead;

import java.util.List;

import ml.utils.Utils;

public class BridgeShuffling {

	public static void main(String[] args) {
		System.out.println(new BridgeShuffling().run(10000));
		System.out.println(3/(double)32);
	}

	/**
	 * What is the probability that each player receives one Ace
	 */
	private double run(int samples) {
		int successes = 0;
		for (int i = 0; i < samples; i++) {

			List<Card> deck = Card.generateAll();
			List<Card> hand1 = Utils.randomChoice(deck, 13, true);
			List<Card> hand2 = Utils.randomChoice(deck, 13, true);
			List<Card> hand3 = Utils.randomChoice(deck, 13, true);
			List<Card> hand4 = Utils.randomChoice(deck, 13, true);
			if (Card.count(hand1, 1, null) == 1 &&
					Card.count(hand2, 1, null) == 1 &&
					Card.count(hand3, 1, null) == 1 &&
					Card.count(hand4, 1, null) == 1) {
				successes++;
			}
		}

		return successes / (double) samples;
	}

}
