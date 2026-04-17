package execution;
import java.util.*;
import java.io.*;
// TODO: figure out how to do multiple outputs (fixed update bias, retry multiple outputs)
public class NNExecution {

	public static void main(String[] args) throws IOException{
		Scanner reader = new Scanner(new File("src\\execution\\Iris.dat"));
		String[] lineSplit;
		int layers = 1;  // Change this to adjust layers
		
		NeuralNet nn1 = new NeuralNet(layers);
		for (int i=0;i<layers-1;i++) {  // Adds the perceptrons to each layer
			nn1.addPerceptrons(i,  3, 3);
		}
		nn1.addPerceptrons(layers-1,4,3);  // Adds the last hidden layer before output
		nn1.addPerceptrons(layers, 3, 1);  // Adds the output perceptron
		
		double[] inputs;
		double[] outputs;
		double[] targets; String target;
		for (int i=0;i<150;i++) {
			
			// Reads the data, then stores the information in inputs and outputs
			if (!reader.hasNext()) break;
			lineSplit = reader.nextLine().split("[,]");
			
			inputs = new double[lineSplit.length-1];
			for (int inputInd=0;inputInd<lineSplit.length-1;inputInd++) {  // Stores the input data
				inputs[inputInd] = Double.parseDouble(lineSplit[inputInd]);
			}
			
			//Assigns output based on a one-hot vector system
			target = lineSplit[lineSplit.length-1];
			if (target.equals("Iris-setosa")) targets = new double[] {1,0,0};
			else if (target.equals("Iris-versicolor")) targets = new double[] {0,1,0};
			else if (target.equals("Iris-virginica")) targets = new double[] {0,0,1};
			else targets = new double[] {0,0,0};
			
			
			// Running the Neural Network for one epoch
			System.out.printf("Epoch: %d%n", i);
			outputs = nn1.forwardAll(inputs);
			
//			System.out.println(outputs[0]);
//			System.out.println(outputs[1]);
//			System.out.println(outputs[2]);
			
			//System.out.println(Arrays.toString(nn1.perceptrons.get(nn1.perceptrons.size()-2).get(0).getWeights()));
			nn1.backwardOutput(0.1, targets);

			//System.out.println(Arrays.toString(nn1.perceptrons.get(nn1.perceptrons.size()-2).get(0).getWeights()));
			System.out.println();
		}
		// 6.5,3.0,5.5,1.8,Iris-virginica
		outputs = nn1.forwardAll(new double[] {6.5,3.0,5.5,1.8});
		
		// Returns from one-hot to a categorical output
		double[] oneHot = convertOneHot(outputs);
		if (oneHot[0] == 1) {
			System.out.println("Iris-setosa");
		}
		if (oneHot[1] == 1) {
			System.out.println("Iris-versicolor");
		}
		if (oneHot[2] == 1) {
			System.out.println("Iris-virginica");
		}
		System.out.println(Arrays.toString(outputs));
	}
	static double[] convertOneHot(double[] oneHot) {
		double max=oneHot[0]; int maxIndex=0;
		for (int i=0;i<oneHot.length;i++) {
			if (oneHot[i] > max) {
				max = oneHot[i];
				maxIndex = i;
			}
		}
		
		double[] maxOneHot = new double[oneHot.length];
		maxOneHot[maxIndex] = 1;
		return maxOneHot;
	}

}
