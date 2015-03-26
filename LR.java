
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LR {
   
    double eeta=0.001;   
    double[] weights;
    ArrayList<String[]> values = new ArrayList<String[]>();
    ArrayList<String[]> testvalues = new ArrayList<String[]>();
    int numOfRows=0;
    int numOfCols=0;
    int numOfTRows=0;
    int numOfTCols=0;
    int converged=0;
   
    public void train()
    {
    	System.out.println("Training...");
        weights = new double[numOfCols-1];
        double oldweights[] = weights.clone();
        double gradProd[] = new double[numOfRows];
        int ite=0;
        while(true)
        {
        	ite++;
        	double J=0;
        	double oldJ=J;
        	
        	for(int i=0;i<numOfRows;i++)
        	{
        		double error=0,prod=0,p=0,y=0,grad=0;
        		String[] x = new String[numOfCols];
        		x = values.get(i);
        		for(int j=0; j<numOfCols-1; j++){
        			prod = prod + (weights[j] * Double.parseDouble(x[j]));
        		}
        		p = (1/(1+Math.exp(-prod)));
        		y = Double.parseDouble(x[numOfCols-1]);
        		error = y - p;
        		J = J + (Double.parseDouble(x[numOfCols-1]) * Math.log(p)) + ((1-Double.parseDouble(x[numOfCols-1])) * Math.log(1-p)); 
        		for(int j=0; j<numOfCols-1; j++){
        			grad = grad + (error * Double.parseDouble(x[j]));
        			weights[j]= weights[j] + eeta*grad;
        		}
        	}
        	
        	/*Convergence Criteria*/
        	        	
        	//If the Likelihood variable doesn't vary by 0.001, then break the loop.
        	if(Math.abs(J-oldJ) < 0.001){
        		break;
        	}
        	
        	// Or If the difference between the old and new weight vectors is less than 0.001, break the loop
        	double sumw=0, sumow=0;
        	for(int k=0;k<weights.length;k++){
        		sumw = sumw + weights[k];
        		sumow = sumow + oldweights[k];
        	}
        	if(Math.abs(sumw-sumow) <= 0.001)
        	{
        		break;
        	}
         }
      }
        
    public void test()
    {
    	System.out.println("Predicting...");
    	int predict=0,correct=0,incorrect=0;
    	String x[] = new String[numOfTCols];
    	for (int i=0; i< numOfTRows; i++)
    	{
    		double prod=0,p=0;
    		x = testvalues.get(i);
    		for (int w=0; w < weights.length; w++)
    		{
                 prod = prod + (weights[w] * Double.parseDouble(x[w]));
        	}
    		
    		p = (1/(1+Math.exp(-prod)));
    		
    		
    		if(p < 0.5)
    			predict = 0;
    		else
    			predict = 1;
    		if(predict == Double.parseDouble(x[numOfTCols-1])){
    			correct++;
    		}
    		else{
    			incorrect++;
    		}
    			
        }
    	System.out.println("Results");
    	System.out.println("correct = "+correct + "\t" + "incorrect ="+ incorrect);
    }
   
    public void readCSV()
    {       
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("C:/Users/sudhakar553/workspace/LogisticRegression/src/zoo-train.csv"));
            BufferedReader reader1 = new BufferedReader(new FileReader("C:/Users/sudhakar553/workspace/LogisticRegression/src/zoo-test.csv"));
            String line;
            String line1;
            while ((line = reader.readLine()) != null){
                values.add(line.split(","));       
            }
            while ((line1 = reader1.readLine()) != null){
            	testvalues.add(line1.split(","));
            }
        }
       
        catch(FileNotFoundException e){
         e.printStackTrace();
        }
     
        catch(IOException e){
         e.printStackTrace();
        }
        numOfRows= values.size();
        numOfCols= values.get(0).length;
        numOfTRows = testvalues.size();
        numOfTCols = testvalues.get(0).length;
       
    }
   
    public static void main(String[] args)
    {
        LR lr = new LR();
        lr.readCSV();
        lr.train();
        lr.test();
    }

}