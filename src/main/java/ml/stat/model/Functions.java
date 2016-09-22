package ml.stat.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Functions {

	public static List c(Object ... o) {
		return Arrays.asList(o);
	}
	
	public static List seq(int start, int end) {
		List<Integer> res = new ArrayList<Integer>();
		for (int i = start; i <= end; i++) {
			res.add(i);
		}
		return res;
	}
}
