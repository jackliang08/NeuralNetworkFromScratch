package execution;
import java.util.*;

public class NNExecution {

	public static void main(String[] args) {
		double[] input1 ={1,-1,5};
		int layers = 3;  // Change this to adjust layers
		
		NeuralNet nn1 = new NeuralNet(layers);
		for (int i=0;i<layers-1;i++) {  // Adds the perceptrons to each layer
			nn1.addPerceptrons(i, 3, 3);
		}
		nn1.addPerceptrons(layers-1,3,1);  // Adds the last hidden layer before output
		//nn1.addPerceptrons(layers, 1, 1);  // Adds the output perceptron
		
		double[] o1,o2,o3;
		double output;
		
		output = nn1.forwardAll(input1);
		System.out.println(output);
		//o2 = nn1.sigForward(1, o1);
		

	}

}
