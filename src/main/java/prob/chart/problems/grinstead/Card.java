package prob.chart.problems.grinstead;

import java.util.ArrayList;
import java.util.List;

public class Card {

	private static int min = 1;
	private static int max = 13;
	/** 1 to 13 */
	private int number;
	private Shape shape;

	public static Card instance(int number, Shape shape) {
		return new Card(number, shape);
	}

	public Card(int number, Shape shape) {
		super();
		this.number = number;
		this.shape = shape;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public static List<Card> generateAll() {
		List<Card> res = new ArrayList<>();
		for (int i = min; i <= max; i++) {
			for (Shape shape : Shape.values()) {
				res.add(new Card(i, shape));
			}
		}
		return res;
	}

	@Override
	public boolean equals(Object other) {
		return number == ((Card) other).getNumber() && shape == ((Card) other).getShape();
	}

	public static boolean contains(List<Card> hand, Integer number, Shape shape) {
		for (Card card : hand) {
			boolean match = true;
			if (number != null && card.getNumber() != number) {
				match = false;
			}
			if (shape != null && card.getShape() != shape) {
				match = false;
			}
			if (match) {
				return true;
			}
		}
		return false;
	}

	public static int count(List<Card> hand, Integer number, Shape shape) {
		int res = 0;
		for (Card card : hand) {
			boolean match = true;
			if (number != null && card.getNumber() != number) {
				match = false;
			}
			if (shape != null && card.getShape() != shape) {
				match = false;
			}
			if(match) {
				res++;
			}
		}
		return res;
	}

	@Override
	public String toString() {
		return "" + number + shape;
	}

}
