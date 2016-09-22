package ml.stat.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static ml.stat.model.Functions.*;

public class DataFrame {

	private List<String> names =  new ArrayList<String>();
	private Map<String, List> data = new LinkedHashMap<String, List>();
	
	/**
	 * Receive default column names
	 * @param columns
	 */
	public DataFrame(List ... columns) {
		for (int i = 0; i < columns.length; i++) {
			String name = "col" + i;
			names.add(name);
			data.put(name, columns[i]);
		}
	}
	
	public static void main(String[] args) {
		DataFrame df = new DataFrame(seq(1,100), seq(100,200));
	}
}
