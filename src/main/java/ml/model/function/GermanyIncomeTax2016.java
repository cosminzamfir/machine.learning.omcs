
package ml.model.function;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * The relative/absolute income tax in Germany as of 2016 for married couples
 * 
 * @author Cosmin Zamfir
 *
 */
public class GermanyIncomeTax2016 implements Function {

	private static final Logger log = Logger.getLogger(GermanyIncomeTax2016.class);
	private boolean usePercent = false;

	
	/**
	 * if <code>true</code> return the percentage of the tax in the taxable income; otherwise the absolute tax value
	 */
	public GermanyIncomeTax2016 asPercentage(boolean usePercent) {
		this.usePercent = usePercent;
		return this;
	}

	public static class TaxLevel {
		private double lowerAmount;
		private double upperAmount;
		private double lowerTax;
		private double upperTax;

		public static TaxLevel instance(double start, double end, double percTaxStart, double percTaxEnd) {
			TaxLevel res = new TaxLevel();
			res.lowerAmount = start;
			res.upperAmount = end;
			res.lowerTax = percTaxStart;
			res.upperTax = percTaxEnd;
			return res;
		}

		public double computeTax(double d) {
			double res = new DefiniteIntegral()
					.compute(((x) -> (lowerTax + (upperTax - lowerTax) * x /
							(upperAmount - lowerAmount))), 0, d, 0.001);
			//						double tax = lowerTax + (upperTax - lowerTax) * (d / (upperAmount - lowerAmount));
			//						double res = d * tax;
			
			log.info(MessageFormat.format("{0}. Tax for {1} is {2}", TaxLevel.this, d, res));
			return res;
		}

		@Override
		public String toString() {
			return MessageFormat.format("TaxLevel[{0}:{1} -> {2}:{3}]", lowerAmount, lowerTax, upperAmount, upperTax);
		}
	}

	private static List<TaxLevel> taxLevels = new ArrayList<GermanyIncomeTax2016.TaxLevel>();
	static {
		taxLevels.add(TaxLevel.instance(0, 16708, 0, 0));
		taxLevels.add(TaxLevel.instance(16708, 26938, 0.14 * 1.0055, 0.24 * 1.0055));
		taxLevels.add(TaxLevel.instance(26938, 105762, 0.24 * 1.0055, 0.42 * 1.0055));
		taxLevels.add(TaxLevel.instance(105762, 510460, 0.42 * 1.0055, 0.42 * 1.0055));
		taxLevels.add(TaxLevel.instance(510460, 999999999, 0.45 * 1.0055, 0.45 * 1.0055));
	}

	@Override
	public double evaluate(double x) {
		double res = 0;
		for (TaxLevel taxLevel : taxLevels) {
			if (x < taxLevel.lowerAmount) {
				return usePercent ? res / x : res;
			}
			res += taxLevel.computeTax(Math.min(taxLevel.upperAmount, x) - taxLevel.lowerAmount);
		}
		return usePercent ? res / x : res;
	}

	public static void main(String[] args) {
		// System.out.println(new
		new GermanyIncomeTax2016().asPercentage(true).plot(0, 500000);
	}

	@Override
	public String toString() {
		return "taxableIncome -> incomeTax " + (usePercent ? "(%)" : "(Eur");
	}

}
