package execution;

public class Perceptron {
	double[] weights;
	double bias;
	double[] outputs;
	
	
	
	public Perceptron(int outputLength) {
		weights = new double[outputLength];
		outputs = new double[outputLength];
		for (int i=0;i<weights.length;i++) {
			weights[i] = Math.random() * 2 - 1;  // Assigns a random weight
		}
		bias = Math.random() * 2 -1;  // Assigns a random bias
	}


	public double[] getWeights() {
		return weights;
	}
	public double getWeight(int pos) {
		return weights[pos];
	}
	public double getBias() {
		return bias;
	}
	public double getOutput(int pos) {
		return outputs[pos];
	}
	
	// Changes the weights on the perceptron for forward propagation
	public void updateWeights(double[] newWeights) {
		if (newWeights.length != weights.length) {
			System.out.println("Lengths not equal");
			return;
		}
		for (int i=0;i<weights.length;i++) {
			weights[i] = newWeights[i];
		}
	}
	public void updateWeight(int pos, double w) {
		weights[pos] = w;
	}
	
	public void updateBias(double newBias) {
		bias = newBias;
	}
	public void updateOutput(int pos, double o) {
		outputs[pos] = o;
	}
	public void updateOutputs(double[] os) {
		outputs = os;
	}
	
	
	
	/*public Perceptron(double[] inputs) {
	this.inputs = inputs;
	weights = new double[inputs.length];
	for (int i=0;i<weights.length;i++) {
		weights[i] = Math.random() * 2 - 1;  // Assigns a random weight
	}
	bias = Math.random() * 2 -1;  // Assigns a random bias
}*/
	
	/*public void updateInputs(double[] newInputs) {
	if (newInputs.length != inputs.length) {
		System.out.println("Lengths not equal");
		return;
	}
	for (int i=0;i<inputs.length;i++) {
		inputs[i] = newInputs[i];
	}
}*/
	
	// Does forward propagation
	/*public double forward() {
		double output = bias;  // Adds bias
		for (int i=0;i<inputs.length;i++) {
			output += inputs[i] * weights[i];
		}
		return output;
	}*/
	
	
	
}
