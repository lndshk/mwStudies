package study_examples;

import java.awt.Color;

import com.motivewave.platform.sdk.common.DataContext;
import com.motivewave.platform.sdk.common.DataSeries;
import com.motivewave.platform.sdk.common.Enums;

//A Java class that can return multiple values of different types
//encapsulate the items into a new object class
//and returning an object of class. 

//A class that is used to store and return 
//members of different types 

public class vrUtility { 
    
	public static double kamaLine(int index, DataContext ctx, int period, int kaFast, int kaSlow, Object input, double prevKama)
	{ 
		if (kaSlow < kaFast) return 0.0;
		if (index < period*2) return 0.0;
		DataSeries series=ctx.getDataSeries();
	    
	    double fastest = (double)2/(kaFast+1);  //cast the integers to double
	    double slowest = (double)2/(kaSlow+1);
	    
	    double change = Math.abs(series.getDouble(index-period, input) - series.getDouble(index, input)); 
	    double kamavol = 0;
	    // loop to sum last number of periods
		for(int i = 0; i < period; i++) {
			kamavol = kamavol + Math.abs(series.getDouble(index-i, input) - series.getDouble(index-(i+1), input));
		}

		// Prevent divide by zero
		if (kamavol == 0) kamavol = 0.00000000000001;

		// Generate the Efficiency Ratio (ER) and Smoothing Constant (SC)
	    double efficiencyRatio = change/kamavol;
	    double smoothingConstant = Math.pow(((efficiencyRatio*(fastest-slowest))+slowest), 2);

	    // Calculate the current KAMA Values
	    double currentKama = prevKama + smoothingConstant*(series.getDouble(index, input) - prevKama);

		return currentKama;

	} 

	public static Color colorFade(Color oldColor, Color newColor, double delta, double maxDelta, double threshold)
	{	
		
		double diff = delta - threshold; //subtract threshold to see how high delta is
		double range = maxDelta - threshold; //subtract threshold to get a proper range
		
		double ratio = diff / range;
		
		if( delta >= maxDelta) ratio = 1;
		
			
		    
		int red = (int)Math.abs((ratio * newColor.getRed()) + ((1 - ratio) * oldColor.getRed()));
		int green = (int)Math.abs((ratio * newColor.getGreen()) + ((1 - ratio) * oldColor.getGreen()));
		int blue = (int)Math.abs((ratio * newColor.getBlue()) + ((1 - ratio) * oldColor.getBlue()));
		
		Color colorGrad = new Color(red, green, blue);
		
		return colorGrad;  //returns the color gradient
		    
	}	    
		


} 

