package cv.omcs;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import prob.chart.ChartBuilder;
import prob.chart.PlotType;
import util.MLUtils;

public class HoughLineTest {

	public static void main(String[] args) {
		List<String> lines = MLUtils.readFile("c:/work/data/transfer/sin.txt");
		int k = 180;
		Map<Double,Double> map = parse(lines, 0*k , 1000*k , 1); 
		new ChartBuilder().add(map, "sins", PlotType.Scatter).build();
	}

	private static Map<Double, Double> parse(List<String> lines, int startLine, int count, int step) {
		Map<Double, Double> res = new LinkedHashMap<Double, Double>();
		for (int i = startLine; i < startLine + count; i += step) {
			String line = lines.get(i);
			String[] tokens = line.split(",");
			res.put(Double.valueOf(tokens[2]), Double.valueOf(tokens[3]));
		}
		return res;
		
	}
}
