package ml.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ml.model.Attribute;
import ml.model.DataSet;
import ml.model.Observation;

public class DataSetCsvParser {

	public static DataSet parse(String resource) {
		List<String> lines = IOUtils.readLines(resource, true);
		String[] header = lines.get(0).split(",");
		List<Attribute> attributes = new ArrayList<>();
		List<Observation> instances = new ArrayList<>();
		for (int i = 0; i < header.length-1; i++) {
			attributes.add(new Attribute(header[i], String.class, i));
		}
		Attribute targetAttribute = new Attribute(header[header.length - 1], String.class, 0);
		lines.remove(0);
		Collections.shuffle(lines);
		for (String line : lines) {
			String[] tokens = line.split(",");
			String[] values = new String[tokens.length -1];
			for (int i = 0; i < values.length; i++) {
				values[i] = tokens[i];
			}
			String classification = tokens[tokens.length - 1];
			instances.add(new Observation(classification, values));
		}
		return new DataSet(attributes, instances, targetAttribute);
	}
}
