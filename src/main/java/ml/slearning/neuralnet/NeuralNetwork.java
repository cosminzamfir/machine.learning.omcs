package ml.slearning.neuralnet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import weka.core.pmml.MappingInfo;
import ml.model.DataSet;
import ml.model.Observation;

/**
 * 
 * @author Cosmin Zamfir
 *
 */
public class NeuralNetwork {

	private DataSet dataSet;
	private List<NetworkLayer> layers = new ArrayList<>();
	/** Map each Sigmoid to its 'outputs' */
	private Map<Sigmoid,List<Sigmoid>> mappings = new LinkedHashMap<>();
	private double learningRate;
	
	public static NeuralNetwork twoLayersNetwork(DataSet inputDataSet, int hiddenUnitsCounts, int outputUnitsCount) {
		NeuralNetwork res = new NeuralNetwork();
		res.dataSet = inputDataSet;
		List<Sigmoid> hiddenUnits = new ArrayList<>();
		for (int i = 0; i < hiddenUnitsCounts; i++) {
			hiddenUnits.add(new Sigmoid());
		}
		NetworkLayer hiddenLayer = new NetworkLayer(inputDataSet, hiddenUnits);
		res.layers.add(hiddenLayer);
		
		
		List<Sigmoid> outputUnits = new ArrayList<>();
		for (int i = 0; i < outputUnitsCount; i++) {
			outputUnits.add(new Sigmoid());
		}
		NetworkLayer outputLayer = new NetworkLayer(hiddenLayer, outputUnits);
		res.layers.add(outputLayer);
		for (Sigmoid hiddenUnit : hiddenUnits) {
			res.mappings.put(hiddenUnit, outputUnits);
		}
		return res;
	}
	
	public NetworkLayer getOutputLayer() {
		return layers.get(layers.size() - 1);
	}
	
	public List<NetworkLayer> getHiddenLayers() {
		List<NetworkLayer> res = new ArrayList<>(layers);
		res.remove(res.size() - 1);
		return res;
	}
	
	/**
	 * Compute the value of each unit for the given observation
	 * @param observation
	 */
	public void compute(Observation observation) {
		for (NetworkLayer layer : layers) {
			layer.compute(observation);
		}
	}
	
	/**
	 * <p>For outut Units, the error term is: error(k) = output(k) * (1-output(k)) * (targetOutput(k) - output(k))
	 * <p>For hidden Units, the error term is: error(h) = output(h) * (1-output(h)) * sum[k in output] (w(k,h) * error(k))
	 * @param observation
	 */
	public void computeErrorTerms(Observation observation) {
		for (int i = 0; i < getOutputLayer().getUnits().size(); i++) {
			Sigmoid unit = getOutputLayer().getUnits().get(i);
			double computedValue = unit.getCurrentValue();
			double targetValue = getTargetAttributeValue(observation,i);
			double errorTerm = computedValue * (1 - computedValue) * (targetValue - computedValue); 
			unit.setCurrentErrorTerm(errorTerm);
		}
		for (int i = layers.size() - 2; i > 0; i--) {
			NetworkLayer currentLayer = layers.get(i);
			NetworkLayer nextLayer = layers.get(i+1);
			for (int j = 0; j < currentLayer.size(); j++) {
				double d1 = currentLayer.getCurrentValue(j) * (1 - currentLayer.getCurrentValue(j));
				double d2 = 0;
				for (int k = 0; k < nextLayer.size(); k++) {
					d2 += nextLayer.get(k).getCoefficients()[j] * nextLayer.getCurrentErrorTerm(k);
				}
				currentLayer.get(j).setCurrentErrorTerm(d1 * d2);
			}
		}
	}
	
	public void updateWeights() {
		NetworkLayer outputLayer = getOutputLayer();
		for (int i = 0; i < getOutputLayer().getUnits().size(); i++) {
			Sigmoid unit = getOutputLayer().getUnits().get(i);
			double delta = learningRate * unit.getCurrentErrorTerm() * unit.getCurrentValue();
			
		}
		for (int i = layers.size() - 2; i > 0; i--) {
		}
	}
	

	/**
	 * There are n categories. The Observation target value is an integer from 1 to n 
	 * @param observation
	 * @param i
	 * @return 0.9 if the target value of the Observation is equal to i, otherwise 0.1
	 */
	private double getTargetAttributeValue(Observation observation, int i) {
		return (int)observation.getTargetAttributeValue() == i ? 0.9 : 0.1;
	}
}
