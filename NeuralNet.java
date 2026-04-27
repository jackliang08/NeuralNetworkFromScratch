package execution;
import java.util.*;

public class NeuralNet {
	public ArrayList<ArrayList<Perceptron>> perceptrons;  // ArrayList containing all perceptrons from each layer

	
	// **CONSTRUCTORS**
	
	// MUST specify how many layers (input + hidden) there are
	public NeuralNet(int layers) {
		perceptrons = new ArrayList<ArrayList<Perceptron>>();
		for (int i=0;i<layers;i++) {
			perceptrons.add(new ArrayList<Perceptron>()); 
		}
		perceptrons.add(new ArrayList<Perceptron>());  // The output layer
		//perceptrons.get(layers).add(new Perceptron(1));  // The output perceptron
	}
	
	
	// **ADDING TO PERCEPTRONS**
	
	// Adds a perceptron to the layer
	// MIGHT be useful, but not really
	public void addPerceptron(int layer, Perceptron p) {
		perceptrons.get(layer).add(p);
	}
	
	// Adds multiple perceptrons to the layer
	public void addPerceptrons(int layer, int size, int numOutputs) {
		for (int i=0;i<size;i++) {
			perceptrons.get(layer).add(new Perceptron(numOutputs));
		}
	}
	
	
	
	// **ACTUAL PROPAGATION**
	
	// *FORWARD PROPAGATION*
	
	// Performs forward propagation based on the sigmoid function
	// inputs array MUST match the size of the #perceptrons in the next layer
	private double[] forward(int layer, double[] inputs) {
		double[] outputs = new double[perceptrons.get(layer+1).size()];  // Creates a new array for an output for each perceptron in the next layer
		double[][] weightMatrix = new double[perceptrons.get(layer).size()][];

		for (int i=0;i<perceptrons.get(layer).size();i++) {  // Fills up the weightMatrix with the perceptron layers
			weightMatrix[i] = perceptrons.get(layer).get(i).getWeights();
		}
		outputs = matMult(layer,inputs,weightMatrix);  // Unstandardized outputs for the next layer
		
		
		for (int i=0;i<outputs.length;i++) {  // Adds bias and performs a function to standardize outputs
			outputs[i] += perceptrons.get(layer+1).get(i).getBias();
			outputs[i] = sigmoid(outputs[i]);
		}
		
		
		return outputs;  // The outputs of the previous layer, to be inputted in the subsequent layer
	}
	
	public double[] forwardAll(double[] inputs) {
		double[] outputs = {};  // Will be the output for ALL layers after each iteration
		for (int i=0;i<perceptrons.size()-1;i++) {
			outputs = forward(i,inputs);
			inputs = outputs;  // The output of this layer becomes the input of the next layer
		}
		for (int i=0;i<outputs.length;i++) {
			perceptrons.getLast().get(i).updateOutput(outputs[i]);  // Updates output layers' outputArray (for backprop)
		}
		return outputs;
	}
	
	
	// *BACKPROPAGATION*
	
	public void backwardAll(double a, double... target) { // For the entire NN, starting with the output layer weights then calling backwardOutput for the other layers
		double weightChanged;
		double biasChanged;
		double output;
		double weightOutput;  // The output of node associated with the weight
		ArrayList<Perceptron> prevLayer = perceptrons.get(perceptrons.size()-2);
		double[] errs = new double[perceptrons.getLast().size()];  // The partial derivatives of each output error on net output of o1
		for (int cN=0;cN<perceptrons.getLast().size();cN++) {  // Each current neuron
			for (int pN=0;pN<prevLayer.size();pN++) {  // Each node before the current
				output = perceptrons.getLast().get(cN).getOutput();
				weightOutput = prevLayer.get(pN).getOutput();  // Get the jth weight from the kth neuron
				errs[cN] = 2 * (target[cN]-output) * (output*(1-output));
				weightChanged = a * 2 * (target[cN]-output) * (output*(1-output)) * (weightOutput);
				biasChanged = a * 2 * (target[cN]-output) * (output*(1-output));
				prevLayer.get(pN).updateWeight(cN, prevLayer.get(pN).getWeight(cN) + weightChanged);  // Updates the jth weight of the kth node
				perceptrons.get(perceptrons.size()-1).get(cN).updateBias(perceptrons.get(perceptrons.size()-1).get(cN).getBias() + biasChanged);  //Updates the bias of the ith neuron
			}
		}
		for (int layer=perceptrons.size()-2;layer>0;layer--) {
			backwardOutput(a, layer,errs);
		}
		
	}
	// errors - collection of dE/dOut from each next neuron
	private void backwardOutput(double a, int layer, double[] errors) {  // Backprop for each layer, based on layer & layer-1 (updates weights outgoing from layer-1)
		double weightChanged;
		double biasChanged;
		double netError=0;  // The summation of all dE/dOuts
		double output;
		double weightOutput;  // The output of node associated with the weight
		ArrayList<Perceptron> currentLayer = perceptrons.get(layer);
		ArrayList<Perceptron> prevLayer = perceptrons.get(layer-1);
		for (int cN=0;cN<currentLayer.size();cN++) {
			for (int pN=0;pN<prevLayer.size();pN++) {  // Each node before the current
				
				for (int cE=0;cE<errors.length;cE++) {  // Adds up each error from the current layer
					errors[cE] = errors[cE] * prevLayer.get(pN).getWeight(cE);
					netError += errors[cE];
				}
				
				output = currentLayer.get(cN).getOutput();  // Gets output of current node (sigmoid of prev node's output to current node)
				// Gets output of previous node to current node
				if (layer == 0) {
					System.out.println("cannot backprop input layer");
					return;
				}
				weightOutput = prevLayer.get(pN).getOutput();
				
				weightChanged =  a * netError * ((output)*(1-output)) * weightOutput;
				biasChanged = a * netError;
				prevLayer.get(pN).updateWeight(cN, prevLayer.get(pN).getWeight(cN) + weightChanged);
				currentLayer.get(cN).updateBias(currentLayer.get(cN).getBias() + biasChanged);
				
			}
		}
	}
	
	// **MATHEMATIC FUNCTIONS**
	
	// Performs matrix multiplication
	// for forward propagation functions
	// ONLY works if arr.length == mat.length (A.K.A columns)
	public double[] matMult(int layer, double[] arr, double[][] mat) {
		if (arr.length != mat.length) {
			System.out.printf("arr length of %d cannot match matrix column size of %d on layer %d%n",arr.length,mat.length,layer);
			System.out.println(Arrays.toString(arr));
			return new double[mat[0].length]; 
		}
		double[] output = new double[mat[0].length];
		double sum;
		double value;
		for (int i=0;i<output.length;i++) {
			sum = 0;
			for (int j=0;j<arr.length;j++) {
				value = arr[j] * mat[j][i];
				sum += value;
				perceptrons.get(layer).get(j).updateOutput(arr[j]);  // Updates the output array of the perceptron (used in backprop)
			}
			output[i] = sum;
		}
		return output;
	}
	
	// Performs the sigmoid function to standardize all values
	public double sigmoid(double val) {
		return 1/(1+Math.exp(-1*val));
	}

	// Performs the ReLU function to standardize all values
	public double relu(double val) {
		return Math.max(0, val);
	}
}
