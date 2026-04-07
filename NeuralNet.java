package execution;
import java.util.*;

public class NeuralNet {
	ArrayList<ArrayList<Perceptron>> perceptrons;  // ArrayList containing all perceptrons from each layer

	
	// **CONSTRUCTORS**
	
	// MUST specify how many layers (input + hidden) there are
	public NeuralNet(int layers) {
		perceptrons = new ArrayList<ArrayList<Perceptron>>();
		for (int i=0;i<layers;i++) {
			perceptrons.add(new ArrayList<Perceptron>()); 
		}
		perceptrons.add(new ArrayList<Perceptron>());  // The output layer
		perceptrons.get(layers).add(new Perceptron(1));  // The output perceptron
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
			System.out.print(i + ". ");
			outputs[i] = sigmoid(outputs[i]);
		}
		
		
		return outputs;  // The outputs of the previous layer, to be inputted in the subsequent layer
	}
	
	public double forwardAll(double[] inputs) {
		double[] outputs = {};  // Will be the output for ALL layers after each iteration
		for (int i=0;i<perceptrons.size()-1;i++) {
			outputs = forward(i,inputs);
			inputs = outputs;  // The output of this layer becomes the input of the next layer
		}
		return outputs[0];
	}
	
	public void backward(int layer, Perceptron p, double a, double error) {
		double output;  // Output of current neuron with that weight
		double forwardOutput;  // Output of neuron in front of weight
		double deltaWeight;  // The change in weight
		for (int i=0;i<perceptrons.get(layer+1).size();i++) {  //  Iterates through the length of weights in the perceptron
			output = p.getOutput(i);
			forwardOutput = perceptrons.get(layer+1).get(i).getOutput();  // TODO: figure out how to plug in sigmoid derivative
			deltaWeight = a;
			deltaWeight *= 
		}
	}
	
	// **MATHEMATIC FUNCTIONS**
	
	// Performs matrix multiplication
	// for forward propagation functions
	// ONLY works if arr.length == mat.length (A.K.A columns)
	public double[] matMult(int layer, double[] arr, double[][] mat) {
		if (arr.length != mat.length) {
			System.out.printf("arr length of %d cannot match matrix column size of %d",arr.length,mat.length);
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
				perceptrons.get(layer).get(j).updateOutput(i, value);  // Updates the output array of the perceptron (used in backprop)
			}
			output[i] = sum;
		}
		return output;
	}
	
	// Performs the sigmoid function to standardize all values
	public double sigmoid(double val) {
		System.out.println(val);
		return val ;//1/(1+Math.exp(val));
	}
	// Performs the ReLU function to standardize all values
	public double relu(double val) {
		System.out.println(val);
		return Math.max(0, val);
	}
}
