package test;

import ml.model.DataSet;
import ml.model.Observation;
import ml.slearning.id3.Id3;
import ml.slearning.id3.Id3Node;
import ml.utils.DataSetCsvParser;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class Id3Test {

	@Test
	public void test1() throws Exception {
		DataSet dataSet = DataSetCsvParser.parse("play_tennis.txt");
		Id3 id3 = new Id3();
		Id3Node decisionTree = id3.createDecistionTree(dataSet);
		System.out.println(decisionTree);
		Assert.assertTrue(decisionTree.getBranches().size() > 1);
	}
	
	@Test
	public void test2() throws Exception {
		DataSet dataSet = DataSetCsvParser.parse("car_data.txt");
		Id3 id3 = new Id3();
		DataSet training = dataSet.subSet(0, 1400);
		DataSet validation = dataSet.subSet(1401,1700);
		Id3Node decisionTree = id3.createDecistionTree(training);
		int matches = 0;
		int mismatches = 0;
		for (Observation instance : validation.getObservations()) {
			Object computed = decisionTree.clasify(instance);
			Object actual = instance.getTargetAttributeValue();
			if(computed.equals(actual)) {
				matches ++;
			} else {
				mismatches ++;
			}
		}
		System.out.println("Matches: " + matches + " - Mismatches: " + mismatches);
		Assert.assertTrue(mismatches/(double) matches < 0.15);
		
	}
}
