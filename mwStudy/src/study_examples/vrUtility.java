package study_examples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;

import com.motivewave.platform.sdk.common.BarSize;
import com.motivewave.platform.sdk.common.DataContext;
import com.motivewave.platform.sdk.common.DataSeries;
import com.motivewave.platform.sdk.common.Enums;
import com.motivewave.platform.sdk.common.Enums.MAMethod;
//import com.motivewave.platform.study.general3.Numb;  //In the utility file but could not resolve


//import study_examples.vrSMImain.Values;
//import study_examples.vrSMItester.Signals;


//A Java class that can return multiple values of different types
//encapsulate the items into a new object class
//and returning an object of class. 

//A class that is used to store and return 
//members of different types 

public class vrUtility { 
    
	public static double[] SMIinput(int hl, int ma, int sp, Enums.MAMethod meth, 
			DataSeries Dsig, DataSeries HLsig, DataSeries D_MAsig, DataSeries HL_MAsig,
			DataSeries SMIsig, Double SMIdouble)  //add ', DataSeries SIGsig' if I want to add signals
	{ 
		double tossme = 12.1;
		double tossme2 = 12.1;
		double[] value = {tossme, tossme2};
		return value;

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

