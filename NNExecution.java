package execution;
import java.util.*;
// TODO: figure out how to do multiple outputs
public class NNExecution {

	public static void main(String[] args) {
		double[] input1 ={1,1,1};
		int layers = 1;  // Change this to adjust layers
		
		NeuralNet nn1 = new NeuralNet(layers);
		for (int i=0;i<layers-1;i++) {  // Adds the perceptrons to each layer
			nn1.addPerceptrons(i,  3, 3);
		}
		nn1.addPerceptrons(layers-1,3,1);  // Adds the last hidden layer before output
		nn1.addPerceptrons(layers, 1, 1);  // Adds the output perceptron
		
		double[] o1,o2,o3;
		double[] outputs;
		for (int i=0;i<1000;i++) {
			outputs = nn1.forwardAll(input1);
			System.out.println(outputs[0]);
			
			System.out.println(Arrays.toString(nn1.perceptrons.get(nn1.perceptrons.size()-2).get(0).getWeights()));
			nn1.backwardOutput(0.7, 0.6);
			System.out.println(Arrays.toString(nn1.perceptrons.get(nn1.perceptrons.size()-2).get(0).getWeights()));
			System.out.println();
		}
		//o2 = nn1.sigForward(1, o1);
		

	}

}
