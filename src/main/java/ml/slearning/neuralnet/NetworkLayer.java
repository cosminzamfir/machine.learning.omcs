package ml.slearning.neuralnet;

import java.util.List;

import linalg.Vector;
import ml.model.DataSet;
import ml.model.Observation;
import ml.utils.Utils;

/**
 * A hidden layer in a {@link NeuralNetwork} that takes inputs from the layer before and forwards outputs to the layer after 
 * @author Cosmin Zamfir
 */
public class NetworkLayer {

	/** Not null if the first hidden layer */
	private DataSet inputDataSet;

	/** Null if the first hidden layer */
	private NetworkLayer previousLayer;

	/** Null if this is the output layer */
	private NetworkLayer nextLayer;

	private List<Sigmoid> units;

	/**
	 * The weight of the output if each unit in this layer to each unit in the next layer
	 */
	private double[][] outputCoefficients;

	public NetworkLayer(DataSet inputDataSet, List<Sigmoid> units) {
		super();
		this.inputDataSet = inputDataSet;
		this.units = units;
	}

	public NetworkLayer(NetworkLayer inputLayer, List<Sigmoid> units) {
		super();
		this.previousLayer = inputLayer;
		this.units = units;
	}

	public NetworkLayer getInputLayer() {
		return previousLayer;
	}

	public void setInputLayer(NetworkLayer inputLayer) {
		this.previousLayer = inputLayer;
	}

	public NetworkLayer getOutputLayer() {
		return nextLayer;
	}

	public void setOutputLayer(NetworkLayer outputLayer) {
		this.nextLayer = outputLayer;
	}

	public List<Sigmoid> getUnits() {
		return units;
	}

	public void setUnits(List<Sigmoid> units) {
		this.units = units;
	}

	public double[][] getOutputCoefficients() {
		return outputCoefficients;
	}

	public DataSet getInputDataSet() {
		return inputDataSet;
	}

	public void setInputDataSet(DataSet inputDataSet) {
		this.inputDataSet = inputDataSet;
	}

	public void generateCoefficients() {
		for (Sigmoid unit : units) {
			if(inputDataSet != null) {
				unit.generateRandomCoefficients(inputDataSet);
			} else {
				unit.generateRandomCoefficients(previousLayer.getUnits().size());
			}
		}
	}

	public void compute(Observation observation) {
		double[] previousLayerValues = getPreviousLayerValues(observation);
		for (Sigmoid unit : units) {
			unit.setCurrentValue(unit.evaluate(previousLayerValues));
		}
	}

	private double[] getPreviousLayerValues(Observation observation) {
		double[] res;
		if(inputDataSet != null) {
			res = new double[observation.getValues().length + 1];
			res[0] = 1;
			for (int i = 0; i < observation.getValues().length; i++) {
				res[i+1] = (double) observation.getValues()[i];
			}
		} else {
			res = new double[previousLayer.getUnits().size()];
			for (int i = 0; i < res.length; i++) {
				res[i] = previousLayer.getUnits().get(i).getCurrentValue();
			}
		}
		return res;
	}

	public Sigmoid get(int i) {
		return units.get(i);
	}
	
	public double getCurrentValue(int i) {
		return units.get(i).getCurrentValue();
	}
	
	public double getCurrentErrorTerm(int i) {
		return units.get(i).getCurrentErrorTerm();
	}
	
	public double evaluate(int i, double[] input) {
		return units.get(i).evaluate(input);
	}

	public int size() {
		return units.size();
	}

}
