package ml.slearning.neuralnet;

import java.util.List;

import ml.model.DataSet;

/**
 * A hidden layer in a {@link NeuralNetwork} that takes inputs from the layer before and forwards outputs to the layer after 
 * @author Cosmin Zamfir
 */
public class NetworkLayer {

	/** Not null if the first hidden layer */
	private DataSet inputDataSet;

	/** Null if the first hidden layer */
	private NetworkLayer inputLayer;

	/** Null if this is the output layer */
	private NetworkLayer outputLayer;

	private List<Sigmoid> units;

	/**
	 * The weight of the input of each unit in the previous layer to each unit in this layer 
	 */
	private double[][] inputCoefficients;

	/**
	 * The weight of the output if each unit in this layer to each unit in the next layer
	 */
	private double[][] outputCoefficients;

	public NetworkLayer(DataSet inputDataSet, List<Sigmoid> units) {
		super();
		this.inputDataSet = inputDataSet;
		this.units = units;
		this.inputCoefficients = new double[inputDataSet.getAttributes().size()][units.size()];
	}
	
	public NetworkLayer(NetworkLayer inputLayer, List<Sigmoid> units) {
		super();
		this.inputLayer = inputLayer;
		this.units = units;
		this.inputCoefficients = new double[inputLayer.getUnits().size()][units.size()];
	}

	public NetworkLayer getInputLayer() {
		return inputLayer;
	}

	public void setInputLayer(NetworkLayer inputLayer) {
		this.inputLayer = inputLayer;
	}

	public NetworkLayer getOutputLayer() {
		return outputLayer;
	}

	public void setOutputLayer(NetworkLayer outputLayer) {
		this.outputLayer = outputLayer;
	}

	public List<Sigmoid> getUnits() {
		return units;
	}

	public void setUnits(List<Sigmoid> units) {
		this.units = units;
	}

	public double[][] getInputCoefficients() {
		return inputCoefficients;
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
}
