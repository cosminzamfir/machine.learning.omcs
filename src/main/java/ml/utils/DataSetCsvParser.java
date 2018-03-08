package ml.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ml.model.Attribute;
import ml.model.DataSet;
import ml.model.Observation;

/**
 * Parse dataSets in csv format:
 * <p>Attr1,Attr2,...,Attrn,TargetAttribute
 * <p>val1,val2,...,valn,targetVal
 * <p>val1,val2,...,valn,targetVal
 * <p>....
 * <p>val1,val2,...,valn,targetVal
 * @author Cosmin Zamfir
 *
 */
public class DataSetCsvParser {

	public static DataSet parseTextDataSet(String resource, boolean shufle) {
		return parse(resource, false, shufle);
	}

	public static DataSet parseNumericDataSet(String resource, boolean shufle) {
		return parse(resource, true, shufle);
	}

	private static DataSet parse(String resource, boolean numeric, boolean shufle) {
		List<String> lines = IOUtils.readLines(resource, true);
		String[] header = lines.get(0).split(",");
		List<Attribute> attributes = new ArrayList<>();
		List<Observation> instances = new ArrayList<>();
		for (int i = 0; i < header.length - 1; i++) {
			attributes.add(new Attribute(header[i], String.class));
		}
		Attribute targetAttribute = new Attribute(header[header.length - 1], String.class);
		lines.remove(0);
		if (shufle) {
			Collections.shuffle(lines);
		}
		int index = 1;
		for (String line : lines) {
			if (line.contains("NA")) {
				continue;
			}
			String[] tokens = line.split(",");
			if (tokens.length != header.length) {
				throw new RuntimeException(MessageFormat.format("Unexpected number of attributes at line {0} in the data: header has {1}. Data row has {2}",
						index, header.length, tokens.length));
			}
			if (!numeric) {
				throw new RuntimeException("Only numerical data sets supported");
			} else {
				double[] values = new double[tokens.length - 1];
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.valueOf(tokens[i]);
				}
				double targetAttributeValue = Double.valueOf(tokens[tokens.length - 1]);
				instances.add(new Observation(targetAttributeValue, values));
			}
			index++;
		}
		return new DataSet(attributes, instances, targetAttribute);
	}

	public static void main(String[] args) {
		new DataSetCsvParser().parseNumericDataSet("bc.txt", false);
	}
}
