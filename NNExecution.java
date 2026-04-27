package execution;
import java.util.*;
import java.io.*;
// TODO: figure out how to do multiple outputs (fixed update bias, retry multiple outputs)
public class NNExecution {

	public static void main(String[] args) throws IOException{
		Scanner trainReader = new Scanner(new File("src\\execution\\Spam.dat"));
		Scanner testReader = new Scanner(new File("src\\execution\\SpamTest.dat"));
		String[] lineSplit;
		int layers = 4;  // Change this to adjust layers
		int hiddenNeurons = 8;  // Adjusts number of neurons per hidden layer
		int inputNum = 57;
		int outputNum = 1;
		double learningRate = 0.001;
		int[] confusion = new int[2];
		
		NeuralNet nn1 = new NeuralNet(layers);
		nn1.addPerceptrons(0,inputNum,hiddenNeurons);
		for (int i=1;i<layers-1;i++) {  // Adds the perceptrons to each layer
			nn1.addPerceptrons(i, hiddenNeurons, hiddenNeurons);
		}
		nn1.addPerceptrons(layers-1,hiddenNeurons,outputNum);  // Adds the last hidden layer before output
		nn1.addPerceptrons(layers, outputNum, 1);  // Adds the output perceptron
		double[] inputs;
		double[] outputs;
		double[] targets; String target;
		
		
		int n=1;
		int epochSize = 10; int epoch = 1;
		while(trainReader.hasNextLine()) {
			
			// Reads the data, then stores the information in inputs and outputs
			if (!trainReader.hasNext()) break;
			lineSplit = trainReader.nextLine().split("[,]");
			
			inputs = new double[lineSplit.length-1];
			for (int inputInd=0;inputInd<lineSplit.length-1;inputInd++) {  // Stores the input data
				inputs[inputInd] = Double.parseDouble(lineSplit[inputInd]);
			}
			
			
			
			//Assigns output based on a one-hot vector system
			target = lineSplit[lineSplit.length-1];
			targets = new double[] {Integer.parseInt(target)};
			
			
			// Running the Neural Network for one epoch
			outputs = nn1.forwardAll(inputs);
			
			// Tests the testing data after each epoch
			if (n++ % epochSize == 0) {
				while (testReader.hasNextLine()) {
					lineSplit = testReader.nextLine().split("[,]");
					inputs = new double[lineSplit.length-1];
					for (int inputInd=0;inputInd<lineSplit.length-1;inputInd++) {  // Stores the input data
						inputs[inputInd] = Double.parseDouble(lineSplit[inputInd]);
					}
					target = lineSplit[lineSplit.length-1];
					// returns 1
					outputs = nn1.forwardAll(inputs);
					outputs[0] = Math.round(outputs[0]);
					if (Double.parseDouble(target) == outputs[0]) {
						confusion[0] += 1;
					}
					else {
						confusion[1] += 1;
					}
				}
				System.out.printf("%d %.2f%n",epoch++,100.0*confusion[0]/(confusion[0]+confusion[1]));
				testReader = new Scanner(new File("src\\execution\\SpamTest.dat"));
			}
			nn1.backwardAll(learningRate, targets);

		}
		
		
	
	}
		
		
	
	// Converts target (categorical) to one-hot vector
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
