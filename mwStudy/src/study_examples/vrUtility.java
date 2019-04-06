package study_examples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;

import com.motivewave.platform.sdk.common.*;
import com.motivewave.platform.sdk.common.Enums.MAMethod;
import com.motivewave.platform.sdk.common.desc.*;
import com.motivewave.platform.sdk.study.*;
import com.motivewave.platform.sdk.study.Study;
import com.motivewave.platform.sdk.common.BarSize;
import com.motivewave.platform.sdk.common.DataContext;
import com.motivewave.platform.sdk.common.DataSeries;
import com.motivewave.platform.sdk.common.Enums;
import com.motivewave.platform.sdk.common.Enums.MAMethod;
//import com.motivewave.platform.study.general3.Numb;  //In the utility file but could not resolve

//import study_examples.vrKAMA2.Values;


//import study_examples.vrSMImain.Values;
//import study_examples.vrSMItester.Signals;


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
} 

class vrSMItotoss {

	public static void getvrSMI(SMIinput a, int index, DataContext ctx)
	{	
		    if (index < a.hlPeriod) return; //return if the index is less than the high period

		    DataSeries series = ctx.getDataSeries();  //data series based on price
		    
		    double HH = series.highest(index, a.hlPeriod, Enums.BarInput.HIGH);
		    double LL = series.lowest(index, a.hlPeriod, Enums.BarInput.LOW);
		    double M = (HH + LL)/2.0;
		    double D = series.getClose(index) - M;
	
		    series.setDouble(index, a.D, D);
		    series.setDouble(index, a.HL , HH - LL);
	
		    if (index < a.hlPeriod + a.maPeriod) return;
	
		    series.setDouble(index, a.D_MA, series.ma(a.method, index, a.maPeriod, a.D));
		    series.setDouble(index, a.HL_MA, series.ma(a.method, index, a.maPeriod, a.HL));
		    
		    if (index < a.hlPeriod + a.maPeriod + a.smoothPeriod) return;
		    
		    Double D_SMOOTH = series.ma(a.method, index, a.smoothPeriod, a.D_MA);
		    Double HL_SMOOTH = series.ma(a.method, index, a.smoothPeriod, a.HL_MA);
		    
		    if (D_SMOOTH == null || HL_SMOOTH == null) return;
		    double HL2 = HL_SMOOTH/2;
		    a.SMId = 0.0; //double SMI = 0;
		    if (HL2 != 0) a.SMId = 100 * (D_SMOOTH/HL2);
		    
		    series.setDouble(index, a.SMIds, a.SMId);
		    // Joe - duplicate question, how can cast a.SMIds so that Values.SMI = a.SMIds in the vrSMImain calculate loop
		    
		    if (!series.isBarComplete(index)) return;
		      
		   
	}

}

