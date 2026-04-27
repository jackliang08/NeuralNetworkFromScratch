package execution;

public class Perceptron {
	double[] weights;
	double bias;
	double output;
	
	
	// **CLASS SETUP**
	
	public Perceptron(int outputLength) {
		weights = new double[outputLength];
		output = 0;
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
	public double getOutput() {
		return output;
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
	public void updateOutput(double o) {
		output = o;
	}
	
}
