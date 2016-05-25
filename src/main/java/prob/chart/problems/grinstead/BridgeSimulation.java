package prob.chart.problems.grinstead;

import java.util.List;

import ml.utils.Utils;

public class BridgeSimulation {

	public static void main(String[] args) {
		System.out.println(new BridgeSimulation().runSimulation1(100000));
		System.out.println(new BridgeSimulation().runSimulation2(100000));
	}
	
	/**
	 * What is the probability that your bridge partner has exactly two aces, given that she has at least one ace?
	 */
	public double runSimulation1(int samples) {
		List<Card> deck = Card.generateAll();
		int successes = 0;
		int relevantSamples = 0;
		for (int i = 0; i < samples; i++) {
			List<Card> hand = Utils.randomChoice(deck, 13, true);
			if(!Card.contains(hand, 1, null)) {
				continue;
			}
			relevantSamples++;
			if(Card.count(hand, 1, null) ==2) {
				successes ++;
			}
		}
		return successes/(double)relevantSamples;
	}

	/**
	 * What is the probability that your bridge partner has exactly two aces, given that she has the ace of black
	 */
	public double runSimulation2(int samples) {
		List<Card> deck = Card.generateAll();
		int successes = 0;
		int relevantSamples = 0;
		for (int i = 0; i < samples; i++) {
			List<Card> hand = Utils.randomChoice(deck, 13, true);
			if(!Card.contains(hand, 1, Shape.Spades)) {
				continue;
			}
			relevantSamples++;
			if(Card.count(hand, 1, null) == 2) {
				successes ++;
			}
		}
		return successes/(double)relevantSamples;
	}

}
