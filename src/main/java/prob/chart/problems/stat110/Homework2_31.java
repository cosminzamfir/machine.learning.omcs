package prob.chart.problems.stat110;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import util.MLUtils;
import static util.MLUtils.*;

public class Homework2_31 extends Simulation {

	@Override
	protected Boolean simulate() {
		int amount = 1000000;
		int targetAmount = amount + 2;
		while (amount >= 0 && amount < targetAmount) {
			boolean win = MLUtils.trueWithProbability(0.33);
			amount = win ? amount + 1  : amount -1; 
			System.out.println(amount);
		}
		if(amount == targetAmount) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	protected double computedResult() {
		List<BigDecimal> cs = new ArrayList<>();
		cs.add(BigDecimal.ZERO);
		cs.add(BigDecimal.ONE);
		BigDecimal pWin = new BigDecimal(0.33333);
		BigDecimal pLoose = new BigDecimal(0.66666);
		BigDecimal totalProbability = new BigDecimal(1.0/9);
		for (int i = 4; i < 2000; i=i+2) {
			int wins = i/2 + 1;
			BigDecimal counts = chooseBD(i,wins);
			for (int j = 2; j < i; j = j+2) {
				BigDecimal s = cs.get(j/2).multiply(chooseBD(i-j, (i-j)/2));
				counts = counts.subtract(s);
			}
			cs.add(counts);
			BigDecimal probability = counts.multiply(pWin.pow(wins)).multiply(pLoose.pow(i -wins));
			totalProbability = totalProbability.add(probability);
			System.out.println(totalProbability.toPlainString());
			
		}
		return 0;
		
				
	}
	
	public static void main(String[] args) {
		new Homework2_31().computedResult();
	}

}
