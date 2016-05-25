package prob.chart.problems.grinstead;

public enum Shape {

	Hearts, Diamond, Clubs, Spades;

	public String toString() {

		if (this == Hearts) {
			return "H";
		}
		else if (this == Diamond) {
			return "D";
		}
		else if (this == Clubs) {
			return "C";
		}
		else {
			return "S";
		}
	};

}
