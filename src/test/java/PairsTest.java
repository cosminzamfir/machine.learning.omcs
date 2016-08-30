import java.util.Date;
import java.util.List;

import util.MLUtils;

public class PairsTest {

	public static void main(String[] args) {
		List<Double> w = MLUtils.randomList(100, 100, 200);
		List<Double> m = MLUtils.randomList(100, 100, 200);
		while(true) {
			int randomW = MLUtils.random(w.size()-1);
			Double wSalaray = w.get(randomW);
			int randomM = MLUtils.random(m.size() -1);
			Double mSalary = m.get(randomM);
			if(mSalary > wSalaray) {
				w.remove(randomW);
				m.remove(randomM);
				System.out.println(new Date() + "::" + w.size());
			}
			
		}
	}
}
