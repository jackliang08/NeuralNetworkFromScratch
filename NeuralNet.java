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
	
	// Performs forward propagation based on the sigmoid function
	// inputs array MUST match the size of the #perceptrons in the next layer
	public double[] forward(int layer, double[] inputs) {
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
			perceptrons.getLast().get(i).updateOutput(0, outputs[i]);  // Updates output layers' outputArray (for backprop)
		}
		return outputs;
	}
	public void backwardOutput(double a, double... target) {
		double weightChanged;
		double biasChanged;
		double output;
		double weightOutput;  // The output of node associated with the weight
		ArrayList<Perceptron> prevLayer = perceptrons.get(perceptrons.size()-2);
		for (int i=0;i<perceptrons.getLast().size();i++) {  // Each output neuron
			for (int j=0;j<perceptrons.getLast().size();j++) {  // Each weight before the output
				for (int k=0;k<prevLayer.size();k++) {  // Each node before the output
					output = perceptrons.getLast().get(i).getOutput(0);
					weightOutput = prevLayer.get(k).getOutput(j);  // Get the jth weight from the kth neuron
					weightChanged = a * 2 * (target[i]-output) * (output*(1-output)) * (weightOutput);  //TODO: figure out whether output is correct or if its switched with weightOutput
					biasChanged = a * 2 * (target[i]-output) * (output*(1-output));
					prevLayer.get(k).updateWeight(j, prevLayer.get(k).getWeight(j) + weightChanged);  // Updates the jth weight of the kth node
					perceptrons.get(perceptrons.size()-1).get(i).updateBias(perceptrons.get(perceptrons.size()-1).get(i).getBias() + biasChanged);  //Updates the bias of the ith neuron
				}
			}
			
		}
	}
	
	/*
	public void backward(int layer, Perceptron p, double a, double error) {
		double output;  // Output of current neuron with that weight
		double forwardOutput;  // Output of neuron in front of weight
		double deltaWeight;  // The change in weight
		for (int i=0;i<perceptrons.get(layer+1).size();i++) {  //  Iterates through the length of weights in the perceptron
			output = p.getOutput(i);
			forwardOutput = perceptrons.get(layer+1).get(i).getOutput();  
			deltaWeight = a;
			deltaWeight *= 
		}
	}
	*/
	
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
				value = arr[i] * mat[j][i];
				sum += value;
				perceptrons.get(layer).get(j).updateOutput(i, sigmoid(value));  // Updates the output array of the perceptron (used in backprop)
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
